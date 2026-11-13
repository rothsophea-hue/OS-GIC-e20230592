# OS Lab 4-Linux I/O Redirection and Pipelines (Hands-on)

## Overview
This lab covers Linux I/O redirection (`>`, `>>`, `<`, `2>`, `2>&1`), piping (`|`), command filters (`grep`, `cut`, `wc`), and exit status codes (`$?`).

---

## Debrief & Reflection Questions

### 1. What does each pipeline stage receive and produce?
* **`grep '^clear,' data/events.csv`**: Receives the raw CSV file contents from standard input or via file argument, searches for lines starting with `clear,`, and produces those matching rows on standard output (`stdout`).
* **`cut -d, -f2`**: Receives the matching rows from the previous stage's pipe, splits each line using comma (`,`) as the delimiter, extracts the second field (the quantity), and produces those values on `stdout`.
* **`wc -l`**: Receives the stream of extracted text lines from the pipe and produces a single integer representing the total line count.

### 2. Why is `wc -l` not a sum?
`wc -l` counts the **total number of lines** (rows) matching the search criteria. It does not perform arithmetic addition on the numerical values within those rows. For example, if three rows match (`clear,2`, `clear,7`, `clear,9`), `wc -l` outputs `3` (the number of lines), whereas a sum of quantities would add `2 + 7 + 9 = 18`.

### 3. How did you prove the missing-file message used `stderr`?
We proved this by redirecting standard output (`>`) and standard error (`2>`) to separate files (`evidence/stdout.txt` and `evidence/stderr.txt`). When executing a command on a non-existent file, `stdout.txt` remained completely empty, while the error warning was successfully captured in `stderr.txt`, proving the diagnostic message traveled exclusively over file descriptor 2 (`stderr`).

### 4. Why can a pipeline print `0` and still have an input failure?
A pipeline can output `0` (for instance, when a `grep` or `wc` command processes an empty or missing input stream and counts zero matches) while the preceding command in the pipeline encounters a file-not-found error. The error diagnostic is sent to `stderr`, bypassing the pipe entirely, so downstream filters receive zero bytes of `stdout` and cleanly output `0`.

---

## File Structure Verification
* `checkpoint.txt` — Contains the exact record count from `wc -l`.
* `evidence/` — Contains all core Milestone logs, edge case checks, stream tests, and `checkpoint-selected.txt`.
