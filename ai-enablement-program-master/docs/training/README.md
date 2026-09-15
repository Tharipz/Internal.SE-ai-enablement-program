# AI Enablement Practical Assignment Index

Each employee completes these assignments in their own branch:

```text
training/<username>/ai-enablement-course
```

Assignments are completed in sequence. Each level adds a concrete artifact or product feature to the project. The practice can be completed in Cline or Cursor; the result and acceptance criteria are the same, only the tools differ.

## How To Start

1. Clone the repository.
2. Create a personal branch `training/<username>/ai-enablement-course`.
3. Open the project in the selected IDE:
   - Cline: VS Code or IntelliJ with Cline.
   - Cursor: Cursor IDE.
4. Read this index and the assignment for the current level.
5. First ask AI to research the project, then ask for a plan, and only then allow file changes.
6. After each level, save the artifact in `docs/training/artifacts/`.
7. Run the checks from the assignment and review the diff.

## Choose A Track

| Track | Where to complete it | Project artifacts |
| --- | --- | --- |
| Cline | VS Code / IntelliJ with Cline, Plan & Act, workflows, skills | `.clinerules/`, `.clinerules/workflows/`, `.cline/skills/`, `.clineignore` |
| Cursor | Cursor Ask/Agent modes, Project Rules, reusable commands and subagents | `.cursor/rules/`, `.cursor/commands/`, `.cursor/agents/`, `.cursorignore` |

A0 is completed without changing code:

- Cline: use Cline Chat/Plan and do not change product files.
- Cursor: use Ask mode for analysis; use Agent mode only to write the evaluation report.

Cursor note: different models consume limits and budget differently. Check the official [Cursor Models and Pricing](https://cursor.com/docs/models-and-pricing) page. For regular research, Ask mode, small edits and routine agent tasks, use Auto/Composer or the default model. Use stronger API/Premium models for complex architecture, multi-file implementation, difficult debugging and review, then return to a cheaper/default model.

## Assignments

| Level | Assignment | Main result |
| --- | --- | --- |
| A0 | [Catalog Understanding](a0.md) | `docs/training/artifacts/a0-catalog-understanding.md` |
| A1 | [Active Shop Filter](a1.md) | `/shop` hides expired offers by default |
| A2 | [Data Quality Panel, AI-Ready Repo And MCP Smoke Test](a2.md) | `/catalog` data quality panel + Cline/Cursor workspace setup + read-only Confluence MCP smoke test |
| A3 | [Channel Availability And Reusable Workflow](a3.md) | `channelAvailability` end-to-end + Cline/Cursor reusable workflow |
| A4 | [Catalog-Backed Sales Journey Agentic Workflow](a4.md) | reusable sales journey workflow + `/shop/bundles` example run |
| A5 | [AI Value Portfolio](a5.md) | `/catalog/enablement` + use-case portfolio |

## Recommended Commit Names

```text
A0 - catalog understanding
A1 - active shop filter
A2 - data quality panel, AI-ready repo and MCP smoke test
A3 - channel availability and reusable workflow
A4 - catalog sales journey agentic workflow
A5 - AI value portfolio and enablement dashboard
```

## Course Feedback

After completing the course, copy [course-feedback.md](course-feedback.md) to your personal artifacts folder and fill it in:

```text
docs/training/artifacts/course-feedback-<username>.md
```

The course author can collect these files and summarize feedback across students.

## Mandatory Rules

- Use Cline or Cursor inside the IDE, not only an external chat.
- Ask AI to research first.
- Ask AI to show a plan before changes.
- Keep scope within the current level.
- Review the diff manually.
- Run the checks from the assignment.
- Save the final training artifact.

## AI Level Evaluation

A0-A4 can be checked with the selected AI tool.

A slash command/workflow is a reusable instruction file that already exists in this repository. The student does not need to create it or understand its internals. After completing the level, open the project in Cline or Cursor and paste the evaluation prompt from the level assignment.

Important: `/evaluate-a*` commands and workflows are post-checks after completing a level, not instructions for generating the main result. First the student completes the assignment and creates the required artifact/code changes according to `a0.md`-`a4.md`. Then AI runs the evaluator and writes only `*-evaluation-report.md`. If the main artifact or required sections are missing, the evaluator must mark the result as FAIL and list required fixes instead of completing the work for the student.

Cline:

```text
/evaluate-a0.md
/evaluate-a1.md
/evaluate-a2.md
/evaluate-a3.md
/evaluate-a4.md
```

Cline workflow files live in `.clinerules/workflows/`.

Cursor:

```text
/evaluate-a0
/evaluate-a1
/evaluate-a2
/evaluate-a3
/evaluate-a4
```

Cursor command files live in `.cursor/commands/`. Use Agent mode for evaluation because AI must write an evaluation report file.

Expected reports:

```text
docs/training/artifacts/a0-evaluation-report.md
docs/training/artifacts/a1-evaluation-report.md
docs/training/artifacts/a2-evaluation-report.md
docs/training/artifacts/a3-evaluation-report.md
docs/training/artifacts/a4-evaluation-report.md
```
