# https://huggingface.co/learn/nlp-course/chapter3/3

from datasets import load_dataset
from transformers import AutoTokenizer
from transformers import DataCollatorWithPadding

from transformers import TrainingArguments
from transformers import AutoModelForSequenceClassification
from transformers import Trainer

import numpy as np
import evaluate

raw_datasets = load_dataset("glue", "wnli") # without access to a GPU, use a small-ish data set
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

def compute_metrics(eval_preds):
    metric = evaluate.load("glue", "wnli")
    logits, labels = eval_preds
    predictions = np.argmax(logits, axis=-1)
    return metric.compute(predictions=predictions, references=labels)

trainer = Trainer(
    model,
    training_args,
    train_dataset=tokenized_datasets["train"],
    eval_dataset=tokenized_datasets["validation"],
    data_collator=data_collator,
    tokenizer=tokenizer,
    compute_metrics=compute_metrics
)

'''
On my MacBook Pro 2018 with 16 GB RAM, this step runs out of memory:

RuntimeError: MPS backend out of memory (MPS allocated: 2.42 GB, other allocations: 4.33 GB, max allowed: 6.80 GB). Tried to allocate 89.42 MB on private pool. Use PYTORCH_MPS_HIGH_WATERMARK_RATIO=0.0 to disable upper limit for memory allocations (may cause system failure).

'''
trainer.train()

predictions = trainer.predict(tokenized_datasets["validation"])
print(predictions.predictions.shape, predictions.label_ids.shape)

preds = np.argmax(predictions.predictions, axis=-1)
print(preds)
