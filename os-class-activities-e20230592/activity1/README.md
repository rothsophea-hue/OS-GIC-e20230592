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

### Questions

1. **What is `/proc`? Is it a real filesystem on disk?**
   - No, `/proc` is not a real filesystem stored on a physical disk. It is a pseudo-filesystem (virtual filesystem) maintained entirely in the system's volatile memory (RAM) by the Linux kernel, acting as a control and information hub for kernel and process data.

2. **Monolithic kernel vs. microkernel — which type does Linux use?**
   - Linux uses a **monolithic kernel**, where CPU scheduling, file systems, device drivers, and memory management all run together in kernel space for high performance.

3. **What memory regions do you see in `/proc/self/maps`?**
   - Key memory regions include the executable code segment (`r-xp`), initialized and uninitialized data segments (`rw-p`), the heap, the stack, and shared library mappings (such as `libc.so.6` and the dynamic linker `ld-linux.so`).

4. **Break down the kernel version string from `uname -a`.**
   - Example (`7.0.0-38-generic`): **7** is the major version, **0** is the minor version, **0** is the patch level, **-38** represents the Ubuntu revision/build number, and **generic** indicates the standard kernel configuration.

5. **How does `/proc` show that the OS is an intermediary between programs and hardware?**
   - It exposes internal kernel data structures and hardware metrics (like CPU and memory stats) directly to user-space applications through virtual files, showing how the OS translates low-level system metrics into readable interfaces.

---

## Reflection

What did you learn from this activity? What was the most surprising difference between library functions and system calls?

> Through this activity, I gained a deep understanding of how user-space applications communicate with the Linux kernel through system calls rather than just relying on standard library functions. The most surprising observation during the `strace` analysis was realizing just how many background system calls—such as memory management (`brk`, `mmap`), page protection (`mprotect`), and dynamic linking setup—happen behind the scenes even in a simple program when using high-level library functions like `fprintf()`, compared to the clean, direct execution of low-level POSIX system calls.
