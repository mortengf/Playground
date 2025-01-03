# Prerequisites: 

* python 3
* Install the following packages (via `pip`):


```
pip install "transformers[sentencepiece]"
pip install torch torchvision #PyTorch
pip install datasets
```

# Hugging Face Terminology

* [Task](https://huggingface.co/tasks) ([alternative link](https://huggingface.co/docs/hub/en/models-tasks)) (pipeline type): “shape” of a model’s API, e.g. image classification, token classification, question answering, etc.
* Model: the thingymagik that will perform a task, e.g. [BERT](https://huggingface.co/docs/transformers/en/model_doc/bert), [DistilBERT](https://huggingface.co/docs/transformers/en/model_doc/distilbert) and [OpenAI GPT](https://huggingface.co/docs/transformers/en/model_doc/openai-gpt)
* [Dataset](https://huggingface.co/docs/datasets/en/index): data divided into data for training (`train`), evaluation (`test`) and often validation (of hyper parameters? (logits?)) (`validation`). Examples: 
  * [General Language Understanding Evaluation (GLUE)](https://huggingface.co/datasets/nyu-mll/glue) benchmark:
    * [Corpus of Linguistic Acceptability (cola)](https://huggingface.co/datasets/nyu-mll/glue/viewer/cola)
    * [Multi-Genre Natural Language Inference (mnli)](https://huggingface.co/datasets/nyu-mll/glue/viewer/mnli)
    * [Microsoft Research Paraphrase Corpus (mrpc)](https://huggingface.co/datasets/nyu-mll/glue/viewer/mrpc/train)
    * [Stanford Sentiment Treebank (sst2)](https://huggingface.co/datasets/nyu-mll/glue/viewer/sst2)
  * Other:
    * ...
