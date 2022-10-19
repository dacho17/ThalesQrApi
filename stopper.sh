# #!/bin/bash

show_script_title() {
	echo ""
	echo "###############################################"
	echo "#         WELCOME TO STOPPER SCRIPT           #"
	echo "###############################################"
}

show_script_shutdown_msg() {
	echo "###############################################"
	echo "#### Shutting down QrCode REST API Stopper  ###"
	echo "###############################################"
	echo ""
}

show_contdown() {
	sleep 1
	echo "3"
	sleep 1
	echo "2"
	sleep 1
	echo "1"
}

stop_application() {
	docker stop api
	docker stop api_db
	sleep 3
}


show_script_title

if [ "$(docker ps -aq -f status=exited -f name=api)" ] && [ "$(docker ps -aq -f status=exited -f name=api_db)" ]
then
	echo "The application has already been shut down".
	sleep 1
	echo $'Exiting the stopper...\n'
	sleep 1
else
    echo "The application is about to be shut down".
	echo "Shutting down in:"
	show_contdown
	echo $'\nAllowing the application to shut down gracefully.'
	sleep 1
	echo $'\nWaiting for a couple of seconds...'

	stop_application

	echo $'\nThe application has been shut down.\n'
	sleep 1
fi


show_script_shutdown_msg
