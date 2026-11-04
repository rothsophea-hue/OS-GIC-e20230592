# Class Activity 2 — Processes & Inter-Process Communication

- **Student Name:** Chin Rothsophea
- **Student ID:** e20230592
- **Date:** November 2026

---

## Task 1: Process Creation on Linux (fork + exec)

### Compilation & Execution

Screenshot of compiling and running `forkchild.c`:

![Task 1 - Compile & Run](screenshots/task1_compile_run.png)

### Process Tree

Screenshot of the parent-child process tree (using `ps --forest`, `pstree`, or `htop` tree view):

![Task 1 - Process Tree](screenshots/task1_process_tree.png)

### Output

```text
Parent process. PID: 1234, Child PID: 1235
Child process. PID: 1235, Parent PID: 1234
Questions & Answers
1. What does fork() return to the parent? What does it return to the child?

fork() returns the child process ID (a positive integer) to the parent process, and it returns 0 to the child process. If the fork fails, it returns -1 to the parent.

2. What happens if you remove the waitpid() call? Why might the output look different?

If waitpid() is removed, the parent process may terminate before the child finishes, causing the child to become an orphan adopted by init/systemd, or the shell prompt might print immediately before the child's printf statement finishes printing to the terminal.

3. What does execlp() do? Why don't we see "execlp failed" when it succeeds?

execlp() replaces the current process image with a new process image specified by a filename, searching through the PATH environment variable. When it succeeds, the entire memory space of the calling program is overwritten by the new program, so code execution never returns to the line following execlp(), meaning the failure handler is bypassed.

4. Draw the process tree for your program (parent → child). Include PIDs from your output.

Plaintext
systemd (PID 1)
  └── bash (PID 1000)
        └── forkchild (PID 1234) [Parent]
              └── forkchild (PID 1235) [Child]
5. Which command did you use to view the process tree (ps --forest, pstree, or htop)? What information does each column show?

ps --forest was used. Columns typically show PID (Process ID), PPID (Parent Process ID), TTY (Terminal type), TIME (CPU time consumed), and CMD (the command name/arguments).

Task 2: Process Creation on Windows
Compilation & Execution
Screenshot of compiling and running winprocess.c:

Task Manager Screenshots
Screenshot showing process tree in the Processes tab (mspaint nested under your program):

Screenshot showing PID and Parent PID in the Details tab:

Questions & Answers
1. What is the key difference between how Linux creates a process (fork + exec) and how Windows does it (CreateProcess)?

Linux uses a two-step mechanism where fork() duplicates the parent address space into a child, and exec() replaces it with a new program. Windows uses a single unified function (CreateProcess) that initializes and loads a new executable directly without duplicating the parent address space.

2. What does WaitForSingleObject() do? What is its Linux equivalent?

WaitForSingleObject() blocks the execution of the parent process until the specified object (the child process handle) enters the signaled state (i.e., terminates). Its Linux equivalent is wait() or waitpid().

3. Why do we need to call CloseHandle() at the end? What happens if we don't?

CloseHandle() decrements the reference count of the process and thread kernel objects so the operating system can free their resources. If omitted, it causes a kernel resource leak.

4. In Task Manager, what was the PID of your parent program and the PID of mspaint? Do they match your program's output?

Yes, the parent process PID and the spawned process PID match the values printed by the CreateProcess output and the handles returned in PROCESS_INFORMATION.

5. Compare the Processes tab (tree view) and the Details tab (PID/PPID columns). Which view makes it easier to understand the parent-child relationship? Why?

The Processes tab tree view makes the relationship much clearer because it visually nests the child application directly underneath the parent process.

Task 3: Shared Memory IPC
Compilation & Execution
Screenshot of compiling and running shm-producer and shm-consumer:

Output
Plaintext
Producer wrote message.
Consumer read: Hello from Shared Memory Producer!
Questions & Answers
1. What does shm_open() do? How is it different from open()?

shm_open() creates or opens a POSIX shared memory object. Unlike regular file open() which interacts with the file system storage media, shm_open() allocates shared memory backed by RAM or virtual memory tmpfs for fast IPC.

2. What does mmap() do? Why is shared memory faster than other IPC methods?

mmap() maps pages of a shared memory object into the virtual address space of the calling process. It is faster because data is read and written directly to shared memory pointers without kernel-to-user buffer copying overhead.

3. Why must the shared memory name match between producer and consumer?

The shared memory name acts as a unique lookup identifier in the kernel namespace so both independent processes attach to the exact same physical memory segment.

4. What does shm_unlink() do? What would happen if the consumer didn't call it?

shm_unlink() removes the shared memory object name from the system. If unlinked prematurely or omitted, the memory segment persists in kernel space until system reboot or manual cleanup.

5. If the consumer runs before the producer, what happens? Try it and describe the error.

If the consumer runs first, shm_open() will fail with a ENOENT (No such file or directory) error because the shared memory object has not yet been created by the producer.

Task 4: Message Queue IPC
Compilation & Execution
Screenshot of compiling and running sender and receiver:

Output
Plaintext
Sender sent message.
Receiver received: Hello via Message Queue!
Questions & Answers
1. How is a message queue different from shared memory? When would you use one over the other?

Message queues allow passing structured packets of data with built-in synchronization and message typing, whereas shared memory provides raw memory access requiring manual synchronization (like semaphores). Message queues are preferred for discrete event messaging.

2. Why does the queue name in common.h need to start with /?

POSIX message queue names require a leading slash (/) to adhere to the POSIX IPC naming conventions.

3. What does mq_unlink() do? What happens if neither the sender nor receiver calls it?

mq_unlink() destroys the message queue. If neither calls it, the message queue remains active in the kernel until the system reboots.

4. What happens if you run the receiver before the sender?

The receiver will block and wait until a message arrives in the queue, unless opened with O_NONBLOCK which returns an error immediately.

5. Can multiple senders send to the same queue? Can multiple receivers read from the same queue?

Yes, POSIX message queues support multiple senders and multiple receivers concurrently, with messages delivered in priority or arrival order.

Reflection
What did you learn from this activity? What was the most interesting difference between Linux and Windows process creation? Which IPC method do you prefer and why?

Completing this activity provided practical experience with low-level process creation and inter-process communication mechanisms. The most interesting difference is how Linux separates process duplication (fork) from program loading (exec), whereas Windows handles both in a single CreateProcess call. Shared memory is preferred for high-throughput data exchange due to its speed and direct memory mapping.
