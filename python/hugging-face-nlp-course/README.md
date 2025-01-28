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

It seems that various sources, e.g. ChatGPT and Wolfram Alpha do not provide a clear distinction between the concepts of "tasks" and "paradigms", so I decided to categorise tasks by paradigm :)

Many tasks probably involve multiple paradigms, but are listed below under only one paradigm for simplicity.

* [Tasks](https://huggingface.co/tasks) & [paradigms](https://www.wolfram.com/language/introduction-machine-learning/machine-learning-paradigms/): the task to be performed / problem to be solved:
    * **Supervised**: predict, from a set of input (feature)-output pairs (examples), output values (labels) for new unseen inputs.
      * **Classification**: predict to which class an example belongs (which label does it have, e.g. 0: "not equivalent", 1: "equivalent"?). Examples:
         * Natural Language Processing (NLP), e.g. text generation/classification/summarization, sentiment analysis, translation, text-to-speech/speech-to-text, etc.
         * Image Generation (text-to-image) and image recognition (image-to-text)
         * Disease Diagnosis/Drug Design
      * **Regression**: predict continuous, numeric values, e.g. house prices, weather forecasts, GDP growth, etc.
    * Self-Supervised Learning: TODO: add description
    * Semi-Supervised Learning: train a model on labeled data, predict the missing labels, then train on the full dataset, predict the missing labels again, and so on.
    * **Unsupervised**: find patterns in unlabelled (unstructured) data.
      * Clustering: group similar items together without predefined labels, e.g. anomaly detection
      * Dimensionality Reduction: reduce the number of input features while preserving essential information
      * Imputation (fill in the gaps)
      * Generative modeling, e.g. Generative Adversarial Networks (GANs).
      * Computer Vision: process visual data, e.g. for self-driving cars or medical imaging
   * **Reinforcement Learning**: model interacts with an environment and over time learns a policy (via rewards) to improve the performance of some function.
     * Examples: Chess, Go, StarCraft, robot movement
   * **Transfer Learning**: transfer "knowledge" (through the layers of a neural network, aka Deep Learning?) from a "broad"/generic task trained on large (unstructured) data set to a more specific downstream task trained on more specific (structured) data sets.
     * The **pre-training** part of models like BERT and GPT is typically unsupervised working on unlabelled data, whereas the fine-tuning (transfer learning) part is typically supervised, working on labeled data.
   * **Reasoning**: infer/deduce/derive conclusions beyond surface-level pattern recognition
     * Examples:
       * Natural Language Inference (NLI), e.g. is a hypothesis entailed/contradicted by a premise.
       * Multi-Hop Question Answering (QA): combine information from multiple sources and apply reasoning across multiple steps
       * Theorem Proving: prove mathematical theorems or verify logical statements based on a set of axioms and rules
  * [Hugging Face Task / pipeline Type](https://huggingface.co/docs/hub/en/models-tasks): "Tasks, or pipeline types, describe the “shape” of each model’s API (inputs and outputs) and are used to determine which Inference API and widget we want to display for any given model."
* [Models](https://learn.microsoft.com/en-us/windows/ai/windows-ml/what-is-a-machine-learning-model): a neural network that will perform a task.
  * Model "alias terms":
    * Classification: classifier
    * Regression: regressor
    * Clustering: K-Means, Gaussian Mixture Model
    * Dimensionality Reduction: Embedding model or Dimensionality Reducer (e.g., PCA, t-SNE)
    * Reinforcement Learning: Agent or Policy Model (e.g., Q-Learning Agent, Policy Network)
  * [Transformer](https://huggingface.co/docs/transformers/en/index): an ML model based on the [Transformer](https://research.google/blog/transformer-a-novel-neural-network-architecture-for-language-understanding/) architecture (from 2017 paper "Attention Is All You Need" by Vaswani et al.) that can be applied to multiple modalities. Most modern models are Transformer-based.
    * Large Language Model (LLM): a model specifically designed and trained for natural language (text) processing tasks. Examples: [BERT (Google)](https://huggingface.co/docs/transformers/en/model_doc/bert), [DistilBERT (Hugging Face)](https://huggingface.co/docs/transformers/en/model_doc/distilbert) and [GPT (OpenAI)](https://huggingface.co/docs/transformers/en/model_doc/openai-gpt)
    * Text-to-image examples: DALL-E (OpenAI), Midjourney, Stable Diffusion (Stability AI), Google DeepMind, etc.
    * Drug Design example: AlphaFold: inference of the structure of a protein from its amino acid sequence
* [Datasets](https://huggingface.co/docs/datasets/en/index): data divided into the parts below. See [MLU-Explain > The Importance of Data Splitting](https://mlu-explain.github.io/train-test-validation/) for a good interactive explanation/visualisation.
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
