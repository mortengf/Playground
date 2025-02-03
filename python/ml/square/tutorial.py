from datasets import load_dataset, DatasetDict, Dataset
import pandas as pd
import ast
import datasets
from tqdm import tqdm
import time
from IPython.display import display

# Load data set from huggingface
dataset = load_dataset("erwanlc/cocktails_recipe_no_brand")

# Convert to a pandas dataframe
data = [{'title': item['title'], 'raw_ingredients': item['raw_ingredients']} for item in dataset['train']]
df = pd.DataFrame(data)

# Just extract the ingredient names, nothing else
df.raw_ingredients = df.raw_ingredients.apply(lambda x: ', '.join([y[1] for y in ast.literal_eval(x)]))
display(df.head())
