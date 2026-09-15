# Netcracker Telekom Catalog Demo

Training demo portal for the AI Enablement Program, prepared for Cline and Cursor practice. The project simulates a mobile operator customer shop and an internal catalog portal: the Excel workbook is the source of truth, the importer normalizes data into JSON, the Quarkus backend serves REST APIs, and the React frontend shows offers for purchase.

This is not a production system. The project is intentionally compact, but resembles an enterprise portal: Excel configuration, backend/frontend code, rules, workflows, skills and agentic execution patterns.

## Business Scenario

Netcracker Telekom manages a catalog of devices, postpaid plans, fiber plans, accessories and digital services. Business users maintain catalog rows in Excel. Engineering imports the workbook into normalized JSON and serves the data through a backend similar to an orchestration/API gateway.

The frontend contains:

- a customer-facing shop for buying plans/devices/services;
- an internal catalog view for search, traceability and data quality checks.

Out of scope:

- real auth;
- CIAM;
- BSS/OSS integration;
- GraphQL;
- Consul;
- Infinispan cluster;
- WebSocket features;
- database persistence;
- Docker;
- Kubernetes/OpenShift manifests;
- production billing logic.

## Repository Structure

```text
backend/
  pom.xml
  catalog-core/
  catalog-api/
  catalog-import/
  catalog-app/
frontend/
  package.json
  package-lock.json
  config-overrides.js
  src/
catalog/
  mobile-operator-catalog.xlsx
  generated/catalog.json
docs/
  catalog-analysis.md
  ai-training-guide.md
  training/
    README.md
    a0.md
    a1.md
    a2.md
    a3.md
    a4.md
    a5.md
    artifacts/
.clinerules/
  01-project-context.md
  02-engineering-workflow.md
  workflows/
    backend-api-change.md
    catalog-import.md
    evaluate-a0.md
    evaluate-a1.md
    evaluate-a2.md
    evaluate-a3.md
    evaluate-a4.md
    frontend-shop-change.md
.cline/
  skills/
    catalog-excel-analysis/
    quarkus-catalog-backend/
    react-catalog-shop/
.cursor/
  rules/
    project.mdc
    training.mdc
  commands/
    evaluate-a0.md
    evaluate-a1.md
    evaluate-a2.md
    evaluate-a3.md
    evaluate-a4.md
  agents/
    # created by learners on A4, e.g. catalog-api-agent.md
.cursorignore
AGENTS.md
README.md
```

## AI Training Setup

The project is prepared for training in Cline and Cursor.

### Cline

What already exists:

- `.clinerules/01-project-context.md` - stable project context, stack, constraints and out-of-scope items.
- `.clinerules/02-engineering-workflow.md` - Cline work rules, commands and definition of done.
- `.clinerules/workflows/catalog-import.md` - repeatable Excel import workflow.
- `.clinerules/workflows/backend-api-change.md` - backend API change workflow.
- `.clinerules/workflows/frontend-shop-change.md` - customer shop/frontend change workflow.
- `.clinerules/workflows/evaluate-a0.md` - A0 evaluation workflow.
- `.clinerules/workflows/evaluate-a1.md` - A1 evaluation workflow.
- `.clinerules/workflows/evaluate-a2.md` - A2 evaluation workflow.
- `.clinerules/workflows/evaluate-a3.md` - A3 evaluation workflow.
- `.clinerules/workflows/evaluate-a4.md` - A4 evaluation workflow.
- `.cline/skills/catalog-excel-analysis/SKILL.md` - skill for Excel/catalog analysis.
- `.cline/skills/quarkus-catalog-backend/SKILL.md` - skill for backend/API tasks.
- `.cline/skills/react-catalog-shop/SKILL.md` - skill for frontend/shop tasks.

Recommended participant setup:

1. Open the repository root in VS Code with Cline enabled.
2. Check that workspace rules from `.clinerules/` are enabled in Cline.
3. Start with analysis-only prompts on A0.
4. Use Cline workflows for repeatable tasks, for example `/catalog-import.md` or `/frontend-shop-change.md`.
5. Before A3, enable Skills: Cline Settings -> Features -> Enable Skills.
6. For A3+, check that Cline sees workspace skills from `.cline/skills/`.
7. Before implementation tasks, ask Cline to show which files it plans to change.

