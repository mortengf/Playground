# https://huggingface.co/learn/nlp-course/chapter3/2

from datasets import load_dataset
from transformers import AutoTokenizer
from transformers import DataCollatorWithPadding

# https://github.com/huggingface/transformers/blob/main/examples/pytorch/text-classification/run_glue.py#L55
task_to_keys = {
    "cola": ("sentence", None),
    "mnli": ("premise", "hypothesis"),
    "mrpc": ("sentence1", "sentence2"),
    "qnli": ("question", "sentence"),
    "qqp": ("question1", "question2"),
    "rte": ("sentence1", "sentence2"),
    "sst2": ("sentence", None),
    "stsb": ("sentence1", "sentence2"),
    "wnli": ("sentence1", "sentence2"),
}

task = "mnli" # TODO: get from user

raw_datasets = load_dataset("glue", task)
print(raw_datasets)

raw_train_dataset = raw_datasets["train"]
#print(raw_train_dataset)
train_row = raw_train_dataset[0]
#print(train_row)

key_names_required = task_to_keys[task]
key_names_dataset_row = train_row.keys()

if not set(key_names_required).issubset(set(key_names_dataset_row)):
	error_message = "All required keys {} of chosen task '{}' are not present in keys of first data set row {}".format(key_names_required, task, str(key_names_dataset_row))
	raise ValueError(error_message)
	#print(f"All required keys {key_names_required} of chosen task '{task}' are not present in keys of first data set row {key_names_dataset_row}")

checkpoint = "bert-base-uncased"
tokenizer = AutoTokenizer.from_pretrained(checkpoint)

def tokenize_function(dataset_row):
	# https://chatgpt.com/share/6777eeb0-cd38-8007-a01b-f8d0a9467148
	# TODO: ok to use a space as separator? Isn't the separator model/task-specific? (see https://huggingface.co/learn/nlp-course/en/chapter2/6?fw=pt#special-tokens)
    combined_texts = [" ".join([dataset_row[key_name][row_number] for key_name in key_names_required]) for row_number in range(len(dataset_row[key_names_required[0]]))]
    return tokenizer(combined_texts, truncation=True)
    
tokenized_datasets = raw_datasets.map(tokenize_function, batched=True)
print(tokenized_datasets)

data_collator = DataCollatorWithPadding(tokenizer=tokenizer)
samples = tokenized_datasets["train"][:8]
key_names_not_empty = ["idx"]
key_names_not_empty.extend(filter(None, key_names_required))
print(key_names_not_empty)
samples = {k: v for k, v in samples.items() if k not in key_names_not_empty}
[print(len(x)) for x in samples["input_ids"]]

batch = data_collator(samples)
# See https://chatgpt.com/share/6777b88f-dbdc-8007-953a-6059a9d88f21
{k: (print(f"Key: {k}, Shape: {v.shape}") or v.shape) for k, v in batch.items()}
