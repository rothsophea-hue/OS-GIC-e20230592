#include <stdio.h>
#include <stdlib.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <string.h>
#include "common.h"
int main() {
    int msqid = msgget(QUEUE_KEY, 0666 | IPC_CREAT);
    struct message msg; msg.mtype = 1;
    strcpy(msg.mtext, "Hello via Message Queue!");
    msgsnd(msqid, &msg, sizeof(msg.mtext), 0);
    printf("Sender sent message.\n");
    return 0;
}
