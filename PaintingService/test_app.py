import pytest
import jwt
import io
import numpy as np
import cv2
from app import app, JWT_SECRET


@pytest.fixture
def client():
    app.config['TESTING'] = True
    with app.test_client() as client:
        yield client


@pytest.fixture
def valid_token():
    return jwt.encode({}, JWT_SECRET, algorithm="HS256")


def test_initialize_without_token(client):
    """Sprawdza czy API odrzuci brak tokena (401)"""
    print("\n>>> [START] Test: Inicjalizacja bez tokena")
    response = client.post('/initialize')

    print(f">>> [LOG] Otrzymano status: {response.status_code}")
    assert response.status_code == 401
    print(">>> [SUCCESS] Dostęp zablokowany poprawnie.")


def test_initialize_with_valid_image(client, valid_token, mocker):
    """Testuje inicjalizację z poprawnym obrazem i zamockowanym AI"""
    print("\n>>> [START] Test: Inicjalizacja z obrazem (Mock AI)")

    # KROK 1: Mockowanie modelu
    print(">>> [KROK 1] Mockowanie ai_painter...")
    mocker.patch('app.ai_painter', autospec=True)

    # KROK 2: Przygotowanie danych
    print(">>> [KROK 2] Generowanie sztucznego obrazu 100x100...")
    img = np.zeros((100, 100, 3), dtype=np.uint8)
    _, img_encoded = cv2.imencode('.jpg', img)
    img_bytes = io.BytesIO(img_encoded.tobytes())

    data = {'image': (img_bytes, 'test.jpg')}
    headers = {'Authorization': f'Bearer {valid_token}'}

    # KROK 3: Request
    print(">>> [KROK 3] Wysyłanie POST /initialize...")
    response = client.post('/initialize', data=data, headers=headers, content_type='multipart/form-data')

    # KROK 4: Asercje
    print(f">>> [KROK 4] Analiza odpowiedzi: {response.json}")
    assert response.status_code == 200
    assert response.json['w'] == 100
    assert response.json['h'] == 100
    assert response.json['status'] == "Zainicjalizowano"
    print(">>> [SUCCESS] Obraz poprawnie przetworzony.")


def test_paint_endpoint(client, valid_token, mocker):
    """Testuje endpoint /paint - integracja wewnętrzna"""
    print("\n>>> [START] Test: Endpoint /paint")

    # KROK 1: Mockowanie segmentacji
    print(">>> [KROK 1] Przygotowanie Mocka maski...")
    mock_painter = mocker.patch('app.ai_painter')
    mock_mask = np.zeros((100, 100), dtype=bool)
    mock_mask[40:60, 40:60] = True  # Tworzymy "wyspę" na środku
    mock_painter.segment.return_value = mock_mask

    # KROK 2: Ustawienie stanu aplikacji
    import app as main_app
    print(">>> [KROK 2] Wstrzykiwanie obrazu do stanu globalnego aplikacji...")
    main_app.current_img = np.zeros((100, 100, 3), dtype=np.uint8)

    # KROK 3: Request
    payload = {
        "x": 50,
        "y": 50,
        "color": [255, 0, 0, 128]
    }
    headers = {'Authorization': f'Bearer {valid_token}'}

    print(f">>> [KROK 3] Wysyłanie żądania malowania w punkcie ({payload['x']}, {payload['y']})...")
    response = client.post('/paint', json=payload, headers=headers)

    # KROK 4: Sprawdzanie wyniku
    print(f">>> [KROK 4] Status odpowiedzi: {response.status_code}")
    assert response.status_code == 200

    res_data = response.json
    assert "mask_base64" in res_data
    assert res_data['status'] == "success"

    mask_len = len(res_data['mask_base64'])
    print(f">>> [LOG] Otrzymano maskę Base64 o długości: {mask_len} znaków")
    print(">>> [SUCCESS] Proces malowania zakończony pomyślnie.")