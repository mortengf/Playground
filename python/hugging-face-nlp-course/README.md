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
This section was initially created from the [Hugging Face NLP Course](https://huggingface.co/learn/nlp-course/), but has since been expanded with information from various other sources to encompass Machine Learning terminology more broadly.

* [Task](https://huggingface.co/tasks) (generic): the task to be performed / problem to be solved.
  * Types:
     * Classification: predict to which class an example belongs (which label does it have, e.g. 0: "not equivalent", 1: "equivalent"?)
       * Examples:
         * Natural Language Processing (NLP), e.g. text classification/summarization, sentiment analysis, etc.
         * Image recognition (image-to-text) and image generation (text-to-image)
         * Disease diagnosis
     * Regression: predict a numeric value, e.g. predicting house prices, weather forecasts, GDP growth, etc.
     * Reasoning: infer/deduce/derive conclusions beyond surface-level pattern recognition
       * Examples:
         * Natural Language Inference (NLI), e.g. is a hypothesis entailed/contradicted by a premise.
         * Multi-Hop Question Answering (QA): combine information from multiple sources and apply reasoning across multiple steps
         * Theorem Proving: prove mathematical theorems or verify logical statements based on a set of axioms and rules
  * [Hugging Face Task / pipeline Type](https://huggingface.co/docs/hub/en/models-tasks): "Tasks, or pipeline types, describe the “shape” of each model’s API (inputs and outputs) and are used to determine which Inference API and widget we want to display for any given model."
* [Model](https://learn.microsoft.com/en-us/windows/ai/windows-ml/what-is-a-machine-learning-model): a neural network that will perform a task.
  * For classification, "model" ~ "classifier"
  * [Transformer](https://huggingface.co/docs/transformers/en/index): an ML model based on the [Transformer](https://research.google/blog/transformer-a-novel-neural-network-architecture-for-language-understanding/) architecture (from 2017 paper "Attention Is All You Need" by Vaswani et al.) that can be applied to multiple modalities. Most modern models are Transformer-based.
    * Large Language Model (LLM): a model specifically designed and trained for natural language (text) processing tasks. Examples: [BERT (Google)](https://huggingface.co/docs/transformers/en/model_doc/bert), [DistilBERT (Hugging Face)](https://huggingface.co/docs/transformers/en/model_doc/distilbert) and [GPT (OpenAI)](https://huggingface.co/docs/transformers/en/model_doc/openai-gpt)
    * Text-to-image examples: DALL-E, Stable Diffusion, Imagen, etc.
* [Paradigm/approach](https://www.wolfram.com/language/introduction-machine-learning/machine-learning-paradigms/):
    * Supervised: model learns (a predictive model is trained) from a set of input (feature)-output pairs (examples) to predict an output value (label) for a new, unseen input.
      * Examples: see Classification and Regression above
    * Unsupervised: find patterns in unstructured/unlabelled data 
      * Examples: clustering (e.g. anomaly detection), dimensionality reduction, imputation (fill in the gaps) or generative modeling (e.g. Generative Adversarial Networks (GANs)).
    * Reinforcement (interactive): model interacts with an environment and over time learns a policy (via rewards) to improve the performance of some function.
      * Examples: Chess, Go, StarCraft, robot navigation
    * Transfer learning: transfer "knowledge" (through the layers of one or more neural networks? Deep Learning?) from a "broad"/generic task trained on large (unstructured) data set to a more specific downstream task trained on more specific (structured) data sets.
      * The pre-training part of models like BERT and GPT is typically unsupervised working on unlabelled data, whereas the fine-tuning (transfer learning) part is typically supervised, working on labeled data.
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
