import requests
from bs4 import BeautifulSoup
import json

from src.const.const import headers,pageOfSize
from src.model.MapperUrl import MapperUrl
from src.service.utills.ChangingUrl import ChangeUrl


class WebScrapingService:
    def __init__(self):
        self.html_pages = []

    # =========================
    # POBIERANIE STRON
    # =========================
    def check_page(self, url: str):
        for i in range(1,pageOfSize):
            try:
                print(f"\n===== Sprawdzam URL: {url} =====")

                response = requests.get(ChangeUrl.changeUrl(url,i), headers=headers, timeout=10)

                print(f"Status code: {response.status_code}")

                if response.status_code in (403, 429):
                    print("❌ Strona blokuje requesty (403/429)")
                    return

                text = response.text.lower()
                block_signals = [
                    "access denied",
                    "request blocked",
                    "captcha",
                    "cloudflare",
                    "error 1020",
                ]

                if any(signal in text for signal in block_signals):
                    print("❌ Wykryto zabezpieczenia (Cloudflare / Captcha)")
                    return

                self.html_pages.append(response.text)
                print("✅ HTML zapisany w pamięci")

            except Exception as e:
                print(f"❌ Błąd requesta: {e}")

    # =========================
    # PARSOWANIE HTML
    # =========================
    def parse_pages(self):
        if not self.html_pages:
            print("❌ Brak HTML do przetworzenia")
            return []

        results = []

        for html in self.html_pages:
            soup = BeautifulSoup(html, "html.parser")



            # ===== JSON-LD =====
            script_tag = soup.find("script", type="application/ld+json")
            if script_tag and script_tag.string:
                try:
                    data = json.loads(script_tag.string)

                    if "itemListElement" in data:
                        for item in data["itemListElement"]:
                            results.append({
                                "title": item.get("name"),
                                "url": item.get("url"),
                                "image_imageUrl": item.get("image"),
                                "price_value": item.get("offers", {}).get("price"),
                                "currency": item.get("offers", {}).get("priceCurrency"),
                            })
                except json.JSONDecodeError:
                    print("⚠️ Błąd dekodowania JSON-LD")

        return results

    # =========================
    # PUBLICZNY SERWIS
    # =========================
    def run(self,data):
        pages = MapperUrl.map_url(data)
        for page in pages:
            self.check_page(page)

        return self.parse_pages()

# titles = soup.find_all(class_="mb-xs font-bold")
# descriptions = soup.find_all(class_="block pb-2xs text-size-sm")
# prices = soup.find_all(class_="font-bold text-size-xl")
# images = soup.find_all(
#     class_="absolute inset-0 h-auto max-h-full max-w-full md:m-auto"
# )

# max_len = max(
#     len(titles),
#     len(descriptions),
#     len(prices),
#     len(images),
# )
#
# for i in range(max_len):
#     product = {
#         "title": titles[i].get_text(strip=True) if i < len(titles) else None,
#         "description": descriptions[i].get_text(strip=True) if i < len(descriptions) else None,
#         "price": prices[i].get_text(strip=True) if i < len(prices) else None,
#         "image": str(images[i]) if i < len(images) else None,
#     }
#     results.append(product)