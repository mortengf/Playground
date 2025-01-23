# Prerequisites: 

* python 3
* Install the following packages (via `pip`):


```
pip install "transformers[sentencepiece]"
pip install transformers[torch]
pip install torch torchvision #PyTorch
pip install datasets
pip install evaluate
```

# Machine Learning (ML) / Hugging Face Terminology

* [Task](https://huggingface.co/tasks) (generic): the task to be performed / problem to be solved.
  * Types:
     * Classification: predict to which class an example belongs (which label does it have?)
     * Regression: predict a numeric value 
  * Examples: natural language processing (NLP), text-to-image (and vice versa), etc.
  * [Hugging Face Task / pipeline Type](https://huggingface.co/docs/hub/en/models-tasks): "Tasks, or pipeline types, describe the “shape” of each model’s API (inputs and outputs) and are used to determine which Inference API and widget we want to display for any given model."
* [Model](https://learn.microsoft.com/en-us/windows/ai/windows-ml/what-is-a-machine-learning-model): a neural network that will perform a task.
  * [Transformer](https://huggingface.co/docs/transformers/en/index): an ML model based on the [Transformer](https://research.google/blog/transformer-a-novel-neural-network-architecture-for-language-understanding/) architecture (from 2017 paper "Attention Is All You Need" by Vaswani et al.) that can be applied to multiple modalities. Most modern models are Transformer-based.
    * Large Language Model (LLM): a model specifically designed and trained for natural language (text) processing tasks. Examples: [BERT (Google)](https://huggingface.co/docs/transformers/en/model_doc/bert), [DistilBERT (Hugging Face)](https://huggingface.co/docs/transformers/en/model_doc/distilbert) and [GPT (OpenAI)](https://huggingface.co/docs/transformers/en/model_doc/openai-gpt)
    * Text-to-image examples: DALL-E, Stable Diffusion, Imagen, etc.
* [Paradigm/approach](https://www.wolfram.com/language/introduction-machine-learning/machine-learning-paradigms/):
    * Supervised: model learns (train a predictive model) from a set of input (feature)-output pairs (examples) to predict an output value (label) for a new, unseen input.
    * Unsupervised: perform e.g. clustering, dimensionality reduction, imputation (fill in the gaps) or generative modeling based on a set of unlabelled examples
    * Reinforcement (interactive): model interacts with an environment to, over time, improve performance of some function
    * The pre-training part of models like BERT and GPT is typically unsupervised working on unlabelled data, whereas the fine-tuning (transfer learning) part is typically supervised, working on labeled data (e.g. 0: not equivalent, 1: equivalent).
* [Dataset](https://huggingface.co/docs/datasets/en/index): data divided into the parts below. See [MLU-Explain > The Importance of Data Splitting](https://mlu-explain.github.io/train-test-validation/) for a good interactive explanation/visualisation.
  * Training data (`train`): train/teach the model via inputs and expected/correct outputs.
    * Output is in the form of model weights (logits?) 
  * Validation (`validation`): validation (and tweaking?) of the chosen model/hyper parameters/hidden units. Iterative step?
    * Output is in the form of accurracy/loss.
    * Sometimes (but not very often?) this data set is omitted.
  * Evaluation/test (`test`): evaluation of the model against unknown data. Used after all training and validation has been performed.
    * Output is in the form of accurracy/precision/recall.
  * Examples: 
    * [General Language Understanding Evaluation (GLUE)](https://huggingface.co/datasets/nyu-mll/glue) benchmark:
      * [Corpus of Linguistic Acceptability (cola)](https://huggingface.co/datasets/nyu-mll/glue/viewer/cola)
      * [Multi-Genre Natural Language Inference (mnli)](https://huggingface.co/datasets/nyu-mll/glue/viewer/mnli)
      * [Microsoft Research Paraphrase Corpus (mrpc)](https://huggingface.co/datasets/nyu-mll/glue/viewer/mrpc/train)
      * [Stanford Sentiment Treebank (sst2)](https://huggingface.co/datasets/nyu-mll/glue/viewer/sst2)
      * ...
    * Other:
      * ...
