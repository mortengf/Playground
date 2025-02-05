# wget "https://archive.ics.uci.edu/ml/machine-learning-databases/00462/drugsCom_raw.zip"
# unzip drugsCom_raw.zip

from datasets import load_dataset

data_files = {"train": "drugsComTrain_raw.tsv", "test": "drugsComTest_raw.tsv"}
# \t is the tab character in Python
drug_dataset = load_dataset("csv", data_files=data_files, delimiter="\t")

# Peek at the first few examples
drug_sample = drug_dataset["train"].shuffle(seed=42).select(range(1000))
for key, value in drug_sample[:3].items():
    print(f"{key}: {value}")

# Unique values of column "Unnamed: 0"?
for split in drug_dataset.keys():
    assert len(drug_dataset[split]) == len(drug_dataset[split].unique("Unnamed: 0"))

drug_dataset = drug_dataset.rename_column(
    original_column_name="Unnamed: 0", new_column_name="patient_id"
)
print(drug_dataset)

print(f"Number of unique 'training' drug names {len(drug_dataset['train'].unique('drugName'))}")
print(f"Number of unique 'training' conditions {len(drug_dataset['train'].unique('condition'))}")

print(f"Number of unique 'test' drug names {len(drug_dataset['test'].unique('drugName'))}")
print(f"Number of unique 'test' conditions {len(drug_dataset['test'].unique('condition'))}")

# Avoid AttributeError: 'NoneType' object has no attribute 'lower'
drug_dataset = drug_dataset.filter(lambda x: x["condition"] is not None)

# normalize "condition" column
def lowercase_condition(example):
    return {"condition": example["condition"].lower()}
drug_dataset.map(lowercase_condition)
print(drug_dataset["train"]["condition"][:3])

# Add new column to data set: "review_length"
def compute_review_length(example):
    return {"review_length": len(example["review"].split())}

drug_dataset = drug_dataset.map(compute_review_length)
# Inspect the first training example
print(drug_dataset["train"][0])

three_shortest_reviews = drug_dataset["train"].sort("review_length")[:3]
print(f"Three shortest reviews: {three_shortest_reviews}")

drug_dataset = drug_dataset.filter(lambda x: x["review_length"] > 30)
print(drug_dataset.num_rows)

# Sorting descending causes the process to hang - why?
#three_longest_reviews = drug_dataset["train"].sort("review_length", reverse=True)[:3]
#print(f"Three longest reviews: {three_longest_reviews}")