How to explain Cline customization layers:

- Rules: always-on project constraints and conventions.
- Skills: on-demand domain expertise loaded when the request matches the skill description.
- Workflows: explicit slash-command procedures for repeatable tasks.

### Cursor

What already exists:

- `.cursor/rules/project.mdc` - stable project context, stack, constraints and out-of-scope items.
- `.cursor/rules/training.mdc` - course rules, Cursor modes and evaluation commands.
- `.cursor/commands/evaluate-a0.md` - reusable command for A0 evaluation.
- `.cursor/commands/evaluate-a1.md` - reusable command for A1 evaluation.
- `.cursor/commands/evaluate-a2.md` - reusable command for A2 evaluation.
- `.cursor/commands/evaluate-a3.md` - reusable command for A3 evaluation.
- `.cursor/commands/evaluate-a4.md` - reusable command for A4 evaluation.
- `.cursor/agents/` - Cursor subagents that learners create on A4 for agentic workflow.
- `.cursorignore` - exclusions for build output, dependencies, logs, secrets and local source-customer files.

Recommended participant setup:

1. Open the repository root in Cursor.
2. Check that Cursor sees project rules from `.cursor/rules/`.
3. On A0, use Ask mode for read-only analysis.
4. To write an evaluation report or implement features, use Agent mode after an approved plan.
5. For A0 self-check, call command `/evaluate-a0`.
6. For A1 self-check, call command `/evaluate-a1`.
7. For A2 self-check, call command `/evaluate-a2`.
8. For A3 self-check, call command `/evaluate-a3`.
9. For A4 self-check, call command `/evaluate-a4`.
10. Before implementation tasks, ask Cursor to show which files it plans to change.
11. Choose models deliberately: different Cursor models have different costs and consume usage differently. For regular Ask/research and small agent tasks, use Auto/Composer or the default model. Reserve stronger API/Premium models for complex architecture, multi-file implementation, hard debugging and review. Current pricing: [Cursor Models and Pricing](https://cursor.com/docs/models-and-pricing).

How to explain Cursor customization layers:

- Project Rules: always-on/scoped project constraints and conventions.
- Commands: reusable slash-command workflows from `.cursor/commands/`.
- Subagents: focused agents from `.cursor/agents/` for parallel workstreams on A4.
- Ask mode: read-only exploration.
- Agent mode: implementation, command execution and report generation.

## Excel As Source Of Truth

Sanitized workbook:

```bash
catalog/mobile-operator-catalog.xlsx
```

Generated normalized catalog:

```bash
catalog/generated/catalog.json
backend/catalog-app/src/main/resources/catalog/generated/catalog.json
```

By default, the backend reads the classpath copy. Catalog JSON contains product fields, prices, parameters, dependencies, validation issues and traceability fields: `sourceSheet`, `sourceRow`, `sourceFile`, `importedAt`.

## Backend

Stack:

- Java 17
- Maven multi-module
- Quarkus 3.23.2
- Quarkus REST / JAX-RS
- Jackson
- YAML config
- MicroProfile config
- OpenAPI / Swagger UI
- Health checks
- Micrometer Prometheus
- Lombok
- MapStruct
- Apache POI for Excel import

Before building the backend, check Java and Maven versions:

```bash
java -version
mvn -version
```

Expected versions:

- Java 17
- Maven 3.9.x

The project was verified on Java 17.0.18 and Maven 3.9.12.

Run backend tests:

```bash
cd backend
mvn test
```

Run backend in dev mode:

```bash
cd backend
mvn quarkus:dev -Dquarkus.test.continuous-testing=disabled
```

Backend local URL:

```text
http://localhost:8080
```

Swagger UI:

```text
http://localhost:8080/q/swagger-ui
```

Health:

```text
http://localhost:8080/q/health
```

Prometheus metrics:

```text
http://localhost:8080/q/metrics
```

## Catalog Import

The MVP uses build-time/script import. Runtime `POST /api/catalog/import` is intentionally not implemented.

Regenerate catalog JSON from Excel:

```bash
cd backend
mvn -pl catalog-import -am install -DskipTests
mvn -f catalog-import/pom.xml exec:java \
  -Dexec.mainClass=com.netcracker.telekom.catalog.importer.CatalogImportMain \
  -Dexec.args="../catalog/mobile-operator-catalog.xlsx ../catalog/generated/catalog.json 2026-04-26"
cp ../catalog/generated/catalog.json catalog-app/src/main/resources/catalog/generated/catalog.json
```

The fixed date keeps training statuses deterministic: future, active and expired products remain stable for exercises.

## REST API

```text
GET /api/catalog/products
GET /api/catalog/products/{id}
GET /api/catalog/categories
GET /api/catalog/metadata
```

`GET /api/catalog/products` supports query params:

```text
search
category
status
type
priceFrom
priceTo
page
pageSize
```

Example:

```bash
curl "http://localhost:8080/api/catalog/products?search=phone&status=ACTIVE&pageSize=5"
```

## Frontend

Stack:

- React 19.1.0
- React DOM 19.1.0
- TypeScript 4.8.4
- CRA / react-scripts 5.0.1
- react-app-rewired
- React Router `createBrowserRouter`
- Redux Toolkit 2.5.0
- RTK Query
- React Redux 9.2.0
- Ant Design 5.26.0
- `@ant-design/v5-patch-for-react-19`
- Less
- i18next / react-i18next
- dayjs

Install dependencies:

```bash
cd frontend
npm install --legacy-peer-deps --lockfile-version=2
```

Run frontend:

```bash
cd frontend
npm start
```

Customer shop:

```text
http://localhost:3000/shop
```

Internal catalog view:

```text
http://localhost:3000/catalog
```

Build frontend:

```bash
cd frontend
npm run build
```

Run frontend smoke test:

```bash
cd frontend
npm test -- --watchAll=false
```

## Local Deployment Model

- Quarkus backend runs on `8080`.
- CRA dev server runs on `3000` and proxies `/api` routes to the backend.

## Demo Script

This script is for the trainer or an overview demo before the practice starts. Students do not need to complete it as a separate assignment: after receiving access, cloning the project, creating a personal branch and starting backend/frontend, move to A0 in `docs/training/a0.md`.

1. Open `docs/catalog-analysis.md` and show how AI analyzed the workbook.
2. Open `catalog/mobile-operator-catalog.xlsx` and show product rows, price rows and edge cases.
3. Start the backend and open Swagger UI.
4. Call `/api/catalog/metadata` and show source sheets, import date and warning count.
5. Open frontend `/shop`.
6. Show Mobile plans, Fiber, Devices and Accessories segments.
7. Add `Postpaid Unlimited 299` or `Fiber Home Plus` to cart and open checkout.
8. Open `/catalog`, search for `iphone`, open `iPhone 14 Midnight 128GB`.
9. Show source traceability: `Equipment Offerings` row `3`.
10. Open `Samsung S25 128GB Mint`, show validation warnings and no-price handling.

## AI Enablement A0-A5

Each participant works in a separate branch:

```text
training/<username>/ai-enablement-course
```

Concrete lab track:

- [docs/ai-training-guide.md](docs/ai-training-guide.md)
- [docs/training/README.md](docs/training/README.md)
- [docs/training/a0.md](docs/training/a0.md)
- [docs/training/a1.md](docs/training/a1.md)
- [docs/training/a2.md](docs/training/a2.md)
- [docs/training/a3.md](docs/training/a3.md)
- [docs/training/a4.md](docs/training/a4.md)
- [docs/training/a5.md](docs/training/a5.md)

Course progression:

- A0: create the catalog understanding artifact.
- A1: implement the `Active only` filter on `/shop`.
- A2: implement the `Data Quality Panel` on `/catalog`, make the repo AI-ready for Cline or Cursor and complete a read-only Confluence MCP smoke test.
- A3: implement `channelAvailability` end-to-end and create a reusable workflow for the selected tool.
- A4: create a reusable `catalog-sales-journey` agentic workflow and apply it to `/shop/bundles`.
- A5: implement `/catalog/enablement` and an AI use-case portfolio.

## Current Verification

The project has focused tests for:

- Excel transform/import logic.
- Filtering/search logic.
- API `/api/catalog/products`.
- API `/api/catalog/products/{id}`.
- Traceability fields.
- Frontend smoke rendering.
