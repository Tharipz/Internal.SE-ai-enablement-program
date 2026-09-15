# AGENTS.md

## Project Overview

Netcracker Telekom Catalog Demo is an AI Enablement training project designed for Cline and Cursor exercises. It simulates a mobile operator customer shop and internal catalog portal where Excel is the source of truth for products and plans. The backend imports Excel into normalized JSON and serves catalog APIs. The frontend displays a customer shop, cart/checkout demo, searchable catalog, product details, source traceability and validation warnings.

This is a demo/training project, not a production system.

## Backend Stack

- Java 17
- Maven multi-module
- Quarkus 3.23.2
- Run with `--enable-preview`
- Quarkus REST / JAX-RS
- `quarkus-rest-jackson`
- `quarkus-config-yaml`
- MicroProfile config
- OpenAPI / Swagger UI
- Health checks
- Micrometer Prometheus
- Lombok
- MapStruct
- Jackson Databind
- Apache POI for Excel import

No database, JDBC or Hibernate ORM is used.

## Frontend Stack

- React 19.1.0
- React DOM 19.1.0
- TypeScript 4.8.4
- CRA / react-scripts 5.0.1
- react-app-rewired
- React Router with `createBrowserRouter`
- Redux Toolkit 2.5.0
- RTK Query
- React Redux 9.2.0
- Ant Design 5.26.0
- `@ant-design/v5-patch-for-react-19`
- Less
- i18next / react-i18next
- dayjs

Package manager: npm with `package-lock.json` lockfile v2.

## Repository Structure

```text
backend/
  catalog-core/      domain model and filtering/search service
  catalog-api/       DTOs and MapStruct mapper
  catalog-import/    Excel to normalized JSON importer
  catalog-app/       Quarkus runtime app and JAX-RS resources
frontend/
  src/app/           router and store
  src/api/           RTK Query API layer
  src/components/    UI components
  src/pages/         route pages
  src/i18n/          localization setup
  src/styles/        Less styles
catalog/
  mobile-operator-catalog.xlsx
  generated/catalog.json
docs/
  catalog-analysis.md
  ai-training-guide.md
  training/         concrete AI course labs and learner artifacts
.clinerules/
  01-project-context.md
  02-engineering-workflow.md
  workflows/
.cline/
  skills/
.cursor/
  rules/
  commands/
  agents/
.cursorignore
```

## Cline Rules, Skills And Workflows

Cline-specific instructions are stored in `.clinerules/`.

- `.clinerules/01-project-context.md`: project context, stack constraints and out-of-scope items.
- `.clinerules/02-engineering-workflow.md`: engineering rules, commands and definition of done.
- `.clinerules/workflows/catalog-import.md`: Excel import workflow.
- `.clinerules/workflows/backend-api-change.md`: backend API workflow.
- `.clinerules/workflows/frontend-shop-change.md`: customer shop/frontend workflow.
- `.clinerules/workflows/evaluate-a0.md`: Cline A0 self-check workflow.
- `.clinerules/workflows/evaluate-a1.md`: Cline A1 self-check workflow.
- `.clinerules/workflows/evaluate-a2.md`: Cline A2 self-check workflow.
- `.clinerules/workflows/evaluate-a3.md`: Cline A3 self-check workflow.
- `.clinerules/workflows/evaluate-a4.md`: Cline A4 self-check workflow.
- `.cline/skills/catalog-excel-analysis/SKILL.md`: on-demand Excel/catalog analysis skill.
- `.cline/skills/quarkus-catalog-backend/SKILL.md`: on-demand backend/API skill.
- `.cline/skills/react-catalog-shop/SKILL.md`: on-demand frontend/shop skill.

When training in Cline, prefer `.clinerules/` for active project rules, `.cline/skills/` for on-demand expertise and `.clinerules/workflows/` for repeatable slash-command procedures. Keep this `AGENTS.md` as a cross-agent reference.

## Cursor Rules And Commands

Cursor-specific instructions are stored in `.cursor/`.

- `.cursor/rules/project.mdc`: project context, stack constraints and out-of-scope items.
- `.cursor/rules/training.mdc`: training workflow rules, modes and validation commands.
- `.cursor/commands/evaluate-a0.md`: Cursor-native A0 self-check command.
- `.cursor/commands/evaluate-a1.md`: Cursor-native A1 self-check command.
- `.cursor/commands/evaluate-a2.md`: Cursor-native A2 self-check command.
- `.cursor/commands/evaluate-a3.md`: Cursor-native A3 self-check command.
- `.cursor/commands/evaluate-a4.md`: Cursor-native A4 self-check command.
- `.cursor/agents/`: Cursor subagents created by learners on A4 for focused agentic workflow workstreams.
- `.cursorignore`: excludes build output, dependencies, logs, secrets and local source-customer files from AI context.

