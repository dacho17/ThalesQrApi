# #!/bin/bash

show_contdown() {
	sleep 1
	echo "3"
	sleep 1
	echo "2"
	sleep 1
	echo "1"
}

show_script_title() {
	echo ""
	echo "###############################################"
	echo "#         WELCOME TO DRIVER SCRIPT            #"
	echo "###############################################"
}

show_driver_startup_msg() {
	echo ""
	echo "- Driver has been run for the QrCode REST API -"
	echo "-----------------------------------------------"
}

show_script_shutdown_msg() {
	echo "###############################################"
	echo "#### Shutting down QrCode REST API Driver  ####"
	echo "###############################################"
	echo ""
}

show_app_run_msg() {
	echo "##################### Read this message #####################"
	echo $'The application will be started now and will be available within 2 minutes on localhost:8080/swagger-ui/index.html.'
	echo $'After the application starts up and becomes available, you can both stay connected to the terminal and monitor application logging, or safely close the terminal.'
	echo $'Closing the terminal will not shut down the application.\n'
	echo $'Application has to be manually shut down using the following command: bash stopper.sh .'
	echo "#############################################################"
	sleep 20
	echo "The application will start building in:"
	show_contdown
}

show_test_run_msg() {
	echo "##################### Read this message #####################"
	echo $'The test suites will be started now and will run approximately 2 minutes.'
	echo $'The test results will be shown at the end of the script execution. The rest of the logs are related to building containers and running the application.'
	echo $'Upon the execution of the script, you will receive a shutdown message and be prompted by the terminal.'
	echo "#############################################################"
	sleep 20
	echo "The test support infrastructure will start building in:"
	show_contdown
}

app_already_running() {
	echo "The application is already running."
	sleep 1
	echo $'Exiting the driver...\n'
	sleep 1
	show_script_shutdown_msg
	exit 1
}

invalid_n_of_args() {
	echo "Invalid number of arguments has been provided. Please run the driver again with exactly one argument."
	echo ""
	show_script_shutdown_msg
	exit 1
}

invalid_arg() {
	echo "Argument $1 does not match either 'app' or 'test' value. Run the driver with one of the formentioned arguments."
	echo ""
	show_script_shutdown_msg
	exit 1
}

cleanup_after_tests() {
	echo $'The tests have been ran.\n'

	# Stop the DB server and cleanup the used containers
	echo $'Stopping the DB server after runnning...Waiting 10 seconds.'
	docker stop api_db
	sleep 10	# wait 10 second for the container to stop
	echo $'\nDB server stopped.\nRemoving the containers...'
	docker rm db_seeder
	docker rm api_db
	docker rm api
	echo $'\nContainers used during the testing have been removed.'
}


########################################
########   START OF THE SCRIPT	########
########################################
show_script_title

show_driver_startup_msg

# Check if app is already running
if [ "$(docker ps -aq -f status=running -f name=api)" ] && [ "$(docker ps -aq -f status=running -f name=api_db)" ]
then
	app_already_running
fi

# only 1 argument must be provided
if [ $# -ne 1 ]; then
    invalid_n_of_args
fi

# the argument must be either 'app' or 'test'
if [ "$1" = "test" ]
then
	echo $'The tests are chosen to be run.\n'
	sleep 5
	show_test_run_msg

	export DOCKERFILE="DockerfileTest.api"
	echo $'Setting up the containers before tests run...\n'
	docker-compose up -d --build --force-recreate --no-deps	
	docker-compose logs --tail=1000 -f api

	cleanup_after_tests
elif [ "$1" = "app" ]
then
	echo $'The application is chosen to be run.\n'
	sleep 5
	show_app_run_msg

	export DOCKERFILE="Dockerfile.api"
	echo $'Setting up the containers before running the application...\n'
	docker-compose up -d --build --force-recreate --no-deps
	docker-compose logs --tail=1000 -f api
else
    invalid_arg
fi

echo $'The script has finished executing!\n'
show_script_shutdown_msg
exit 0
