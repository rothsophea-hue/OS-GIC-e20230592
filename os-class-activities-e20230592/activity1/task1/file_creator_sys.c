#include <fcntl.h>    // open(), O_WRONLY, O_CREAT, O_TRUNC
#include <unistd.h>   // write(), close()
#include <string.h>   // strlen()

int main() {
    int fd = open("output.txt", O_WRONLY | O_CREAT | O_TRUNC, 0644);
    if (fd < 0) {
        const char *err = "Error opening/creating file\n";
        write(2, err, strlen(err));
        return 1;
    }

    const char *text = "Hello from Operating Systems class!\n";
    write(fd, text, strlen(text));
    close(fd);

    const char *msg = "File created successfully!\n";
    write(1, msg, strlen(msg));
    return 0;
}
