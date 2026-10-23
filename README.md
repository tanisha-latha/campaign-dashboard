# Campaign Dashboard

A small full-stack practice project for managing fundraising campaigns:
view, create and edit campaigns, plus summary stats.

| Part     | Tech                       | Folder      | Address                          |
| -------- | -------------------------- | ----------- | -------------------------------- |
| Database | MongoDB                    | -           | `localhost:27017`                |
| Backend  | Java 21, Spring Boot 4.1   | `backend/`  | `http://localhost:8080/api`      |
| Frontend | Vue 3, Nuxt 4, Pinia       | `app/`      | `http://localhost:3000`          |

## Requirements

- Node.js 22 and pnpm
- Java 21 (JDK)
- MongoDB running on `localhost:27017`

## Run it

Start these in order, each in its own terminal. Leave them running.

**1. MongoDB**

If MongoDB is installed as a service it's already running. Otherwise:

```bash
mongod --dbpath <path-to-a-data-folder>
```

**2. Backend** (from the `backend` folder). The first run downloads Maven and libraries.

```bash
./mvnw spring-boot:run
```

On Windows use `.\mvnw.cmd spring-boot:run`. If Java isn't found, set `JAVA_HOME` to your JDK folder first.
On first start the backend adds 4 sample campaigns to an empty database.

**3. Frontend** (from the project folder)

```bash
pnpm install
pnpm dev
```

Open http://localhost:3000. Stop any of them with `Ctrl+C`.

## API

| Method | Path                       | Does                          |
| ------ | -------------------------- | ----------------------------- |
| GET    | `/api/campaigns`           | List campaigns (newest first) |
| GET    | `/api/campaigns/stats`     | Totals for the summary cards  |
| GET    | `/api/campaigns/{id}`      | One campaign (404 if missing) |
| POST   | `/api/campaigns`           | Create (400 if invalid)       |
| PUT    | `/api/campaigns/{id}`      | Edit (400 if invalid)         |

## Tests

```bash
cd backend
./mvnw test
```

`CampaignServiceTest` runs without MongoDB; `BackendApplicationTests` needs MongoDB running.
