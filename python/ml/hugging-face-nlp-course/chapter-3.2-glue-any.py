# https://huggingface.co/learn/nlp-course/chapter3/2

from datasets import load_dataset
from transformers import AutoTokenizer
from transformers import DataCollatorWithPadding

# https://github.com/huggingface/transformers/blob/main/examples/pytorch/text-classification/run_glue.py#L55
dataset_to_keys = {
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

dataset = "mrpc" # TODO: get from CLI arg

raw_datasets = load_dataset("glue", dataset)
print(raw_datasets)

raw_train_dataset = raw_datasets["train"]
#print(raw_train_dataset)
train_row = raw_train_dataset[0]
#print(train_row)

key_names_required = list(filter(None, dataset_to_keys[dataset]))
key_names_dataset_row = train_row.keys()

if not set(key_names_required).issubset(set(key_names_dataset_row)):
	error_message = "All required keys {} of chosen dataset '{}' are not present in keys of first data set row {}".format(key_names_required, dataset, str(key_names_dataset_row))
	raise ValueError(error_message)
	#print(f"All required keys {key_names_required} of chosen dataset '{dataset}' are not present in keys of first data set row {key_names_dataset_row}")

checkpoint = "bert-base-uncased"
tokenizer = AutoTokenizer.from_pretrained(checkpoint)

# https://chatgpt.com/share/677805fb-2f2c-8007-9dfc-7f35a81c6ac9
# 
# "In machine learning, an example is usually defined as the set of features that we feed to the model. 
# In some contexts, these features will be the set of columns in a Dataset, but in others (like here and for question answering), 
# multiple features can be extracted from a single example and belong to a single column."
# From https://huggingface.co/learn/nlp-course/en/chapter5/3
# 
def tokenize_function(examples):
	number_of_rows_in_batch = len(examples[key_names_required[0]])

	# https://chatgpt.com/share/677be5a2-fbdc-8007-8fbf-751f6eff76ab
	'''	
	sentence1	sentence2
	"The sky"	"is blue."
	"Grass"		"is green."
	
	# BERT-style tokenization
	combined_values = [
    	    "[CLS] The sky [SEP] is blue. [SEP]",
    	    "[CLS] Grass [SEP] is green. [SEP]"
	]
	'''
	combined_values = [
		"[CLS] " + " [SEP] ".join([examples[key_name][current_row_number] for key_name in key_names_required]) + " [SEP]"
		for current_row_number in range(number_of_rows_in_batch)
	]
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

# https://chatgpt.com/share/6777b88f-dbdc-8007-953a-6059a9d88f21
{k: (print(f"Key: {k}, Shape: {v.shape}") or v.shape) for k, v in batch.items()}
