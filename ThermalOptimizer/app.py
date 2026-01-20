from flask import Flask, jsonify, request
from flask_cors import CORS
import py_eureka_client.eureka_client as eureka_client
from engine import CastoramaTilesEngine

app = Flask(__name__)
CORS(app)
scraper = CastoramaTilesEngine()

EUREKA_SERVER = "http://localhost:8761/eureka"
SERVICE_PORT = 8089

def init_eureka():
    try:
        eureka_client.init(
            eureka_server=EUREKA_SERVER,
            app_name="TILE-SERVICE",
            instance_port=SERVICE_PORT,
            instance_host="localhost"
        )
        print("✅ Registered in Eureka")
    except Exception as e:
        print(f"❌ Eureka connection failed: {e}")

@app.route('/tiles', methods=['POST'])
def get_tiles():
    data = request.json
    if not data:
        return jsonify({"status": "error", "message": "No data provided"}), 400

    required_size = data.get('size')
    raw_area = data.get('area', 0)

    try:
        required_area = float(str(raw_area).replace(',', '.'))
    except:
        required_area = 0.0

    print(f"📊 Request: Rozmiar={required_size}, Metraż={required_area}m2")
    products = scraper.fetch_and_filter(size_filter=required_size, required_area=required_area)

    return jsonify({
        "status": "success",
        "count": len(products),
        "data": products
    })

if __name__ == '__main__':
    init_eureka()
    app.run(host='0.0.0.0', debug=True, port=SERVICE_PORT)