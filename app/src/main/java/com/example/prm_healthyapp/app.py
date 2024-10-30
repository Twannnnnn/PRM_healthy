from flask import Flask, request, jsonify
from transformers import AutoTokenizer, AutoModelForSequenceClassification
import torch

app = Flask(__name__)

# Tải mô hình và tokenizer
model_name = "textattack/bert-base-uncased-imdb"
tokenizer = AutoTokenizer.from_pretrained(model_name)
model = AutoModelForSequenceClassification.from_pretrained(model_name)

@app.route('/predict', methods=['POST'])
def predict():
    data = request.json
    text = data['text']

    # Tokenize văn bản
    inputs = tokenizer(text, return_tensors="pt")

    # Dự đoán
    with torch.no_grad():
        outputs = model(**inputs)
        predictions = torch.softmax(outputs.logits, dim=1).tolist()

    return jsonify(predictions)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)