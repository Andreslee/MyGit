#include <stdio.h>
#include <string.h>

int main(int argc, char **argv)
{
	char name[255];

	printf("Enter your name: ");
	fgets(name, 255, stdin);
	
	name[strlen(name)-1] = '\0'; /* remove the newline at the end */

	printf("Hello %s!\n", name);
	return 0;
	/* Modificado por por Brayan David Cajica,
	                  Christian Camilo Torres
	                  Francisco Andres Guzman*/
}
