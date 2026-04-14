ConstructionApplication — Backend

A microservices-based backend for a construction assistant app that calculates paint/tile requirements, searches eBay for products, scrapes Polish hardware stores, and uses AI (SAM model) for room segmentation — all secured with JWT and orchestrated via Docker.


🛠️ Tech Stack
Java Services (Spring Boot 3.x / 4.x)

Java 17 / 21, Spring Boot, Spring Cloud Netflix (Eureka), Spring Security, Spring Data JPA
JWT authentication (com.auth0:java-jwt), RabbitMQ (AMQP), PostgreSQL, Lombok, Maven

Python Services (Flask)

Python 3.10, Flask, OpenCV, Segment Anything Model (SAM / vit_b), PyTorch (CPU)
BeautifulSoup4, spaCy, py-eureka-client, PyJWT

Infrastructure

 PostgreSQL 15, Spring Cloud Gateway (WebFlux)


🏗️ Architecture — Services Overview
Client
  └─► Gateway :8085  (Spring Cloud Gateway)
        ├─► MainService     :8082  (Java  — core logic, eBay API, auth)
        ├─► PaintingService :8087  (Python — SAM AI segmentation)
        └─► ThermalOptimizer:8089  (Python — Castorama tile scraping)

EurekaServer :8761   (service discovery)
PostgreSQL           (user data)
RabbitMQ   :5672     (message broker)
WebScrapingService :5000  (Python — Castorama paint scraping)
ServicePortLanguageResponsibilityEurekaSerwer8761JavaService discoveryGateway8085JavaAPI routingMainService8082JavaAuth, eBay API, paint calculatorPaintingService8087PythonSAM-based room segmentationThermalOptimizer8089PythonTile scraping (Castorama)WebScrapingService5000PythonPaint scraping (Castorama)

✨ Key Features

Paint Calculator — User submits a list of wall areas (width × height, add/subtract). The system calculates total square meters, queries the eBay Browse API for paints filtered by color, and computes how many cans are needed and total cost (USD → PLN conversion included).
AI Room Segmentation — PaintingService uses Meta's Segment Anything Model (vit_b) to segment a room photo by a single click/point, returning an RGBA mask overlay — secured with JWT.
Web Scraping Pipeline — WebScrapingService scrapes Castorama's search pages (JSON-LD parsing) for paints; ThermalOptimizer scrapes tiles with size filtering and per-project cost calculation.
JWT Authentication — All services share the same secret key. MainService issues tokens on login/register; PaintingService and ThermalOptimizer validate them independently on every request.
AI Chatbot — ChatBotController proxies user questions to OpenRouter (GPT-4o-mini) and returns structured JSON responses.

<img width="1422" height="688" alt="image" src="https://github.com/user-attachments/assets/a93bd70e-aaac-41ba-b39e-865caf7ad765" />
