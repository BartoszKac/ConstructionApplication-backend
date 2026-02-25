from flask import Flask, jsonify, request
from flask_cors import CORS
import py_eureka_client.eureka_client as eureka_client
from engine import CastoramaTilesEngine
import jwt  # Biblioteka do obsługi tokenów
import os

app = Flask(__name__)
CORS(app)
scraper = CastoramaTilesEngine()

# --- TWOJE TAJNE ZABEZPIECZENIE (KLUCZ NA SZTYWNO) ---
JWT_SECRET = "replace_this_with_a_long_random_secret_key"

EUREKA_SERVER = "http://localhost:8761/eureka"
SERVICE_PORT = 8089

def verify_token(req):
    """Funkcja sprawdzająca bilet wstępu (token JWT)"""
    auth_header = req.headers.get('Authorization')
    if not auth_header:
        return False
    try:
        # Wyciągamy token z formatu "Bearer <token>"
        token = auth_header.split(" ")[1]
        jwt.decode(token, JWT_SECRET, algorithms=["HS256"])
        return True
    except Exception as e:
        print(f"❌ Błąd weryfikacji tokena: {e}")
        return False

def init_eureka():
    try:
        eureka_client.init(
            eureka_server=EUREKA_SERVER,
            app_name="TILE-SERVICE",
            instance_port=SERVICE_PORT,
            instance_host="localhost",
            should_register=True,
            should_discover=True
        )
        print("✅ Registered in Eureka")
    except Exception as e:
        print(f"❌ Eureka connection failed: {e}")

@app.route('/tiles', methods=['POST'])
def get_tiles():
    # --- KROK 1: SPRAWDZANIE AUTORYZACJI ---
    if not verify_token(request):
        print("🛑 Próba nieautoryzowanego dostępu do scrapingu!")
        return jsonify({"status": "error", "message": "Unauthorized - brak dostępu"}), 401

    # --- KROK 2: LOGIKA BIZNESOWA ---
    data = request.json
    if not data:
        return jsonify({"status": "error", "message": "No data provided"}), 400

    required_size = data.get('size')
    raw_area = data.get('area', 0)

    try:
        required_area = float(str(raw_area).replace(',', '.'))
    except:
        required_area = 0.0

    print(f"✅ Autoryzacja poprawna. Request: Rozmiar={required_size}, Metraż={required_area}m2")
    products = scraper.fetch_and_filter(size_filter=required_size, required_area=required_area)

    return jsonify({
        "status": "success",
        "count": len(products),
        "data": products
    })

if __name__ == '__main__':
    init_eureka()
    app.run(host='0.0.0.0', debug=True, port=SERVICE_PORT)