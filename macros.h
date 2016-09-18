#ifndef MACROS_H
#define MACROS_H

#include <stdio.h>
#include <stdlib.h>

#define MIN_PORT 1024    /* minimum port allowed */
#define MAX_PORT 65535   /* maximum port allowed */

#ifndef INDIVIDUAL_PORT /* if INDIVIDUAL_PORT is not defined by the user */
#define INDIVIDUAL_PORT 6799 /* define default port address */
#endif

#ifndef MULTICAST_PORT /* if MULTICAST_PORT is not defined by the user */
#define MULTICAST_PORT 6789 /* define default port address */
#endif

#ifndef IP_ADDRESS /* if IP wasn't defined */
#define IP_ADDRESS "225.1.2.3" /* define the default IP*/
#endif

/* if the INDIVIDUAL_PORT was given an invalid number */
#if ((INDIVIDUAL_PORT > MAX_PORT) || (INDIVIDUAL_PORT < MIN_PORT))
#undef INDIVIDUAL_PORT /* undef the port */
#define INDIVIDUAL_PORT 6799 /* re-define with the default value */
#endif

/* if the MULTICAST_PORT was given an invalid number */
#if ((MULTICAST_PORT > MAX_PORT) || (MULTICAST_PORT < MIN_PORT))
#undef MULTICAST_PORT /* undef the port */
#define MULTICAST_PORT 6789 /* re-define with the default value */
#endif

#endif /* MACROS_H */

