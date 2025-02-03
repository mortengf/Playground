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