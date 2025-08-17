# 🚀 Pismo Transaction Service

A Spring Boot application for managing Accounts and Transactions with PostgreSQL, following clean architecture and REST API standards.


---

## 📚 Tech Stack

- Java 23
- Spring Boot 3.5.4
- Spring Data JPA
- PostgreSQL 16
- Docker & Docker Compose
- Swagger UI (springdoc-openapi)

---

## ⚡ Features

- Create and fetch accounts by document number
- Create and fetch transactions for accounts
- Validations on API request payloads
- API documentation with Swagger UI

---

## 🔗 API Endpoints

| Method | Endpoint                     | Description                      |
|--------|------------------------------|----------------------------------|
| POST   | `/accounts`                   | Create a new account            |
| GET    | `/accounts/{id}`              | Fetch account details by ID     |
| POST   | `/transactions`               | Create a transaction            |





### 1️⃣ Create Account
```json
POST /accounts
{
  "documentNumber": "12345678900"
}
✅ Response

json
{
  "accountId": 1,
  "documentNumber": "12345678900"
}


### 2️⃣ Fetch Account by ID
json

GET /accounts/1
✅ Response

json
{
  "accountId": 1,
  "documentNumber": "12345678900"
}


### 3️⃣ Create Transaction
json

POST /transactions
{
  "accountId": 1,
  "operationTypeId": 4,
  "amount": 200.50
}

✅ Response

json
{
  "transactionId": 1,
  "accountId": 1,
  "operationTypeId": 4,
  "amount": 200.50,
  "eventDate": "2025-08-13T14:32:10"
}




## Database

![Transaction Database](images/TransactionDataBase.png)

![Accounts Database](images/AccountsDataBase.png)

![Operation Database](images/OperationDataBase.png)




🛠 Run Locally
bash
# 1. Clone repo
git clone https://github.com/nikhil699/Pismo_Capstone_Project.git

# 2. Build
mvn clean install

# 3. Run
mvn spring-boot: run




Swagger UI:
http://localhost:9090/swagger-ui/index.html#/



🐳 Run with Docker
bash
docker-compose up --build
App runs on: http://localhost:8080




PostgreSQL on: localhost:5432

🗄 Database Setup
sql
-- Connect to DB
 docker exec -it postgres-db psql -U postgres -d pismo_db

-- View Tables
\dt

-- Check Accounts
SELECT * FROM accounts;

-- Check Transactions
SELECT * FROM transactions;

-- Check Document
SELECT * FROM operationstypes;





💡 Developed with ❤️ and Java.
Nikhil Chaurasiya