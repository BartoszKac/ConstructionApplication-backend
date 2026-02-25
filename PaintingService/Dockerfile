FROM python:3.10-slim

# Ustawiamy zmienne środowiskowe, aby Python nie tworzył plików .pyc i logował na bieżąco
ENV PYTHONDONTWRITEBYTECODE=1
ENV PYTHONUNBUFFERED=1

WORKDIR /app

# Instalacja zależności systemowych
# Dodajemy curl, żeby móc sprawdzać stan serwisu wewnątrz kontenera (healthcheck)
RUN apt-get update && apt-get install -y \
    git \
    libgl1 \
    libglib2.0-0 \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Kopiujemy requirements najpierw (wykorzystanie cache'u warstw Dockera)
COPY requirements.txt .

# Instalacja zależności. Jeśli używasz tylko CPU, warto dodać flagi oszczędzające miejsce
RUN pip install --no-cache-dir -r requirements.txt

# Kopiujemy resztę aplikacji
COPY . .

# Ważne: upewnij się, że port w aplikacji Python (np. we Flasku/FastAPI) to 8087
EXPOSE 8087

# Uruchomienie aplikacji
CMD ["python", "app.py"]