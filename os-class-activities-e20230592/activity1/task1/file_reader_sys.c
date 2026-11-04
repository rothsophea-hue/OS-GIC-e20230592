#include <fcntl.h>
#include <unistd.h>

int main() {
    char buffer[256];
    ssize_t bytesRead;

    int fd = open("output.txt", O_RDONLY);
    if (fd < 0) {
        const char *err = "Error opening file for reading\n";
        write(2, err, 31);
        return 1;
    }

    while ((bytesRead = read(fd, buffer, sizeof(buffer))) > 0) {
        write(1, buffer, bytesRead);
    }

    close(fd);
    return 0;
}
