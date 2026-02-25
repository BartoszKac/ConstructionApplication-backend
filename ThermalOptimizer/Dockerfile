FROM python:3.10-slim

WORKDIR /app

# Instalacja podstawowych narzędzi (opcjonalnie, dla stabilności bibliotek)
RUN apt-get update && apt-get install -y \
    build-essential \
    && rm -rf /var/lib/apt/lists/*

# Kopiujemy wymagania
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Kopiujemy kod źródłowy (app.py oraz engine.py)
COPY . .

# Port z Twojego kodu (8089)
EXPOSE 8089

# Uruchomienie aplikacji
CMD ["python", "app.py"]