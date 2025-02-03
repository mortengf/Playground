# https://huggingface.co/learn/nlp-course/chapter3/2

from datasets import load_dataset
from transformers import AutoTokenizer
from transformers import DataCollatorWithPadding

raw_datasets = load_dataset("glue", "sst2")
print(raw_datasets)

raw_train_dataset = raw_datasets["train"]

train_row = raw_train_dataset[15]
print(train_row)

checkpoint = "bert-base-uncased"
tokenizer = AutoTokenizer.from_pretrained(checkpoint)

train_row_sentence_inputs = tokenizer(train_row["sentence"])
print('train row, sentence inputs: ' + str(train_row_sentence_inputs))

train_row_sentence_words = tokenizer.convert_ids_to_tokens(train_row_sentence_inputs["input_ids"])
print('train row, sentence words: ' + str(train_row_sentence_words))

tokenized_dataset = tokenizer(
    raw_train_dataset["sentence"],
    padding=True,
    truncation=True,
)

def tokenize_function(dataset_row):
    return tokenizer(dataset_row["sentence"], truncation=True)
    
tokenized_datasets = raw_datasets.map(tokenize_function, batched=True)
print(tokenized_datasets)

data_collator = DataCollatorWithPadding(tokenizer=tokenizer)
samples = tokenized_datasets["train"][:8]
samples = {k: v for k, v in samples.items() if k not in ["idx", "sentence"]}
#[print(len(x)) for x in samples["input_ids"]]

batch = data_collator(samples)
# See https://chatgpt.com/share/6777b88f-dbdc-8007-953a-6059a9d88f21
{k: (print(f"Key: {k}, Shape: {v.shape}") or v.shape) for k, v in batch.items()}
