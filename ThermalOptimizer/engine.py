import requests
from bs4 import BeautifulSoup
import json
import math
import re


class CastoramaTilesEngine:
    def __init__(self):
        self.session = requests.Session()
        self.headers = {
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36',
            'Accept-Language': 'pl-PL,pl;q=0.9',
            'Referer': 'https://www.castorama.pl/'
        }

    def _get_single_tile_area(self, size_str):
        """Wylicza m2 jednej płytki na podstawie stringa np. '60x60'"""
        try:
            # Szukamy cyfr, obsługując też formaty typu 29,8
            dims = re.findall(r'\d+', size_str)
            if len(dims) >= 2:
                width = float(dims[0]) / 100
                height = float(dims[1]) / 100
                return width * height
        except:
            return None
        return None

    def fetch_and_filter(self, size_filter=None, required_area=0.0):
        search_term = f"plytki {size_filter}" if size_filter else "plytki podlogowe"
        url = f"https://www.castorama.pl/search?term={search_term.replace(' ', '%20')}"

        # Tworzymy Regex, który zignoruje spacje: "60 x 60" == "60x60"
        size_regex = None
        if size_filter:
            dims = re.findall(r'\d+', size_filter)
            if len(dims) >= 2:
                # Szuka: liczba1 -> dowolne znaki niebędące cyframi -> liczba2
                pattern = rf"{dims[0]}\s*[xX, ]+\s*{dims[1]}"
                size_regex = re.compile(pattern)

        try:
            response = self.session.get(url, headers=self.headers, timeout=10)
            soup = BeautifulSoup(response.content, "html.parser")
            all_products = []

            single_tile_m2 = self._get_single_tile_area(size_filter) if size_filter else None

            scripts = soup.find_all("script", type="application/ld+json")
            for script in scripts:
                if not script.string: continue
                try:
                    data = json.loads(script.string)
                    # Sprawdzamy czy to lista produktów
                    if data.get("@type") == "ItemList":
                        items = data.get("itemListElement", [])
                        for item in items:
                            name = item.get("name", "")
                            if not name: continue

                            # Logika dopasowania Regex
                            is_match = False
                            if not size_regex:
                                is_match = True
                            elif size_regex.search(name):
                                is_match = True

                            if not is_match:
                                continue

                            print(f"   ✅ DOPASOWANO: {name[:50]}...")

                            # Pobieranie ceny
                            price = 0.0
                            offers = item.get("offers", {})
                            if isinstance(offers, dict):
                                # Castorama używa różnych pól dla ceny
                                p_val = offers.get("price") or offers.get("priceAmount")
                                if p_val:
                                    price = float(str(p_val).replace(',', '.'))

                            if price > 0:
                                total_cost = round(price * required_area, 2)
                                pieces_needed = 0
                                if single_tile_m2:
                                    pieces_needed = math.ceil(required_area / single_tile_m2)

                                # Dodaj to w engine.py, żeby pobierać URL zdjęcia
                                all_products.append({
                                    "title": name,
                                    "price_per_m2": price,
                                    "total_project_cost": total_cost,
                                    "pieces_needed": pieces_needed,
                                    "url": item.get("url"),
                                    "image": item.get("image")  # Castorama podaje to w JSON-LD
                                })
                except Exception as e:
                    print(f"⚠️ Błąd wewnątrz pętli JSON: {e}")
                    continue

            # Sortowanie od najtańszej oferty
            return sorted(all_products, key=lambda x: x['total_project_cost'])

        except Exception as e:
            print(f"❌ Scraper Error: {e}")
            return []