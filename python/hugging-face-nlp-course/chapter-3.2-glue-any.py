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

task = "mrpc" # TODO: get from CLI arg

raw_datasets = load_dataset("glue", task)
print(raw_datasets)

raw_train_dataset = raw_datasets["train"]
#print(raw_train_dataset)
train_row = raw_train_dataset[0]
#print(train_row)

key_names_required = list(filter(None, task_to_keys[task]))
key_names_dataset_row = train_row.keys()

if not set(key_names_required).issubset(set(key_names_dataset_row)):
	error_message = "All required keys {} of chosen task '{}' are not present in keys of first data set row {}".format(key_names_required, task, str(key_names_dataset_row))
	raise ValueError(error_message)
	#print(f"All required keys {key_names_required} of chosen task '{task}' are not present in keys of first data set row {key_names_dataset_row}")

checkpoint = "bert-base-uncased"
tokenizer = AutoTokenizer.from_pretrained(checkpoint)

def tokenize_function(dataset_rows):
	# See https://chatgpt.com/share/677805fb-2f2c-8007-9dfc-7f35a81c6ac9
	number_of_rows_in_batch = len(dataset_rows[key_names_required[0]])
	combined_values = ["[SEP]".join([dataset_rows[key_name][current_row_number] for key_name in key_names_required]) for current_row_number in range(number_of_rows_in_batch)]
	return tokenizer(combined_values, truncation=True)
    
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
