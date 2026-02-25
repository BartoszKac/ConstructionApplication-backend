FROM python:3.10-slim

WORKDIR /app

RUN apt-get update && apt-get install -y \
    build-essential \
    && rm -rf /var/lib/apt/lists/*

COPY requirements.txt .

RUN pip install --no-cache-dir -r requirements.txt && \
    python -m spacy download en_core_web_sm

# Kopiuje wszystko (w tym folder src) do /app
COPY . .

EXPOSE 5000

# Wskazujemy ścieżkę relatywną do WORKDIR (/app)
CMD ["python", "-m", "src.controler.Serwer"]