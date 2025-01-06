# https://huggingface.co/learn/nlp-course/chapter3/3

from datasets import load_dataset
from transformers import AutoTokenizer
from transformers import DataCollatorWithPadding

from transformers import TrainingArguments
from transformers import AutoModelForSequenceClassification
from transformers import Trainer

raw_datasets = load_dataset("glue", "mrpc")
raw_train_dataset = raw_datasets["train"]

checkpoint = "bert-base-uncased"
tokenizer = AutoTokenizer.from_pretrained(checkpoint)

tokenized_dataset = tokenizer(
    raw_train_dataset["sentence1"],
    raw_train_dataset["sentence2"],
    padding=True,
    truncation=True,
)

def tokenize_function(dataset_row):
    return tokenizer(dataset_row["sentence1"], dataset_row["sentence2"], truncation=True)
    
tokenized_datasets = raw_datasets.map(tokenize_function, batched=True)

data_collator = DataCollatorWithPadding(tokenizer=tokenizer)

training_args = TrainingArguments("test-trainer")

model = AutoModelForSequenceClassification.from_pretrained(checkpoint, num_labels=2)

trainer = Trainer(
    model,
    training_args,
    train_dataset=tokenized_datasets["train"],
    eval_dataset=tokenized_datasets["validation"],
    data_collator=data_collator,
    tokenizer=tokenizer,
)

trainer.train()