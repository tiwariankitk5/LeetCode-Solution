#!/usr/bin/env python3
"""Recalculate LeetCode difficulty totals from each solution's README."""

from pathlib import Path
import json
import re

ROOT = Path(__file__).resolve().parents[2]
README_PATH = ROOT / "README.md"
STATS_PATH = ROOT / "stats.json"
DIFFICULTIES = ("Easy", "Medium", "Hard")
counts = {difficulty: 0 for difficulty in DIFFICULTIES}


def read_difficulty(problem_readme: Path) -> str | None:
    """Read LeetHub's <h3>Difficulty</h3> or a simple Markdown equivalent."""
    content = problem_readme.read_text(encoding="utf-8", errors="replace")

    html_match = re.search(
        r"<h3\b[^>]*>\s*(Easy|Medium|Hard)\s*</h3>", content, re.IGNORECASE
    )
    if html_match:
        return html_match.group(1).title()

    markdown_match = re.search(
        r"(?im)^\s*(?:difficulty\s*:\s*|#{1,6}\s*)?(Easy|Medium|Hard)\s*$",
        content,
    )
    return markdown_match.group(1).title() if markdown_match else None


# Each problem is expected to live in its own top-level directory with a README.
for problem_dir in ROOT.iterdir():
    if not problem_dir.is_dir() or problem_dir.name.startswith("."):
        continue
    problem_readme = problem_dir / "README.md"
    if not problem_readme.is_file():
        continue
    difficulty = read_difficulty(problem_readme)
    if difficulty:
        counts[difficulty] += 1

total = sum(counts.values())

# Keep LeetHub's existing metadata (including its file hashes) intact.
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
STATS_PATH.write_text(json.dumps(stats, indent=2) + "\n", encoding="utf-8")

# Replace only the block between these two README headings.
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
updated_readme, replacements = progress_pattern.subn(progress_block, readme, count=1)
if replacements != 1:
    raise SystemExit(
        "Could not find the README Progress section followed by Problem Index; "
        "no README changes were written."
    )
README_PATH.write_text(updated_readme, encoding="utf-8")

print(
    "Updated LeetCode totals: "
    f"Easy={counts['Easy']}, Medium={counts['Medium']}, "
    f"Hard={counts['Hard']}, Total={total}"
)
