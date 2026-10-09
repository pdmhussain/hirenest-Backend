# Employee Management - Employment Module

Package: `com.hirenest` | Spring Boot 3.2 | Java 17 | MySQL | Maven

## Run
1. Start MySQL (user `root`, password in `src/main/resources/application.properties`).
   The `hirenest` database is created automatically.
2. `mvn spring-boot:run` (or run `HiesnestApplication.java` from the IDE).

## Employment APIs (`/api/employment`)
| Method | URL | Purpose |
|--------|-----|---------|
| POST | /api/employment | Create employment details |
| GET | /api/employment | List all |
| GET | /api/employment/{id} | Get by id |
| GET | /api/employment/employee/{employeeId} | Get by employee |
| GET | /api/employment/domains | List the 9 allowed domains (for a dropdown) |
| GET | /api/employment/domain/{domain} | Filter by domain (case-insensitive), e.g. `/api/employment/domain/Data Science` or `/domain/DATA_SCIENCE` |
| GET | /api/employment/type/{FULL_TIME\|INTERN} | Filter by type |
| GET | /api/employment/status/{status} | Filter by status |
| PUT | /api/employment/{id} | Update |
| PATCH | /api/employment/{id}/status?status=ACTIVE | Change status |
| DELETE | /api/employment/{id} | Delete |

### Sample POST body
```json
{
  "employeeId": 1,
  "domain": "Java",
  "employmentType": "FULL_TIME",
  "joiningDate": "2026-10-01",
  "probationPeriod": 6
}
```
If probation dates are not sent, they are calculated from `joiningDate` + `probationPeriod` (months).

## Notes
- `domain` is restricted to these 9 values (enum `Domain`): **Java, Python, React Js, Mern Stack, DevOps, DevSecOps, Data Analyst, Data Engineering, Data Science**.
  Send either the display name (`"React Js"`) or the constant (`"REACT_JS"`), case-insensitive. Any other value returns 400 with the list of allowed values. Responses return the display name.
- If you ran an older version, drop the old `employment_details` table (or fix old `domain` values) so Hibernate recreates it with the enum-based `domain` column.
- `config`, `security`, `util` are placeholders for other members' modules (Auth/JWT etc.).
- Do not push real DB passwords to a shared Git repository.
