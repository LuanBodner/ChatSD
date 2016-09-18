#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "macros.h"

void thread_function(void * ptr) {

}

int main(int argc, char** argv) {

    struct in_addr in_address;
    struct sockaddr_in group_socket;

    int socket_id = socket(AF_INET, SOCK_DGRAM, 0);

    if (!socket_id) {

        printf("Opening Datagram Socket Error\n");
        exit(EXIT_FAILURE);
    }

    memset((char *) &group_socket, 0, sizeof (group_socket));

    group_socket.sin_family = AF_INET;
    group_socket.sin_addr.s_addr = inet_addr(IP_ADDRESS);
    group_socket.sin_port = htons(MULTICAST_PORT);

    sendto(socket_id, "msg", 3, 0, (struct sockaddr*) &group_socket, sizeof (group_socket));


    return (EXIT_SUCCESS);
}

