# https://huggingface.co/learn/nlp-course/chapter3/2

from datasets import load_dataset
from transformers import AutoTokenizer
from transformers import DataCollatorWithPadding

raw_datasets = load_dataset("glue", "mrpc")
#print(raw_datasets)

raw_train_dataset = raw_datasets["train"]
#print(raw_train_dataset.features)

train_row1 = raw_train_dataset[15]
#print(train_row1)

train_row2 = raw_train_dataset[87]
#print(train_row2)

checkpoint = "bert-base-uncased"
tokenizer = AutoTokenizer.from_pretrained(checkpoint)

train_row1_sentence1_inputs = tokenizer(train_row1["sentence1"])
#print('train row 1, sentence 1 inputs: ' + str(train_row1_sentence1_inputs))

train_row1_sentence2_inputs = tokenizer(train_row1["sentence2"])
#print('train row 1, sentence 2 inputs: ' + str(train_row1_sentence2_inputs))
train_row1_sentence2_words = tokenizer.convert_ids_to_tokens(train_row1_sentence2_inputs["input_ids"])
#print('train row 1, sentence 2 words: ' + str(train_row1_sentence2_words))

train_row1_both_sentences_inputs = tokenizer(train_row1["sentence1"], train_row1["sentence2"])
#print('train row 1, both sentences inputs: ' + str(train_row1_both_sentences_inputs))
train_row1_both_sentences_words = tokenizer.convert_ids_to_tokens(train_row1_both_sentences_inputs["input_ids"])
#print('train row 1, both sentences words: ' + str(train_row1_both_sentences_words))

tokenized_dataset = tokenizer(
    raw_train_dataset["sentence1"],
    raw_train_dataset["sentence2"],
    padding=True,
    truncation=True,
)

def tokenize_function(dataset_row):
    return tokenizer(dataset_row["sentence1"], dataset_row["sentence2"], truncation=True)
    
tokenized_datasets = raw_datasets.map(tokenize_function, batched=True)
print(tokenized_datasets)

data_collator = DataCollatorWithPadding(tokenizer=tokenizer)
samples = tokenized_datasets["train"][:8]
samples = {k: v for k, v in samples.items() if k not in ["idx", "sentence1", "sentence2"]}
#[print(len(x)) for x in samples["input_ids"]]

batch = data_collator(samples)
# See https://chatgpt.com/share/6777b88f-dbdc-8007-953a-6059a9d88f21
{k: (print(f"Key: {k}, Shape: {v.shape}") or v.shape) for k, v in batch.items()}
