from flask import Flask, request, jsonify
import cv2
import numpy as np
import base64
from py_eureka_client import eureka_client

app = Flask(__name__)


# --- KONFIGURACJA EUREKA DLA KABLA USB ---
def init_eureka():
    # Na kablu USB używamy localhosta
    current_ip = '127.0.0.1'

    eureka_client.init(
        eureka_server="http://localhost:8761/eureka",
        app_name="visual-ai-service",
        instance_port=8087,
        instance_host=current_ip,
        should_register=True,
        should_discover=True,
        renewal_interval_in_secs=30,
        duration_in_secs=90
    )
    print(f"[*] Serwis AI zarejestrowany w Eurece na localhost (Kabel USB)")


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
        return jsonify({"status": "Zainicjalizowano", "w": w, "h": h})
    return jsonify({"error": "AI nie gotowe"}), 500


@app.route('/paint', methods=['POST'])
def paint():
    if current_img is None:
        return jsonify({"error": "Brak obrazu"}), 400
    data = request.json
    x, y = data.get('x'), data.get('y')
    # Pobieramy kolor przesłany z frontu (opcjonalnie, ale lepiej kolorować na froncie)
    req_color = data.get('color', [0, 0, 150, 140])

    if ai_painter:
        h, w = current_img.shape[:2]
        mask = ai_painter.segment([y, x], h, w)
        if mask is not None:
            # Tworzymy obraz RGBA
            rgba_mask = np.zeros((h, w, 4), dtype=np.uint8)
            mask_bool = mask.astype(bool)

            # Używamy koloru przesłanego w body requestu zamiast stałego [0,0,150,140]
            # req_color to [B, G, R, A]
            rgba_mask[mask_bool] = req_color

            _, buffer = cv2.imencode('.png', rgba_mask)
            mask_base64 = base64.b64encode(buffer).decode('utf-8')
            return jsonify({"status": "success", "mask_base64": mask_base64})
    return jsonify({"error": "Błąd maski"}), 400


if __name__ == '__main__':
    init_eureka()
    app.run(host='0.0.0.0', port=8087, threaded=True)