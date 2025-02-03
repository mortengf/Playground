from datasets import load_dataset, DatasetDict, Dataset
import pandas as pd
import ast
import datasets
from tqdm import tqdm
import time
from IPython.display import display

from transformers import GPT2Tokenizer, GPT2LMHeadModel
import torch
import torch.nn as nn
import torch.optim as optim
from torch.utils.data import Dataset, DataLoader, random_split

class LanguageDataset(Dataset):
    """
    An extension of the Dataset object to:
      - Make training loop cleaner
      - Make ingestion easier from pandas df's
    """
    def __init__(self, df, tokenizer):
        self.labels = df.columns
        self.data = df.to_dict(orient='records')
        self.tokenizer = tokenizer
        x = self.fittest_max_length(df)
        self.max_length = x

    def __len__(self):
        return len(self.data)

    def __getitem__(self, idx):
        x = self.data[idx][self.labels[0]]
        y = self.data[idx][self.labels[1]]
        text = f"{x} | {y}"
        tokens = self.tokenizer.encode_plus(text, return_tensors='pt', max_length=128, padding='max_length', truncation=True)
        return tokens

    def fittest_max_length(self, df):
      """
      Smallest power of two larger than the longest term in the data set.
      Important to set up max length to speed training time.
      """
      max_length = max(len(max(df[df.columns[0]], key=len)), len(max(df[df.columns[1]], key=len)))
      x = 2
      while x < max_length: x = x * 2
      return x

# Load data set from huggingface
dataset = load_dataset("erwanlc/cocktails_recipe_no_brand")

# Convert to a pandas dataframe
data = [{'title': item['title'], 'raw_ingredients': item['raw_ingredients']} for item in dataset['train']]
df = pd.DataFrame(data)

# Just extract the ingredient names, nothing else
df.raw_ingredients = df.raw_ingredients.apply(lambda x: ', '.join([y[1] for y in ast.literal_eval(x)]))
display(df.head())

# Models need to be attached to hardware
# If you have an NVIDIA GPU attached, use 'cuda'
if torch.cuda.is_available():
    device = torch.device('cuda')
else:
    # If Apple Silicon, set to 'mps' - otherwise 'cpu' (not advised)
    try:
        device = torch.device('mps')
    except Exception:
        device = torch.device('cpu')

# The tokenizer turns texts to numbers (and vice-versa)
tokenizer = GPT2Tokenizer.from_pretrained('distilgpt2')

# The transformer
model = GPT2LMHeadModel.from_pretrained('distilgpt2').to(device)

# Model params
BATCH_SIZE = 8

# Cast the Huggingface data set as a LanguageDataset we defined above
dataset = LanguageDataset(df, tokenizer)

# Create train, valid
train_size = int(0.8 * len(dataset))
valid_size = len(dataset) - train_size
train_data, valid_data = random_split(dataset, [train_size, valid_size])

# Make the iterators
train_loader = DataLoader(train_data, batch_size=BATCH_SIZE, shuffle=True)
valid_loader = DataLoader(valid_data, batch_size=BATCH_SIZE)

# Set the number of epochs
num_epochs = 3

# Training parameters
batch_size = BATCH_SIZE
model_name = 'distilgpt2'
gpu = 0

# Set the learning rate and loss function
## CrossEntropyLoss measures how close answers to the truth.
## More punishing for high confidence wrong answers
criterion = nn.CrossEntropyLoss(ignore_index = tokenizer.pad_token_id)
optimizer = optim.Adam(model.parameters(), lr=5e-4)
tokenizer.pad_token = tokenizer.eos_token

# Init a results dataframe
results = pd.DataFrame(columns=['epoch', 'transformer', 'batch_size', 'gpu',
                                'training_loss', 'validation_loss', 'epoch_duration_sec'])
# The training loop
for epoch in range(num_epochs):
    start_time = time.time()  # Start the timer for the epoch

    # Training
    ## This line tells the model we're in 'learning mode'
    model.train()
    epoch_training_loss = 0
    train_iterator = tqdm(train_loader, desc=f"Training Epoch {epoch+1}/{num_epochs} Batch Size: {batch_size}, Transformer: {model_name}")
    for batch in train_iterator:
        optimizer.zero_grad()
        inputs = batch['input_ids'].squeeze(1).to(device)
        targets = inputs.clone()
        outputs = model(input_ids=inputs, labels=targets)
        loss = outputs.loss
        loss.backward()
        optimizer.step()
        train_iterator.set_postfix({'Training Loss': loss.item()})
        epoch_training_loss += loss.item()
    
    avg_epoch_training_loss = epoch_training_loss / len(train_iterator)

    # Validation
    ## This line below tells the model to 'stop learning'
    model.eval()
    epoch_validation_loss = 0
    total_loss = 0
    valid_iterator = tqdm(valid_loader, desc=f"Validation Epoch {epoch+1}/{num_epochs}")
    with torch.no_grad():
        for batch in valid_iterator:
            inputs = batch['input_ids'].squeeze(1).to(device)
            targets = inputs.clone()
            outputs = model(input_ids=inputs, labels=targets)
            loss = outputs.loss
            total_loss += loss
            valid_iterator.set_postfix({'Validation Loss': loss.item()})
            epoch_validation_loss += loss.item()

    avg_epoch_validation_loss = epoch_validation_loss / len(valid_loader)

    end_time = time.time()  # End the timer for the epoch
    epoch_duration_sec = end_time - start_time  # Calculate the duration in seconds

    new_row = {'transformer': model_name,
               'batch_size': batch_size,
               'gpu': gpu,
               'epoch': epoch+1,
               'training_loss': avg_epoch_training_loss,
               'validation_loss': avg_epoch_validation_loss,
               'epoch_duration_sec': epoch_duration_sec}  # Add epoch_duration to the dataframe

    results.loc[len(results)] = new_row
    print(f"Epoch: {epoch+1}, Validation Loss: {total_loss/len(valid_loader)}")