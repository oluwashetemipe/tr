# Tredbase Backend

A Spring Boot 3.4 backend system for managing multi-parent student accounts with secure JWT-based authentication and a multi-table relational design for flexible, auditable payment flows.

---

## Build & Run Instructions

### Prerequisites
- Java 22+
- Maven 3.8+
- IDE (IntelliJ recommended)

### Build the Project

```bash
mvn clean install


Run the Application via maven command or the main class in your IDE
mvn spring-boot:run

The app starts on:
http://localhost:8080

Use the login endpoint to obtain a JWT:
POST /v1/api/auth/login
Content-Type: application/json

{
  "email": "admin@example.com",
  "password": "admin123"
}

Sample Data in inserted on Start Up in DATABASEINITIALIZER in the Util Package

Authorization: Bearer <token>
Then pass the JWT as a Bearer token to access protected endpoints:

Design Decisions
Security
JWT Authentication: Stateless, secure, and scalable.

Role-Based Access Control: Enforced using @PreAuthorize and role-based annotations.

H2 Console Access: Enabled in dev with frame options disabled for local debugging.

Global Exception Handling: Custom error handling using @RestControllerAdvice.

Multi-Table Payment Structure
Parent, Student, and ParentStudentRelationship tables allow:

Shared or sole custody.

Dynamic payment splits (payment_share).

Soft deletion via is_active.

This allows one student to be funded by multiple parents in configurable proportions, tracked via the join table.



Validation: Ensures parents have sufficient balance before transaction.

Precision: Uses BigDecimal for accurate financial calculations.

Concurrency: Optional @Transactional annotations to prevent race conditions.

Example
If a student’s total fee is 1,000 and two parents share custody (50/50):

Each parent pays 500 fees excluded.

Both parents' balances are deducted proportionally.

Student's balance increases by the full amount.


The app uses H2 for development/testing. Access via:

http://localhost:8080/h2-console
Settings:

JDBC URL: jdbc:h2:mem:test

Username: sa

Password: (blank)

Payment Processing as Admin
Import Curl request to postman
Replace the Authorization header value with your actual token received from login.
curl --location 'http://localhost:8080/v1/api/payments/process' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer <your_token_here>' \
--data-raw '{
  "studentId": "e2c569be-0f1e-4dd0-a8a6-1e5d309e25b9",
  "payingParentId": "8f14e45f-ea5e-4a17-b2f3-a8e4aa5b2db1",
  "amount": 200.00,
  "transactionRef": "WewO8r409",
  "fee": 0.5
}'


Fee Logic Explained
Your fee system dynamically calculates the transaction fee as follows:

Amount between 200 and 2000 (inclusive):

Default fee = 0.3

Amount above 2000:

Default fee = 0.05

Overridden Fee:

If a custom fee is passed in the request, it overrides the default logic.

Example
For a ₦200 payment:

Default fee: 0.3

Passed overridden fee: 0.5 → This is used instead of the default


