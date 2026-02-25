import pytest
import requests_mock
from src.service.Webscraping import WebScrapingService


@pytest.fixture
def scraper():
    """Tworzy instancję serwisu przed każdym testem."""
    return WebScrapingService()


# --- TEST 1: Parsowanie poprawnego HTML z JSON-LD ---
def test_parse_pages_success(scraper):
    print("\n>>> [START] Test 1: Parsowanie poprawnego HTML (JSON-LD)")

    # KROK 1: Przygotowanie danych wejściowych
    print(">>> [KROK 1] Przygotowanie sztucznego HTML z tagiem script JSON-LD...")
    mock_html = """
    <html>
        <script type="application/ld+json">
        {
            "itemListElement": [
                {
                    "name": "Farba Akrylowa Biała",
                    "url": "http://sklep.pl/farba1",
                    "image": "img.jpg",
                    "offers": { "price": "129.99", "priceCurrency": "PLN" }
                }
            ]
        }
        </script>
    </html>
    """
    scraper.html_pages = [mock_html]

    # KROK 2: Uruchomienie parsera
    print(">>> [KROK 2] Wywołanie scraper.parse_pages()...")
    results = scraper.parse_pages()

    # KROK 3: Asercje
    print(f">>> [KROK 3] Analiza wyników: Znaleziono {len(results)} przedmiotów.")
    assert len(results) == 1
    assert results[0]["title"] == "Farba Akrylowa Biała"
    assert results[0]["price_value"] == "129.99"
    assert results[0]["currency"] == "PLN"

    print(">>> [SUCCESS] Dane zostały poprawnie wyciągnięte z JSON-LD.")


# --- TEST 2: Odporność na uszkodzony JSON ---
def test_parse_pages_invalid_json(scraper):
    print("\n>>> [START] Test 2: Odporność na uszkodzony JSON")

    # KROK 1: Wstrzyknięcie błędnego skryptu
    print(">>> [KROK 1] Wstrzykiwanie HTML z uszkodzonym formatem JSON...")
    mock_html = '<html><script type="application/ld+json">{ uszkodzony_json: error }</script></html>'
    scraper.html_pages = [mock_html]

    # KROK 2: Próba parsowania
    print(">>> [KROK 2] Wywołanie parsera (oczekujemy braku wyjątków)...")
    results = scraper.parse_pages()

    # KROK 3: Sprawdzenie bezpieczeństwa
    assert results == []
    print(">>> [SUCCESS] Parser bezpiecznie obsłużył błąd składni JSON.")


# --- TEST 3: Wykrywanie blokady strony (Cloudflare/Captcha) ---
def test_check_page_blocking(scraper, capsys):
    print("\n>>> [START] Test 3: Wykrywanie blokad (Cloudflare/403)")
    url = "http://test-sklep.pl"

    with requests_mock.Mocker() as m:
        # KROK 1: Symulacja błędu 403
        print(f">>> [KROK 1] Mockowanie odpowiedzi 403 (Access Denied) dla {url}...")
        m.get(requests_mock.ANY, text="Access Denied by Cloudflare", status_code=403)

        # KROK 2: Wywołanie funkcji sprawdzającej
        print(">>> [KROK 2] Sprawdzanie strony przez scraper.check_page()...")
        scraper.check_page(url)

        # KROK 3: Przechwycenie logów konsoli
        captured = capsys.readouterr()
        log_output = captured.out

        print(f">>> [LOG] Przechwycono komunikat: {log_output.strip()}")

        # Sprawdzamy czy Twój kod wypisał odpowiedni komunikat błędu
        assert any(msg in log_output for msg in ["❌ Strona blokuje requesty", "❌ Wykryto zabezpieczenia"])

    print(">>> [SUCCESS] Blokada strony została poprawnie wykryta i zalogowana.")