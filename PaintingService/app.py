from flask import Flask, request, jsonify
import cv2
import numpy as np
import base64
from py_eureka_client import eureka_client

app = Flask(__name__)

# --- KONFIGURACJA EUREKA ---
# To sprawi, że Gateway i inne serwisy Cię "zobaczą"
def init_eureka():
    eureka_client.init(
        eureka_server="http://localhost:8761/eureka",
        app_name="visual-ai-service",
        instance_port=8087,
        instance_host="192.168.1.37",  # Twoje stałe IP WiFi
        should_register=True,
        should_discover=True,
        renewal_interval_in_secs=30,
        duration_in_secs=90
    )
    print("[*] Sukces: Serwis AI zarejestrowany w Eurece pod IP 192.168.1.37")

# --- ŁADOWANIE MODELU AI ---
try:
    from Aialgorythm import SAM_Painter
    ai_painter = SAM_Painter()
    print("[*] Model SAM gotowy.")
except Exception as e:
    print(f"[!] Błąd modelu: {e}")
    ai_painter = None

current_img = None

@app.route('/initialize', methods=['POST'])
def initialize():
    global current_img
    if 'image' not in request.files:
        return jsonify({"error": "Brak pliku"}), 400

    file_bytes = request.files['image'].read()
    img = cv2.imdecode(np.frombuffer(file_bytes, np.uint8), cv2.IMREAD_COLOR)

    if img is None:
        return jsonify({"error": "Błąd obrazu"}), 400

    current_img = img
    h, w = img.shape[:2]

    if ai_painter:
        ai_painter.prepare_image(current_img)
        return jsonify({
            "status": "Zainicjalizowano",
            "w": w,
            "h": h
        })
    return jsonify({"error": "AI nie gotowe"}), 500

@app.route('/paint', methods=['POST'])
def paint():
    if current_img is None:
        return jsonify({"error": "Brak obrazu"}), 400

    data = request.json
    x, y = data.get('x'), data.get('y')

    if ai_painter:
        h, w = current_img.shape[:2]
        # SAM oczekuje [y, x]
        mask = ai_painter.segment([y, x], h, w)

        if mask is not None:
            rgba_mask = np.zeros((h, w, 4), dtype=np.uint8)
            mask_bool = mask.astype(bool)
            # Kolor BGR: Czerwony [0, 0, 150], Alpha: 140
            rgba_mask[mask_bool] = [0, 0, 150, 140]

            _, buffer = cv2.imencode('.png', rgba_mask)
            mask_base64 = base64.b64encode(buffer).decode('utf-8')

            return jsonify({"status": "success", "mask_base64": mask_base64})

    return jsonify({"error": "Błąd maski"}), 400

if __name__ == '__main__':
    # Uruchamiamy Eurekę przed startem Flaska
    init_eureka()
    # Host 0.0.0.0 pozwala na dostęp z sieci zewnętrznej (telefonu)
    app.run(host='0.0.0.0', port=8087, threaded=True)