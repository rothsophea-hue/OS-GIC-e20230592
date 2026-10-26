# Class Activity 1 — System Calls in Practice

- **Student Name:** Chin Rothsophea
- **Student ID:** e20230592
- **Date:** October 25, 2026

---

## Warm-Up: Hello System Call

Screenshot of running `hello_syscall.c` on Linux:

![Hello syscall](screenshots/task1_creator_sys.png)

Screenshot of running `hello_winapi.c` on Windows:

![Hello WinAPI](screenshots/task1_win.png)

Screenshot of running `copyfilesyscall.c` on Linux:

![Copy file syscall](screenshots/task1_creator_lib.png)

---

## Task 1: File Creator & Reader

### Part A — File Creator

**Describe your implementation:** The library version (`file_creator_lib.c`) uses high-level wrapper functions (`fopen()`, `fprintf()`) which manage buffers and stream states automatically. The system call version (`file_creator_sys.c`) directly invokes the kernel via `open()` with specific flags (`O_WRONLY | O_CREAT | O_TRUNC`) and writes data using write descriptors.

**Version A — Library Functions (`file_creator_lib.c`):**

![Task 1A - Library](screenshots/task1_creator_lib.png)

**Version B — POSIX System Calls (`file_creator_sys.c`):**

![Task 1A - Syscall](screenshots/task1_creator_sys.png)

**Questions:**

1. **What flags did you pass to `open()`? What does each flag mean?**
   - `O_WRONLY`: Opens the file for writing only.
   - `O_CREAT`: Creates the file if it does not already exist.
   - `O_TRUNC`: Truncates an existing file to length 0 if it already exists.

2. **What is `0644`? What does each digit represent?**
   - File permission mode granting read/write for owner, and read-only for group and others.

3. **What does `fopen("output.txt", "w")` do internally that you had to do manually?**
   - It handles stream buffering, memory allocation, and system-level file descriptor creation internally.

### Part B — File Reader & Display

**Version A — Library Functions (`file_reader_lib.c`):**

![Task 1B - Library](screenshots/task1_reader_lib.png)

**Version B — POSIX System Calls (`file_reader_sys.c`):**

![Task 1B - Syscall](screenshots/task1_reader_sys.png)

---

## Task 2: Directory Listing & File Info

### Version A — Library Functions (`dir_list_lib.c`)

![Task 2 - Version A](screenshots/task2_lib.png)

### Version B — System Calls (`dir_list_sys.c`)

![Task 2 - Version B](screenshots/task2_sys.png)

---

## Optional Bonus: Windows API (`file_creator_win.c`)

Screenshot of running on Windows:

![Task 1 - Windows](screenshots/task1_win.png)

---

## Task 3: strace Analysis

**Describe what you observed:** The library version executes significantly more system calls due to dynamic linker lookups (`/etc/ld.so.cache`, `libc.so.6`), memory allocation initialization, and stream buffer management compared to direct system calls.

### strace Output — Library Version (File Creator)

![strace - Library version File Creator](screenshots/strace_lib_creator.png)

### strace Output — System Call Version (File Creator)

![strace - System call version File Creator](screenshots/strace_sys_creator.png)

### strace Output — Library Version (File Reader)

![strace - Library version](screenshots/strace_lib_reader.png)

### strace Output — System Call Version (File Reader)

![strace - System call version](screenshots/strace_sys_reader.png)

### strace -c Summary Comparison

- **Library Summary:**
  ![strace summary - Library](screenshots/strace_summary_lib.png)
- **Syscall Summary:**
  ![strace summary - Syscall](screenshots/strace_summary_sys.png)

### Questions

1. **How many system calls does the library version make compared to the system call version?**
   - The library version triggers dozens of setup calls (`brk`, `mmap`, `mprotect`, `arch_prctl`), while the system call version invokes only essential operations.

2. **What extra system calls appear in the library version? What do they do?**
   - `brk` and `mmap` allocate heap memory and set up internal buffers; `mprotect` adjusts memory page protection flags.

3. **How many `write()` calls does `fprintf()` actually produce?**
   - It buffers text internally and executes a single buffered `write()` call upon flushing or closing.

4. **In your own words, what is the real difference between a library function and a system call?**
   - A library function is user-space helper code handling formatting and buffering, whereas a system call is the secure, privileged entry point requesting direct kernel services.

---

## Task 4: Exploring OS Structure

### System Information

- Kernel: Linux i4gic-os 7.0.0-38-generic #38-Ubuntu SMP PREEMPT_DYNAMIC
- CPU: Intel(R) Xeon(R) CPU E5-2630 v4 @ 2.20GHz
- Memory: MemTotal: 7600784 kB

![System Info](screenshots/task4_system_info.png)

### Process Information

![Process Info](screenshots/task4_process_info.png)

### Kernel Modules

![Kernel Modules](screenshots/task4_modules.png)

### OS Layers Diagram

![OS Layers Diagram](screenshots/task4_os_layers_diagram.png)

---


### Questions

1. **What is `/proc`? Is it a real filesystem on disk?**
   - `/proc` is a pseudo-filesystem (virtual filesystem) generated and maintained entirely in system memory by the Linux kernel. It is **not** a real filesystem stored on a physical hard disk; its files and directories are created on the fly when accessed to provide a direct interface to kernel data structures and system parameters.

2. **Monolithic kernel vs. microkernel — which type does Linux use?**
   - Linux uses a **monolithic kernel** architecture, though it incorporates modern modular design features that allow kernel modules (drivers, filesystems) to be loaded and unloaded dynamically at runtime.

3. **What memory regions do you see in `/proc/self/maps`?**
   - Typical memory regions include the process's executable code segment (text), read-only data, read-write data (data/bss), the stack, the heap, and memory mappings for shared libraries (such as `libc.so`), complete with their virtual address ranges and permission flags (e.g., `r-xp`, `rw-p`).

4. **Break down the kernel version string from `uname -a`.**
   - `7.0.0` represents the major, minor, and patch version of the kernel.
   - `-38` represents the specific revision/patch level from the distribution.
   - `generic` indicates the kernel configuration flavor built for general-purpose hardware.
   - `SMP` stands for Symmetric Multi-Processing (multi-core support).
   - `PREEMPT_DYNAMIC` indicates the preemption model configured in the kernel for low-latency responsiveness.

5. **How does `/proc` show that the OS is an intermediary between programs and hardware?**
   - `/proc` collects low-level hardware, CPU, memory, and process statistics directly from kernel space and exposes them safely to user-space applications. Because user programs cannot talk directly to hardware for security reasons, the OS acts as an intermediary by abstracting and presenting this structured data through virtual files.

---

## Reflection

What did you learn from this activity? What was the most surprising difference between library functions and system calls?

> Through this activity, I gained a deep understanding of how user-space applications communicate with the Linux kernel using POSIX system calls versus high-level library wrapper functions. The most surprising observation from the `strace` analysis was seeing how many extra setup and initialization calls (such as dynamic linker lookups, memory allocation via `brk`/`mmap`, and page protection adjustments) a simple library function triggers behind the scenes compared to a direct, minimal system call.
