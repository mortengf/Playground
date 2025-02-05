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

print(f"Number of unique drug names {len(drug_dataset['train'].unique('drugName'))}")
print(f"Number of unique conditions {len(drug_dataset['train'].unique('condition'))}")
