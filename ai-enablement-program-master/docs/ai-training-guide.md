# AI Training Guide

This project is used as a practical AI Enablement Program A0-A5 course. Each employee completes the course in their own branch and works through the same assignment backlog. By the end, the branch contains not only training artifacts, but also several concrete functional improvements to the demo portal.

## Branch Model

Each participant creates a separate branch:

```text
training/<username>/ai-enablement-course
```

Recommended commit order:

```text
A0 - catalog understanding
A1 - active shop filter
A2 - data quality panel, AI-ready repo and MCP smoke test
A3 - channel availability and reusable workflow
A4 - catalog sales journey agentic workflow
A5 - AI value portfolio and enablement dashboard
```

## Practical Backlog

| Level | Concrete branch result | Main skill |
| --- | --- | --- |
| A0 | `docs/training/artifacts/a0-catalog-understanding.md` | Project analysis through Cline/Cursor without changing code |
| A1 | `Active only` filter on `/shop` | First AI-assisted MR with a test and self-review |
| A2 | `Data Quality Panel` on `/catalog` + Cline/Cursor workspace setup + read-only Confluence MCP smoke test | AI-ready repository and MCP basics |
| A3 | `Channel Availability` end-to-end + reusable workflow | Reusable workflows/commands |
| A4 | reusable `catalog-sales-journey` workflow + example `/shop/bundles` | Agentic workflow, task split, approval gates |
| A5 | `/catalog/enablement` + AI use-case portfolio | Cost-value thinking, metrics, rollout |

## Supported Tracks

The practice can be completed in one of two supported tracks.

| Track | When to use it | Artifacts |
| --- | --- | --- |
| Cline | VS Code / IntelliJ with Cline | `.clinerules/`, `.clinerules/workflows/`, `.cline/skills/`, `.clineignore` |
| Cursor | Cursor IDE | `.cursor/rules/`, `.cursor/commands/`, `.cursor/agents/`, `.cursorignore` |

## Cline Setup

Open the repository root in VS Code with Cline installed.

Project rules:

```text
.clinerules/
```

Workspace workflows:

```text
.clinerules/workflows/
```

Workspace skills, used starting from A3:

```text
.cline/skills/
```

For skills, enable Cline Settings -> Features -> Enable Skills.

Self-check workflows:

```text
/evaluate-a0.md
/evaluate-a1.md
/evaluate-a2.md
/evaluate-a3.md
/evaluate-a4.md
```

## Cursor Setup

Open the repository root in Cursor.

Project rules:

```text
.cursor/rules/
```

Reusable commands:

```text
.cursor/commands/
```

Subagents, used starting from A4:

```text
.cursor/agents/
```

Ignore rules:

```text
.cursorignore
```

For A0, use Cursor Ask mode. For tasks where files need to be changed or a report needs to be created, use Agent mode after an approved plan.

Cursor model note: models have different costs and consume included usage differently. Check current rates in [Cursor Models and Pricing](https://cursor.com/docs/models-and-pricing). For read-only analysis, regular questions, small edits and routine agent tasks, use Auto/Composer or the default model. For complex architecture, multi-file implementation, hard debugging and review, you can temporarily switch to a stronger API/Premium model, then return to a cheaper/default model.

Self-check commands:

```text
/evaluate-a0
/evaluate-a1
/evaluate-a2
/evaluate-a3
/evaluate-a4
```

## Core Cline Concepts

- Prompt: a concrete user request in the current task.
- Context: files, diff, terminal output, browser state, docs and other data Cline can see right now.
- Instructions: stable instructions for AI behavior.
- Rules: project rules in `.clinerules/` that define constraints and conventions.
- Memory: long-lived project and decision context that helps continue work after a pause.
- Workflow: a repeatable procedure in `.clinerules/workflows/`, invoked by a slash command.
- Skill: on-demand expertise in `.cline/skills/<name>/SKILL.md`, loaded starting from A3 when the task matches the skill description.
- MCP: an external context source or tool, for example Jira, Confluence, logs or docs. Basic read-only Confluence MCP is used starting from A2.
- Approval gate: a point where a human explicitly confirms an action, especially if it is risky.

## Core Cursor Concepts

- Ask mode: read-only exploration, convenient for A0 and research.
- Agent mode: implementation, file edits, report generation and command execution.
- Project Rules: persistent project guidance in `.cursor/rules/*.mdc`.
- Commands: reusable workflows in `.cursor/commands/*.md`, invoked through `/`.
- Subagents: independent focused agents in `.cursor/agents/*.md`, used on A4 for separate workstreams.
- `.cursorignore`: excludes build/generated/temp/secrets files from AI context.
- MCP: an external context source or tool. Basic read-only Confluence MCP is used starting from A2.

## Practical Assignments

Complete the assignments in order:

1. [A0 Catalog Understanding](training/a0.md)
2. [A1 Active Shop Filter](training/a1.md)
3. [A2 Data Quality Panel, AI-Ready Repo And MCP Smoke Test](training/a2.md)
4. [A3 Channel Availability And Reusable Workflow](training/a3.md)
5. [A4 Catalog-Backed Sales Journey Agentic Workflow](training/a4.md)
6. [A5 AI Value Portfolio](training/a5.md)

General assignment index:

- [Training Lab Index](training/README.md)

## Confirmation Matrix

| Transition | Confirming artifact |
| --- | --- |
| A0 -> A1 | 5 working prompts and a result validation checklist |
| A1 -> A2 | AI-assisted MR or training task with tests |
| A2 -> A3 | AI-ready repository for Cline or Cursor + read-only Confluence MCP smoke test |
| A3 -> A4 | End-to-end AI workflow with workflows/commands |
| A4 -> A5 | Reusable agentic workflow for the team |
| A5 | AI use-case portfolio with cost-value and metrics |

## General Rules For All Assignments

- Work in your own branch.
- Ask AI to do research first, then plan, then implementation.
- Keep assignment scope within the current level.
- Review the diff before moving to the next level.
- Do not accept AI-generated code without review.
- Run the specified checks.
- Do not add production integrations, real auth, DB, Docker, Kubernetes/OpenShift manifests, real BSS/OSS or a billing engine.
- Do not store credentials, tokens, real customer identifiers or sensitive data.

## Standard Verification Commands

Backend:

```bash
cd backend
mvn test
mvn -q clean package
```

Frontend:

```bash
cd frontend
npm test -- --watchAll=false
npm run build
```

Catalog import:

```bash
cd backend
mvn -pl catalog-import -am install -DskipTests
mvn -f catalog-import/pom.xml exec:java \
  -Dexec.mainClass=com.netcracker.telekom.catalog.importer.CatalogImportMain \
  -Dexec.args="../catalog/mobile-operator-catalog.xlsx ../catalog/generated/catalog.json 2026-04-26"
cp ../catalog/generated/catalog.json catalog-app/src/main/resources/catalog/generated/catalog.json
```
