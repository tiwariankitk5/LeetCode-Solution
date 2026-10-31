
#!/usr/bin/env python3
"""Automatically update LeetCode statistics and Problem Index."""

from pathlib import Path
import json
import re

ROOT = Path(__file__).resolve().parents[2]
README_PATH = ROOT / "README.md"
STATS_PATH = ROOT / "stats.json"

DIFFICULTIES = ("Easy", "Medium", "Hard")
counts = {difficulty: 0 for difficulty in DIFFICULTIES}
problems = []


def read_problem_info(problem_readme: Path):
    """Extract problem name, difficulty, and topics from README."""

    content = problem_readme.read_text(
        encoding="utf-8", errors="replace"
    )

    difficulty_match = re.search(
        r"<h3\b[^>]*>\s*(Easy|Medium|Hard)\s*</h3>",
        content,
        re.IGNORECASE,
    )

    if not difficulty_match:
        difficulty_match = re.search(
            r"(?im)^\s*(?:Difficulty\s*:\s*)?(Easy|Medium|Hard)\s*$",
            content,
        )

    if not difficulty_match:
        return None

    difficulty = difficulty_match.group(1).title()

    # Extract problem title from the first README heading.
    title_match = re.search(
        r"(?im)^\s*#{1,6}\s+(.+?)\s*$",
        content,
    )

    title = title_match.group(1).strip() if title_match else problem_readme.parent.name

    # Extract topics from a Topics heading or Topics field.
    topics_match = re.search(
        r"(?is)(?:<h\d>\s*Topics\s*</h\d>|^\s*Topics\s*:\s*)(.*?)(?=\n\s*(?:<h\d>|#{1,6}\s|Difficulty\s*:)|\Z)",
        content,
    )

    topics = ""

    if topics_match:
        topics_text = re.sub(r"<[^>]+>", " ", topics_match.group(1))
        topics_text = re.sub(r"[\[\]()]","", topics_text)
        topics = ", ".join(
            topic.strip()
            for topic in re.split(r"[,|]", topics_text)
            if topic.strip()
        )

    return {
        "number": problem_readme.parent.name[:4],
        "title": title,
        "difficulty": difficulty,
        "topics": topics or "—",
        "folder": problem_readme.parent.name,
    }


# Scan each problem folder.
for problem_dir in ROOT.iterdir():

    if not problem_dir.is_dir() or problem_dir.name.startswith("."):
        continue

    problem_readme = problem_dir / "README.md"

    if not problem_readme.is_file():
        continue

    problem = read_problem_info(problem_readme)

    if problem:
        counts[problem["difficulty"]] += 1
        problems.append(problem)


total = sum(counts.values())

# Update stats.json while preserving existing metadata.
try:
    stats = json.loads(STATS_PATH.read_text(encoding="utf-8"))
except (FileNotFoundError, json.JSONDecodeError):
    stats = {}

leetcode_stats = stats.setdefault("leetcode", {})

leetcode_stats.update(
    {
        "easy": counts["Easy"],
        "medium": counts["Medium"],
        "hard": counts["Hard"],
        "solved": total,
    }
)

STATS_PATH.write_text(
    json.dumps(stats, indent=2) + "\n",
    encoding="utf-8",
)


# Update Progress table.
readme = README_PATH.read_text(encoding="utf-8")

progress_pattern = re.compile(
    r"(?ms)^## Progress\s*\n.*?(?=^## Problem Index\s*$)"
)

progress_block = (
    "## Progress\n\n"
    "| Metric | Current |\n"
    "|---|---:|\n"
    f"| Problems tracked | {total} |\n"
    f"| Easy | {counts['Easy']} |\n"
    f"| Medium | {counts['Medium']} |\n"
    f"| Hard | {counts['Hard']} |\n\n"
)

readme, progress_replacements = progress_pattern.subn(
    progress_block, readme, count=1
)

if progress_replacements != 1:
    raise SystemExit("Could not update Progress section.")


# Generate Problem Index table.
index_pattern = re.compile(
    r"(?ms)(^## Problem Index\s*\n).*?(?=^## |\Z)"
)

problems.sort(key=lambda problem: problem["number"])

index_rows = [
    "| # | Problem | Difficulty | Topics |",
    "|---:|---|---|---|",
]

for problem in problems:

    folder = problem["folder"]

    problem_link = (
        f"[{problem['title']}](./{folder}/README.md)"
    )

    index_rows.append(
        f"| {problem['number']} | {problem_link} | "
        f"{problem['difficulty']} | {problem['topics']} |"
    )

index_block = (
    "## Problem Index\n\n"
    + "\n".join(index_rows)
    + "\n\n"
)

readme, index_replacements = index_pattern.subn(
    lambda match: match.group(1) + "\n" + index_block.split(
        "## Problem Index\n\n", 1
    )[1],
    readme,
    count=1,
)

if index_replacements != 1:
    raise SystemExit("Could not update Problem Index section.")

README_PATH.write_text(readme, encoding="utf-8")

print(
    "Updated statistics and Problem Index: "
    f"Easy={counts['Easy']}, "
    f"Medium={counts['Medium']}, "
    f"Hard={counts['Hard']}, "
    f"Total={total}"
)
