# Lab 2 — Linux Navigation and File Management: individual report

- Student name/ID: Chin Rothsophea / e20230592
- Ubuntu username: gic-chin-rothsophea
- My values (`oslab values lab2`): file1 = "budget 2026.txt", file2 = "audit notes.txt", owner = Finance, reader = HR, third = Support
- Files: `evidence/paths.txt`, `evidence/tree.txt`, `evidence/checkpoint.txt`

## What I Did

| Step | My command | Current folder when I ran it |
| :--- | :--- | :--- |
| Move the first file (relative destination) | `mv "$file1" ../reports/` | `~/oslab-work/lab2/incoming` |
| Move the second file (absolute paths) | `mv "$OSLAB_WORKSPACE/lab2/incoming/$file2" "$OSLAB_WORKSPACE/lab2/reports/"` | `~/oslab-work/lab2` |
| Read the report from my reader department (relative path) | `cat "../../reports/$file1"` | `~/oslab-work/lab2/TechCorp/HR` |
| The path error I made on purpose, and its message | `cat ../reports/payroll may.txt`<br>*Message:* `cat: ../reports/payroll may.txt: No such file or directory` | `~/oslab-work/lab2/TechCorp/HR` |
| The relative path that fixed it | `../../reports/$file1` | `~/oslab-work/lab2/TechCorp/HR` |

## Explanation

A correct file name can still produce a "No such file or directory" error if the relative path is evaluated relative to the current working directory, which might not contain the targeted path hierarchy. Relative paths depend strictly on where you execute the command. The difference between `mv` and `cp` is that `mv` relocates a file from its source directory to a destination without creating duplicates, while `cp` creates a duplicate in the target location while leaving the original file intact.

## What My Tests Do Not Prove

Running `ls` only proves that a file or directory exists with a matching name in the filesystem tree. It does not verify whether the file's contents are intact or identical to the original, which requires a byte-by-byte comparison tool like `cmp`.

## Plus / Challenge (only if you did them)

Completed the Live Checkpoint tasks:
- Relative path from `TechCorp/Engineering/drafts` to `Legal/archive/first-original.txt`: `../../Legal/archive/first-original.txt`.
- Number of `..` required: 2.
- `cat reports/cash flow.txt` without quotes passes 2 arguments to `cat`.

## AI Note (optional)

An AI tool suggested using double quotes around variables with spaces (e.g., `"$file1"`). I checked this by running `ls -l incoming/$file1` without quotes, which failed because the shell split the file name into two arguments at the space.
