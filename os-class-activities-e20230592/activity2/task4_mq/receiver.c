#include <stdio.h>
#include <stdlib.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include "common.h"
int main() {
    int msqid = msgget(QUEUE_KEY, 0666 | IPC_CREAT);
    struct message msg;
    msgrcv(msqid, &msg, sizeof(msg.mtext), 1, 0);
    printf("Receiver received: %s\n", msg.mtext);
    msgctl(msqid, IPC_RMID, NULL);
    return 0;
}
