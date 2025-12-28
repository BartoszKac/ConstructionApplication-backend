class MapperUrl:
    _URLS = {
        "farba": [
            "https://www.castorama.pl/search?term=farba%20wentrzna%20do%20scian"
        ]

    }

    @classmethod
    def map_url(cls, body_url: str) -> str:
        if body_url not in cls._URLS:
            raise ValueError(f"Nieznany typ: {body_url}")
        return cls._URLS[body_url]
