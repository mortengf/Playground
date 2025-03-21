# Machine Learning (ML) Terminology
This section was initially created from the [Hugging Face NLP Course](https://huggingface.co/learn/nlp-course/), but has since been expanded with information from various other sources to encompass Machine Learning terminology more broadly.

## Tasks & Paradigms
It seems that various sources, e.g. ChatGPT and Wolfram Alpha do not provide a clear distinction between the concepts of "tasks" and "paradigms", so I decided to categorise some popular tasks by paradigm :)

Many tasks probably involve multiple paradigms, but are listed below under only one paradigm for simplicity.

### Definitions
* [Task](https://huggingface.co/tasks): the work to be performed / problem to be solved.
  * [Hugging Face Task / pipeline Type](https://huggingface.co/docs/hub/en/models-tasks): "Tasks, or pipeline types, describe the “shape” of each model’s API (inputs and outputs) and are used to determine which Inference API and widget we want to display for any given model."
* [Paradigm](https://www.wolfram.com/language/introduction-machine-learning/machine-learning-paradigms/): the approach by which the work is done.

### Listing Tasks by Paradigm

#### Paradigm: Supervised 
Predict, from a set of input (feature)-output pairs (examples), output values (labels) for new unseen inputs.

The term "supervised" comes from the fact that the labels (correct outputs) are "guiding" aka "supervising" the model (during fine-tuning).

The two most common supervised tasks are:
* Classification
* Regression

##### Task: Classification
Predict, with some probability, to which class an example belongs (which label does it have, e.g. 0: "not equivalent", 1: "equivalent"?).

Examples:
 * Natural Language Processing (NLP), e.g. text generation/classification/summarization, sentiment analysis, translation, text-to-speech/speech-to-text, etc.
 * Image Generation (text-to-image) and image recognition (image-to-text)
 * Healthcare, e.g. disease diagnosis and medicine design

##### Task: Regression
Predict continuous, numeric values.

Examples: house prices, weather forecasts, GDP growth, etc.

#### Paradigm: Self-Supervised Learning
Obtain input-output pairs from the data itself.

TODO: add examples of tasks.

#### Paradigm: Semi-Supervised Learning
Train a model on labeled data, predict the missing labels, then train on the full dataset, predict the missing labels again, and so on.

TODO: add examples of tasks.

#### Paradigm: Unsupervised
Find patterns in unlabelled (unstructured) data.

Example Tasks:
* Clustering: group similar items together without predefined labels, e.g. anomaly detection
* Dimensionality Reduction: reduce the number of input features while preserving essential information
* Imputation (fill in the gaps)
* Generative modeling, e.g. Generative Adversarial Networks (GANs).
* Computer Vision: process visual data, e.g. for self-driving cars or medical imaging

#### Paradigm: Reinforcement Learning
Model interacts with an environment and over time learns a policy (via rewards) to improve the performance of some function.

Example tasks: Chess, Go, StarCraft, robot movement, etc.

#### Paradigm: Transfer Learning
Transfer "knowledge" (weights?) from a "broad"/generic task trained on large (unstructured) data set to a more specific downstream task trained on more specific (structured) data sets.

The **pre-training** part of models like BERT and GPT is typically unsupervised working on unlabelled data, whereas the fine-tuning (transfer learning) part is typically supervised, working on labeled data.

Example tasks: TODO: add examples

#### Paradigm: Reasoning
Infer/deduce/derive conclusions beyond surface-level pattern recognition

Example tasks:
* Natural Language Inference (NLI), e.g. is a hypothesis entailed/contradicted by a premise.
* Multi-Hop Question Answering (QA): combine information from multiple sources and apply reasoning across multiple steps
* Theorem Proving: prove mathematical theorems or verify logical statements based on a set of axioms and rules

## Neural Networks & Models

### Definitions
* [ML Model](https://en.wikipedia.org/wiki/Machine_learning#Models): a mathematical model that can be used to make predictions
* [Neural Network](https://en.wikipedia.org/wiki/Neural_network_(machine_learning)): an ML model inspired by the structure and function of biological neural networks in animal brains

#### Deep Learning
Deep Learning is a technique that can be applied within each learning paradigm. The term "deep" in "Deep Neural Network" (DNN) refers to the multiple/many layers in the neural network that are used for training.

Examples of DNNs are:

* [Transformer](https://huggingface.co/docs/transformers/en/index): an ML model based on the [Transformer](https://research.google/blog/transformer-a-novel-neural-network-architecture-for-language-understanding/) architecture (from 2017 paper "Attention Is All You Need" by Vaswani et al.) that can be applied to multiple modalities. Most modern models are Transformer-based.
* [Convolutional Neural Networks](https://en.wikipedia.org/wiki/Convolutional_neural_network) (CNNs)
* [Generative Adversarial Networks](https://en.wikipedia.org/wiki/Generative_adversarial_network) (GANs)

#### Model "alias terms"
* Classification: Classifier
* Regression: Regressor
* Generative Models: Generator
* Clustering: Clusterer
* Dimensionality Reduction: Feature Extractor or Dimensionality Reducer
* Reinforcement Learning: Agent or Policy Model

### Model Types

#### Large Language Models (LLMs)
A model specifically designed and trained for natural language (text) processing tasks. 

Examples:
* [BERT](https://huggingface.co/docs/transformers/en/model_doc/bert) (Google) & [RoBERTa](https://huggingface.co/docs/transformers/en/model_doc/roberta) (Meta)
  * Hugging Face offer small ("distilled") versions of many models, e.g. [DistilBERT](https://huggingface.co/docs/transformers/en/model_doc/distilbert).
* [GPT](https://huggingface.co/docs/transformers/en/model_doc/openai-gpt) (OpenAI) & [LLaMA](https://huggingface.co/docs/transformers/en/model_doc/llama) (Meta)
* [T5](https://huggingface.co/docs/transformers/en/model_doc/t5) (Google)
* [DeepSeek LLM](https://github.com/deepseek-ai/DeepSeek-LLM) (DeepSeek)

See more models under "Multi-Modal & Reasoning Models" below.

##### Specialized: Healthcare
Various models fine-tuned specifically for healthcare applications.

Examples:
* [AlphaFold](https://deepmind.google/technologies/alphafold/) (Google DeepMind): inference of the structure of a protein from its amino acid sequence for designing medicine.
* [Solo, Ensemble & Symphony](https://www.corti.ai/foundation-models) (Corti): models for healthcare-specific conversations and medical coding

##### Specialized: ...
TODO: add example

#### Text-to-Image & Image-to-Text Models
* Text-to-image: generate images from text input
* Image-to-text: perform image recognition to create textual descriptions of images

Examples:
* [DALL-E](https://openai.com/index/dall-e-2/) (OpenAI)
* [Midjourney](https://www.midjourney.com/home)
* [Stable Diffusion](https://stability.ai/stable-image) (Stability AI)

#### Text-to-Video Models
Generate videos from text input.

* [Sora](https://openai.com/index/sora/) (OpenAI)
* [Imagen](https://deepmind.google/technologies/imagen-3/) (Google DeepMind)
  * Can also handle images

#### Multi-Modal & Reasoning Models
* [GPT-4o](https://openai.com/index/hello-gpt-4o/) & [GPT-4o Mini](https://openai.com/index/gpt-4o-mini-advancing-cost-efficient-intelligence/) (OpenAI)
* [o1](https://openai.com/o1/) (OpenAI)
* [Gemini](https://deepmind.google/technologies/gemini/) (Google DeepMind)
* [Claude Haiku/Sonnet/Opus](https://www.anthropic.com/claude) (Anthropic)
* [Pixtral](https://mistral.ai/en/news/pixtral-large) (Mistral)
* [DeepSeek-R1](https://github.com/deepseek-ai/DeepSeek-R1) (DeepSeek)

## Data Sets

### Definitions
[Dataset](https://huggingface.co/docs/datasets/en/index): data used to train a model, divided into the parts below.

See [MLU-Explain > The Importance of Data Splitting](https://mlu-explain.github.io/train-test-validation/) for a good interactive explanation/visualisation.

### Data Set "Parts"

#### Training data (`train`)
Train/teach the model via inputs and expected/correct outputs.

Primary outputs:
* Loss: a scalar value representing how well the model performed on the training data
* Model weights: learned parameters (bias)

#### Validation (`validation`)
Validation and tweaking of the chosen model/hyper parameters/hidden units used during the modeling process to compare candidate models. Performed iteratively.

Output is in the form of accurracy/loss.

Sometimes (but not very often?) this data set is omitted.

#### Evaluation/test (`test`)
Evaluation of the model against unknown data. Used after all training and validation has been performed to obtain an unbiased estimation of the performance of the model.

Output is in the form of accurracy/precision/recall.

### Examples

* [General Language Understanding Evaluation (GLUE)](https://huggingface.co/datasets/nyu-mll/glue) benchmark:
  * [Corpus of Linguistic Acceptability (cola)](https://huggingface.co/datasets/nyu-mll/glue/viewer/cola)
  * [Multi-Genre Natural Language Inference (mnli)](https://huggingface.co/datasets/nyu-mll/glue/viewer/mnli)
  * [Microsoft Research Paraphrase Corpus (mrpc)](https://huggingface.co/datasets/nyu-mll/glue/viewer/mrpc/train)
  * [Stanford Sentiment Treebank (sst2)](https://huggingface.co/datasets/nyu-mll/glue/viewer/sst2)
  * ...
* Other:
  * ...