When training in Cursor, use Ask mode for read-only analysis and Agent mode for implementation/report generation. Prefer `.cursor/rules/` for persistent project guidance, `.cursor/commands/` for reusable slash-command procedures and `.cursor/agents/` for focused subagents on A4.

## Training Course

Concrete learner labs are stored in `docs/training/`.

Each learner works in a separate branch:

```text
training/<username>/ai-enablement-course
```

Do not pre-implement lab features in the baseline unless explicitly requested. The labs intentionally ask learners to add:

- A1: `Active only` filter on `/shop`.
- A2: `Data Quality Panel`, Cline/Cursor AI-ready repo artifacts and mandatory read-only Confluence MCP smoke test.
- A3: `channelAvailability` plus a reusable workflow for the selected tool.
- A4: reusable `catalog-sales-journey` agentic workflow with `/shop/bundles` as the example run.
- A5: `/catalog/enablement` AI value dashboard.

## Backend Commands

Run all backend tests:

```bash
cd backend
mvn test
```

Run Quarkus dev mode:

```bash
cd backend
mvn quarkus:dev
```

Package backend:

```bash
cd backend
mvn clean package
```

Run packaged app:

```bash
cd backend/catalog-app
java --enable-preview -jar target/quarkus-app/quarkus-run.jar
```

## Catalog Import Commands

Regenerate normalized JSON:

```bash
cd backend
mvn -pl catalog-import -am install -DskipTests
mvn -f catalog-import/pom.xml exec:java \
  -Dexec.mainClass=com.netcracker.telekom.catalog.importer.CatalogImportMain \
  -Dexec.args="../catalog/mobile-operator-catalog.xlsx ../catalog/generated/catalog.json 2026-04-26"
cp ../catalog/generated/catalog.json catalog-app/src/main/resources/catalog/generated/catalog.json
```

Do not manually edit generated JSON unless the task explicitly asks for a fixture-only change.

## Frontend Commands

Install:

```bash
cd frontend
npm install --legacy-peer-deps --lockfile-version=2
```

Run dev server:

```bash
cd frontend
npm start
```

Build:

```bash
cd frontend
npm run build
```

Test:

```bash
cd frontend
npm test -- --watchAll=false
```

## Coding Conventions

- Keep the project small and readable.
- Prefer existing module boundaries over new abstractions.
- Keep Excel import deterministic for training.
- Keep `/shop` customer-facing and `/catalog` internal/traceability-focused.
- Preserve traceability fields: `sourceFile`, `sourceSheet`, `sourceRow`, `importedAt`.
- Add validation warnings for imperfect catalog data instead of silently dropping rows.
- Use MapStruct for DTO mapping when adding API fields.
- Use RTK Query for API calls.
- Use Ant Design components for UI controls.
- Keep Less styles scoped and simple.

## Testing Expectations

Add focused tests for:

- importer transformation logic;
- search/filter behavior;
- API list/detail endpoints;
- traceability fields;
- frontend smoke rendering when UI changes.

Run affected tests before handing work back. If a setup is too heavy, document the skipped check and why.

## Security and Privacy Constraints

- Do not add real customer names, credentials, tokens or production URLs.
- Do not reintroduce source-customer identifiers from the original workbook into sanitized demo data.
- Do not implement real auth, CIAM, BSS/OSS, GraphQL, Consul, Infinispan, WebSocket or deployment integrations.
- Keep the demo data neutral and fictional.

## What Not To Change Without Approval

- Technology stack versions.
- Maven module layout.
- Runtime import endpoint decision.
- Generated catalog schema shape.
- Sanitized operator name.
- Port assumptions in README.
- Removal of traceability fields.

## Definition of Done

- Excel workbook is analyzed and documented.
- Sanitized demo workbook exists.
- `catalog/generated/catalog.json` is generated.
- Quarkus backend serves catalog endpoints.
- React frontend displays catalog and details.
- Search and filters work.
- Source sheet/source row are visible.
- Tests are added and run.
- README and AI training guide are updated.
- No out-of-scope production integrations are added.
