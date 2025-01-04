# Prerequisites: 

* python 3
* Install the following packages (via `pip`):


```
pip install "transformers[sentencepiece]"
pip install torch torchvision #PyTorch
pip install datasets
```

# Hugging Face Terminology

* [Task](https://huggingface.co/tasks) ([alternative link](https://huggingface.co/docs/hub/en/models-tasks)) (pipeline type): “shape” of a model’s API, e.g. natural language processing, text-to-image (and vice versa), etc.
* Model: a neural network that will perform a task.
  * Transformer: a model based on the [Transformer](https://research.google/blog/transformer-a-novel-neural-network-architecture-for-language-understanding/) architecture (from 2017 paper "Attention Is All You Need" by Vaswani et al.) that can be applied to multiple modalities. Most modern models are Transformer-based.
    * Large Language Model (LLM): a model specifically designed and trained for natural language (text) processing tasks. Examples: [BERT](https://huggingface.co/docs/transformers/en/model_doc/bert), [DistilBERT](https://huggingface.co/docs/transformers/en/model_doc/distilbert) and [OpenAI GPT](https://huggingface.co/docs/transformers/en/model_doc/openai-gpt)
    * Text-to-image examples: DALL-E, Stable Diffusion, Imagen, etc.
* [Dataset](https://huggingface.co/docs/datasets/en/index): data divided into data for training (`train`), evaluation (`test`) and often validation (of hyper parameters? (logits?)) (`validation`). Examples: 
  * [General Language Understanding Evaluation (GLUE)](https://huggingface.co/datasets/nyu-mll/glue) benchmark:
    * [Corpus of Linguistic Acceptability (cola)](https://huggingface.co/datasets/nyu-mll/glue/viewer/cola)
    * [Multi-Genre Natural Language Inference (mnli)](https://huggingface.co/datasets/nyu-mll/glue/viewer/mnli)
    * [Microsoft Research Paraphrase Corpus (mrpc)](https://huggingface.co/datasets/nyu-mll/glue/viewer/mrpc/train)
    * [Stanford Sentiment Treebank (sst2)](https://huggingface.co/datasets/nyu-mll/glue/viewer/sst2)
  * Other:
    * ...
