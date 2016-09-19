#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>
#include "macros.h"

typedef struct {
    struct sockaddr_in socket;
    int socket_id;
    int address_length;
} SocketIn;

typedef struct {
    SocketIn socket_struct;
    char message[509];
} SocketMessage;

void initialize_multicast_socket(SocketIn * s) {

    if (!(s->socket_id = socket(AF_INET, SOCK_DGRAM, 0))) {

        fprintf(stdout, "Socket Error\n");
        exit(EXIT_FAILURE);
    }

    bzero((char *) s, sizeof (s->socket));

    s->socket.sin_family = AF_INET;
    s->socket.sin_addr.s_addr = htonl(INADDR_ANY);
    s->socket.sin_port = htons(MULTICAST_PORT);
    s->address_length = sizeof (s);
}

void * send_multicast_message(void * ptr) {

    SocketMessage * sm = (SocketMessage*) ptr;

    while (strcmp(sm->message, "exit")) {
        if (!sendto(sm->socket_struct.socket_id, sm->message,
                sizeof (sm->message), 0, (struct sockaddr *) &sm->socket_struct.socket,
                sm->socket_struct.address_length)) {

            fprintf(stdout, "Error Sending Message\n");
            exit(EXIT_FAILURE);
        } else
            fprintf(stdout, "Message Sent\n");

        printf("> ");
        scanf("%s", &sm->message);
    }
}

void * receive_multicast_message(void * ptr) {

    SocketIn * s = (SocketIn*) ptr;
    struct ip_mreq receive_message;
    char message[509];

    if (!bind(s->socket_id, (struct sockaddr *) &s->socket,
            sizeof (s->socket))) {

        fprintf(stdout, "Error Receiving Message\n");
        exit(EXIT_FAILURE);
    }

    receive_message.imr_multiaddr.s_addr = inet_addr(IP_ADDRESS);
    receive_message.imr_interface.s_addr = htonl(INADDR_ANY);

    if (setsockopt(s->socket_id, IPPROTO_IP, IP_ADD_MEMBERSHIP,
            &receive_message, sizeof (receive_message)) < 0) {

        fprintf(stdout, "Set Socket Error\n");
        exit(EXIT_FAILURE);
    }

    while (1) {
        printf("Rodando\n");
        if (recvfrom(s->socket_id, message, sizeof (message), 0,
                (struct sockaddr *) s, &s->address_length) < 0) {

            fprintf(stdout, "Invalid Message\n");
            exit(EXIT_FAILURE);
        }

        printf("%s: message = \"%s\"\n", inet_ntoa(s->socket.sin_addr), message);
    }

    pthread_exit(0);
}

int main(int argc, char** argv) {

    CLEAR_SCREEN;

    char command_string[STRING_SIZE];
    int thread_ctrl;

    SocketIn group_socket;
    SocketIn socket_receive;

    initialize_multicast_socket(&group_socket);
    initialize_multicast_socket(&socket_receive);

    pthread_t * threads = malloc(2 * sizeof (pthread_t));

    scanf("%s", command_string);

    SocketMessage sm;
    sm.socket_struct = group_socket;
    strcpy(sm.message, command_string);

    pthread_create(&threads[0], NULL, send_multicast_message,
            (void*) &sm);
    pthread_create(&threads[1], NULL, receive_multicast_message,
            (void*) &socket_receive);

    pthread_join(threads[0], NULL);
    pthread_join(threads[1], NULL);

    return (EXIT_SUCCESS);
}