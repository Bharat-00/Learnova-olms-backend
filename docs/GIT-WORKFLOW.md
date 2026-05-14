
---

## 3. `docs/GIT-WORKFLOW.md`

```md
# Learnova Git Workflow

This project follows a feature-branch based Git workflow.

## Main Branches

| Branch | Purpose |
|---|---|
| main | Stable production-ready code |
| dev | Active development integration branch |
| feature/* | Individual feature or microservice work |

## Standard Workflow

Always start new work from updated `dev`.

```bash
git checkout dev
git pull origin dev
git checkout -b feature/your-feature-name