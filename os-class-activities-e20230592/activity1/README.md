# Class Activity 1 — System Calls in Practice

* **Student Name:** Chin Rothsophea
* **Student ID:** e20230592
* **Date:** October 25, 2026

---

## Warm-Up: Hello System Call

* **Screenshot of running `hello_syscall.c` on Linux:**
  ![Hello Syscall](screenshots/hello_syscall.png)

* **Screenshot of running `hello_winapi.c` on Windows:**
  ![Hello WinAPI](screenshots/hello_winapi.png)

* **Screenshot of running `copyfilesyscall.c` on Linux:**
  ![Copy File Syscall](screenshots/copyfilesyscall.png)

---

## Task 1: File Creator & Reader

### Part A — File Creator
Describe your implementation: The library version (`file_creator_lib.c`) uses high-level wrapper functions (`fopen()`, `fprintf()`) which manage buffers and stream states automatically. The system call version (`file_creator_sys.c`) directly invokes the kernel via `open()` with specific flags (`O_WRONLY | O_CREAT | O_TRUNC`) and writes data using `write()` descriptors.

* **Version A — Library Functions (`file_creator_lib.c`):**
  ![Task 1 Creator Lib](screenshots/task1_creator_lib.png)

* **Version B — POSIX System Calls (`file_creator_sys.c`):**
  ![Task 1 Creator Sys](screenshots/task1_creator_sys.png)

### Questions:
1. **What flags did you pass to `open()`? What does each flag mean?**
   * `O_WRONLY`: Opens the file for writing only.
   * `O_CREAT`: Creates the file if it does not already exist.
   * `O_TRUNC`: Truncates an existing file to length 0 if it already exists.

---

### Part B — File Reader

* **Version A — Library Functions (`file_reader_lib.c`):**
  ![Task 1 Reader Lib](screenshots/task1_reader_lib.png)

* **Version B — POSIX System Calls (`file_reader_sys.c`):**
  ![Task 1 Reader Sys](screenshots/task1_reader_sys.png)

---

## Task 2: Directory Lister

* **Version A — Library Functions (`dir_list_lib.c`):**
  ![Task 2 Lib](screenshots/task2_lib.png)

* **Version B — POSIX System Calls (`dir_list_sys.c`):**
  ![Task 2 Sys](screenshots/task2_sys.png)

---

## Task 3: strace Analysis

**Describe what you observed:**
The library version executes significantly more system calls due to dynamic linker lookups (`/etc/ld.so.cache`, `libc.so.6`), memory allocation initialization, and stream buffer management compared to direct system calls.

* **strace Output — Library Version (File Creator):** 
  ![strace - Library version File Creator](screenshots/strace_lib_creator.png)
* **strace Output — System Call Version (File Creator):** 
  ![strace - System call version File Creator](screenshots/strace_sys_creator.png)
* **strace Output — Library Version (File Reader):** 
  ![strace - Library version](screenshots/strace_lib_reader.png)
* **strace Output — System Call Version (File Reader):** 
  ![strace - System call version](screenshots/strace_sys_reader.png)
* **strace -c Summary Comparison:**
  * Library: ![strace summary - Library](screenshots/strace_summary_lib.png)
  * Syscall: ![strace summary - Syscall](screenshots/strace_summary_sys.png)

### Questions:
1. **How many system calls does the library version make compared to the system call version?**
   > The library version triggers dozens of setup calls (`brk`, `mmap`, `mprotect`, `arch_prctl`), while the system call version invokes only essential operations.

2. **What extra system calls appear in the library version? What do they do?**
   > `brk` and `mmap` allocate heap memory and set up internal buffers; `mprotect` adjusts memory page protection flags.

3. **How many `write()` calls does `fprintf()` actually produce?**
   > It buffers text internally and executes a single buffered `write()` call upon flushing or closing.

4. **In your own words, what is the real difference between a library function and a system call?**
   > A library function is user-space helper code handling formatting and buffering, whereas a system call is the secure, privileged entry point requesting direct kernel services.

---

## Task 4: Exploring OS Structure

* **System Information (`uname -a`, `/proc/cpuinfo`, `/proc/meminfo`, `/proc/version`, `/proc/uptime`):**
  * Kernel: `Linux i4gic-os 7.0.0-38-generic #38-Ubuntu SMP PREEMPT_DYNAMIC`
  * CPU: `Intel(R) Xeon(R) CPU E5-2630 v4 @ 2.20GHz`
  * Memory: `MemTotal: 7600784 kB`
  * ![System Info](screenshots/task4_system_info.png)
* **Process Information (`/proc/self` & `ps aux`):** 
  ![Process Info](screenshots/task4_process_info.png)
* **Kernel Modules (`lsmod` & `modinfo bochs`):** 
  ![Kernel Modules](screenshots/task4_modules.png)
* **OS Layers Diagram:** 
  ![OS Layers Diagram](screenshots/task4_os_layers_diagram.png)
