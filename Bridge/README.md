# SkillBridge – Student Skill Exchange Platform (Backend)

Complete Spring Boot 3.4 / Java 21 REST API backend with MySQL persistence, JWT authentication, mutual skill matching, exchange request workflow, connections, notifications, and admin controls.

## Technology Stack
- **Java 21** & **Spring Boot 3.4.3**
- **Spring Web**, **Spring Data JPA**, **Spring Security**, **Spring Validation**
- **MySQL 8+** (`skillsbridge_db`) & **Hibernate**
- **JWT Authentication (`jjwt 0.12.6`)** & **BCrypt**
- **Swagger / OpenAPI 3 (`springdoc-openapi`)**
- **JUnit 5** & **Mockito**

## Setup & Running in IntelliJ IDEA

1. **Configure Environment Variables (Optional — defaults provided in `application.properties`):**
   - `DB_HOST` (default: `localhost`)
   - `DB_PORT` (default: `3306`)
   - `DB_NAME` (default: `skillsbridge_db`)
   - `DB_USERNAME` (default: `root`)
   - `DB_PASSWORD` (default: `root`)
   - `JWT_SECRET` (Base64-encoded 256-bit secret)
   - `ADMIN_EMAIL` (default: `admin@skillbridge.com`)
   - `ADMIN_PASSWORD` (default: `Admin@12345`)

2. **Database:**
   - Ensure MySQL is running on `localhost:3306`.
   - `skillsbridge_db` is created automatically (`createDatabaseIfNotExist=true`), and Hibernate `ddl-auto=update` creates/updates all tables on startup.

3. **Run the Application:**
   - Open `Bridge` in IntelliJ IDEA and run `com.skillbridge.SkillBridgeApplication`.
   - Access Swagger UI at: `http://localhost:8080/swagger-ui.html`
   - Run end-to-end HTTP tests directly inside IntelliJ using `api-tests.http`.
