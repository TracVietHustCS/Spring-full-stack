# 🛒 Spring Full-Stack E-commerce Application

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0+-6DB33F?style=flat-square&logo=spring-boot)](https://spring.io/projects/spring-boot)
[![Angular](https://img.shields.io/badge/Angular-15+-DD0031?style=flat-square&logo=angular)](https://angular.io/)
[![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=flat-square&logo=java)](https://www.oracle.com/java/)
[![Apache Kafka](https://img.shields.io/badge/Apache%20Kafka-231F20?style=flat-square&logo=apache-kafka)](https://kafka.apache.org/)
[![Redis](https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=redis&logoColor=white)](https://redis.io/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=flat-square&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=flat-square)](https://opensource.org/licenses/MIT)

> A modern, scalable e-commerce web application built with Angular frontend and Spring Boot backend, featuring real-time messaging, caching, payment integration, and microservices architecture.

## 📋 Table of Contents

- [Introduction](#-introduction)
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Architecture](#-architecture)
- [Prerequisites](#-prerequisites)
- [Installation](#-installation)
- [Configuration](#-configuration)
- [Usage](#-usage)
- [API Documentation](#-api-documentation)
- [Contributing](#-contributing)
- [License](#-license)

## 🎯 Introduction

This is a full-stack e-commerce application designed with modern web technologies and microservices principles. The application provides a complete online shopping experience with features like product catalog, shopping cart, order management, payment processing, and real-time notifications.

**Key Highlights:**

- 🏗️ **Microservices Architecture** - Scalable and maintainable design
- 🔄 **Real-time Processing** - Kafka-based event streaming
- ⚡ **High Performance** - Redis caching layer
- 💳 **Payment Integration** - VNPay gateway support
- 🛡️ **Secure** - JWT authentication and authorization
- 📱 **Responsive** - Mobile-first Angular frontend

## ✨ Features

### 🛍️ Customer Features

- User registration and authentication
- Product browsing and search
- Shopping cart management
- Order placement and tracking
- Payment processing with VNPay
- User profile management
- Order history and invoices

### 👨‍💼 Admin Features

- Product inventory management
- Order management and processing
- User management
- Analytics and reporting dashboard
- Real-time notification system

### 🔧 Technical Features

- RESTful API design
- Real-time event processing with Kafka
- Redis-based caching and session management
- Database migration with Flyway
- JWT-based authentication
- Microservices communication
- Automated testing suite

## 🛠️ Tech Stack

### Frontend

- **Angular 15+** - Modern web framework
- **TypeScript** - Type-safe JavaScript
- **Angular Material** - UI component library
- **RxJS** - Reactive programming
- **Bootstrap** - Responsive design

### Backend

- **Java 17** - Modern Java features
- **Spring Boot 3.0+** - Application framework
- **Spring Security** - Authentication & authorization
- **Spring Data JPA** - Database abstraction
- **Spring Cloud** - Microservices tools

### Database & Storage

- **PostgreSQL** - Primary database
- **Redis** - Caching and session store
- **Flyway** - Database migration tool

### Message Broker

- **Apache Kafka** - Event streaming platform
- **Kafka Connect** - Data integration

### Payment Gateway

- **VNPay** - Vietnamese payment processor

### DevOps & Deployment

- **Docker** - Containerization
- **Docker Compose** - Multi-container orchestration
- **Maven** - Java build tool
- **npm** - Node package manager

## 🏗️ Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Angular SPA   │    │  Load Balancer  │    │   Admin Panel   │
│    (Port 4200)  │    │   (Nginx/AWS)   │    │    (Port 4201)  │
└─────────┬───────┘    └─────────┬───────┘    └─────────┬───────┘
          │                      │                      │
          └──────────────────────┼──────────────────────┘
                                 │
                    ┌─────────────┴─────────────┐
                    │     API Gateway           │
                    │    (Port 8080)           │
                    └─────────────┬─────────────┘
                                 │
        ┌────────────────────────┼────────────────────────┐
        │                        │                        │
┌───────▼────────┐    ┌─────────▼────────┐    ┌─────────▼────────┐
│ User Service   │    │ Product Service  │    │ Order Service    │
│ (Port 8081)    │    │ (Port 8082)      │    │ (Port 8083)      │
└───────┬────────┘    └─────────┬────────┘    └─────────┬────────┘
        │                       │                       │
        └───────────────────────┼───────────────────────┘
                               │
                    ┌──────────▼──────────┐
                    │    Apache Kafka     │
                    │   (Port 9092)       │
                    └──────────┬──────────┘
                               │
        ┌──────────────────────┼──────────────────────┐
        │                      │                      │
┌───────▼────────┐    ┌────────▼────────┐    ┌───────▼────────┐
│  PostgreSQL    │    │     Redis       │    │    VNPay       │
│ (Port 5432)    │    │  (Port 6379)    │    │   Gateway      │
└────────────────┘    └─────────────────┘    └────────────────┘
```

## 📋 Prerequisites

### System Requirements

- **Java 17** or higher
- **Node.js 16+** and npm
- **PostgreSQL 13+**
- **Redis 6+**
- **Apache Kafka 3.0+**
- **Maven 3.8+**
- **Git**

### Development Tools (Recommended)

- **IntelliJ IDEA** or **VS Code**
- **Postman** for API testing
- **Docker Desktop** for containerization
- **pgAdmin** for database management

## 🚀 Installation

### 1. Clone the Repository

```bash
git clone https://github.com/TracVietHustCS/Spring-full-stack.git
cd Spring-full-stack
```

### 2. Database Setup

```bash
# Start PostgreSQL (using Docker)
docker run --name ecommerce-postgres \
  -e POSTGRES_DB=ecommerce \
  -e POSTGRES_USER=ecommerce_user \
  -e POSTGRES_PASSWORD=your_password \
  -p 5432:5432 -d postgres:13

# Start Redis
docker run --name ecommerce-redis \
  -p 6379:6379 -d redis:6-alpine
```

### 3. Kafka Setup

```bash
# Start Zookeeper
docker run --name zookeeper \
  -p 2181:2181 \
  -e ALLOW_ANONYMOUS_LOGIN=yes \
  -d bitnami/zookeeper:latest

# Start Kafka
docker run --name kafka \
  -p 9092:9092 \
  -e KAFKA_BROKER_ID=1 \
  -e KAFKA_CFG_ZOOKEEPER_CONNECT=localhost:2181 \
  -e ALLOW_PLAINTEXT_LISTENER=yes \
  -e KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
  -d bitnami/kafka:latest
```

### 4. Backend Setup

```bash
# Navigate to backend directory
cd backend

# Install dependencies and build
mvn clean install

# Run database migrations
mvn flyway:migrate

# Start the Spring Boot application
mvn spring-boot:run
```

### 5. Frontend Setup

```bash
# Navigate to frontend directory
cd ../frontend

# Install dependencies
npm install

# Start Angular development server
ng serve
```

## ⚙️ Configuration

### Backend Configuration (`application.yml`)

```yaml
server:
  port: 8080

spring:
  application:
    name: ecommerce-backend
  
  datasource:
    url: jdbc:postgresql://localhost:5432/ecommerce
    username: ecommerce_user
    password: ${DB_PASSWORD:your_password}
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
  
  redis:
    host: localhost
    port: 6379
    timeout: 2000ms
    lettuce:
      pool:
        max-active: 8
        max-idle: 8
        min-idle: 0
  
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.JsonSerializer
    consumer:
      group-id: ecommerce-group
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.JsonDeserializer

# VNPay Configuration
vnpay:
  terminal-id: ${VNPAY_TERMINAL_ID:your_terminal_id}
  secret-key: ${VNPAY_SECRET_KEY:your_secret_key}
  payment-url: ${VNPAY_PAYMENT_URL:https://sandbox.vnpayment.vn/paymentv2/vpcpay.html}
  return-url: ${VNPAY_RETURN_URL:http://localhost:4200/payment/return}

# JWT Configuration
jwt:
  secret: ${JWT_SECRET:your_jwt_secret_key}
  expiration: 86400000 # 24 hours

logging:
  level:
    com.ecommerce: DEBUG
    org.springframework.security: DEBUG
```

### Environment Variables (`.env`)

```bash
# Database
DB_PASSWORD=your_secure_password
DB_HOST=localhost
DB_PORT=5432

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379

# Kafka
KAFKA_BOOTSTRAP_SERVERS=localhost:9092

# VNPay
VNPAY_TERMINAL_ID=your_terminal_id
VNPAY_SECRET_KEY=your_secret_key
VNPAY_PAYMENT_URL=https://sandbox.vnpayment.vn/paymentv2/vpcpay.html
VNPAY_RETURN_URL=http://localhost:4200/payment/return

# JWT
JWT_SECRET=your_very_long_and_secure_jwt_secret_key_here

# Application
SPRING_PROFILES_ACTIVE=development
SERVER_PORT=8080
```

### Frontend Configuration (`environment.ts`)

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api',
  wsUrl: 'ws://localhost:8080/ws',
  vnpayReturnUrl: 'http://localhost:4200/payment/return',
  cacheTimeout: 300000, // 5 minutes
  pagination: {
    defaultPageSize: 12,
    pageSizeOptions: [6, 12, 24, 48]
  }
};
```

## 📖 Usage

### Starting the Application

1. **Start Infrastructure Services:**

```bash
# Using Docker Compose (recommended)
docker-compose up -d postgres redis kafka zookeeper
```

2. **Start Backend:**

```bash
cd backend
mvn spring-boot:run
```

3. **Start Frontend:**

```bash
cd frontend
ng serve
```

4. **Access the Application:**
   - Frontend: http://localhost:4200
   - Backend API: http://localhost:8080
   - API Documentation: http://localhost:8080/swagger-ui.html

### Default Admin Credentials

```
Username: admin@ecommerce.com
Password: Admin123!
```

## 🔌 API Documentation

### Authentication Endpoints

| Method | Endpoint               | Description       |
| ------ | ---------------------- | ----------------- |
| POST   | `/api/auth/login`    | User login        |
| POST   | `/api/auth/register` | User registration |
| POST   | `/api/auth/refresh`  | Refresh JWT token |
| POST   | `/api/auth/logout`   | User logout       |

### Product Endpoints

| Method | Endpoint               | Description        |
| ------ | ---------------------- | ------------------ |
| GET    | `/api/products`      | Get all products   |
| GET    | `/api/products/{id}` | Get product by ID  |
| POST   | `/api/products`      | Create new product |
| PUT    | `/api/products/{id}` | Update product     |
| DELETE | `/api/products/{id}` | Delete product     |

### Order Endpoints

| Method | Endpoint                    | Description         |
| ------ | --------------------------- | ------------------- |
| GET    | `/api/orders`             | Get user orders     |
| GET    | `/api/orders/{id}`        | Get order by ID     |
| POST   | `/api/orders`             | Create new order    |
| PUT    | `/api/orders/{id}/status` | Update order status |

### Payment Endpoints

| Method | Endpoint                       | Description           |
| ------ | ------------------------------ | --------------------- |
| POST   | `/api/payments/vnpay/create` | Create VNPay payment  |
| GET    | `/api/payments/vnpay/return` | Handle payment return |
| POST   | `/api/payments/{id}/verify`  | Verify payment status |

### Example API Requests

**Create Order:**

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "items": [
      {
        "productId": 1,
        "quantity": 2,
        "price": 29.99
      }
    ],
    "shippingAddress": {
      "street": "123 Main St",
      "city": "Ho Chi Minh City",
      "country": "Vietnam",
      "postalCode": "70000"
    }
  }'
```

**Get Products with Pagination:**

```bash
curl "http://localhost:8080/api/products?page=0&size=12&sort=name,asc"
```

## 🧪 Testing

### Running Backend Tests

```bash
cd backend
mvn test                    # Unit tests
mvn integration-test       # Integration tests
mvn verify                 # All tests
```

### Running Frontend Tests

```bash
cd frontend
npm test                   # Unit tests
npm run e2e               # End-to-end tests
npm run test:coverage     # Coverage report
```

### Test Data Setup

```bash
# Load sample data
mvn spring-boot:run -Dspring-boot.run.profiles=test-data
```

## 🐛 Troubleshooting

### Common Issues

**1. Database Connection Error**

```bash
# Check if PostgreSQL is running
docker ps | grep postgres

# Check database logs
docker logs ecommerce-postgres
```

**2. Kafka Connection Issues**

```bash
# Verify Kafka topics
docker exec -it kafka kafka-topics.sh --bootstrap-server localhost:9092 --list

# Create required topics manually
docker exec -it kafka kafka-topics.sh --bootstrap-server localhost:9092 --create --topic order-events --partitions 3 --replication-factor 1
```

**3. Redis Connection Problems**

```bash
# Test Redis connection
redis-cli -h localhost -p 6379 ping
# Should return: PONG
```

**4. Frontend Build Errors**

```bash
# Clear node_modules and reinstall
rm -rf node_modules package-lock.json
npm install

# Clear Angular cache
ng cache clean
```

**5. VNPay Integration Issues**

- Verify your VNPay credentials in environment variables
- Check that return URLs are correctly configured
- Ensure your IP is whitelisted in VNPay dashboard (for production)

### Performance Optimization

**Database Optimization:**

```sql
-- Add indexes for frequently queried columns
CREATE INDEX idx_products_category_id ON products(category_id);
CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_created_at ON orders(created_at);
```

**Redis Caching Strategy:**

- Product catalog: 1 hour TTL
- User sessions: 24 hours TTL
- Shopping carts: 7 days TTL

## 🤝 Contributing

### Development Workflow

1. **Fork the repository**
2. **Create a feature branch:**

```bash
git checkout -b feature/your-feature-name
```

3. **Make your changes and commit:**

```bash
git commit -m "feat: add your feature description"
```

4. **Push to your fork:**

```bash
git push origin feature/your-feature-name
```

5. **Create a Pull Request**

### Code Standards

**Backend (Java):**

- Follow Google Java Style Guide
- Use meaningful variable and method names
- Write unit tests for new features
- Document public APIs with JavaDoc

**Frontend (Angular):**

- Follow Angular Style Guide
- Use TypeScript strict mode
- Implement proper error handling
- Write component tests

### Commit Message Convention

```
feat: add new feature
fix: bug fix
docs: documentation changes
style: formatting changes
refactor: code refactoring
test: adding tests
chore: maintenance tasks
```

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

```
MIT License

Copyright (c) 2024 TracVietHustCS

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

## 🙏 Acknowledgments

- Spring Boot community for excellent documentation
- Angular team for the powerful framework
- Apache Kafka for reliable message streaming
- VNPay for payment gateway integration
- Open source contributors

---

**Made by [TracVietHustCS](https://github.com/TracVietHustCS)-introvert ComSci student**

*For support, please create an issue in the GitHub repository.*
