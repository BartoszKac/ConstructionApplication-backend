from flask import Flask, jsonify,request
from spacy.attrs import value

from src.service.Webscraping import WebScrapingService

app = Flask(__name__)


@app.route("/compute", methods=["POST"])
def compute():
    data = request.json
    value = data["value"]
    service = WebScrapingService()
    result = service.run(value)
    return jsonify(result)


if __name__ == "__main__":
    app.run(debug=True)
