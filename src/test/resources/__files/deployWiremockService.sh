#!/bin/bash
# Deploys a wiremock service from the user's input

shopt -s dotglob
shopt -s nullglob

if [ -z $WIREMOCK_HOME ]; then
	echo You need to set the environment variable WIREMOCK_HOME
	echo WIREMOCK_HOME=path_to_wiremock
	echo export WIREMOCK_HOME
	exit 1
fi

echo Please select which service your like to deploy an endpoint for
appDirs=(*/)

select appDir in "${appDirs[@]}"; do echo "You selected the ${appDir} service."; break; done

echo
echo Now select which endpoint you would like to deploy for ${appDir}.
echo

endpointDirs=(${appDir}*)

select endpointDir in "${endpointDirs[@]}"; do echo "You selected the ${endpointDir} endpoint, please select the endpoint variant."; break; done

endpoints=(${endpointDir}/*)

select endpoint in "${endpoints[@]}"; do echo "You selected the ${endpoint} endpoint"; break; done

echo Copying files to Wiremock

serviceName=$(basename ${endpointDir})
echo $serviceName
echo $endpoint

if [ -f ${endpoint}/mapping.json ]; then
	echo mapping.json exists - copying to wiremock mapping directory
	cp -rv ${endpoint}/mapping.json $WIREMOCK_HOME/mappings/$serviceName.json
else
	echo THERE IS NO MAPPING FILE FOR ${endpoint}/mapping.json - PLEASE CHECK FOLDERS
fi

if [ -f ${endpoint}/file.json ]; then
	echo file.json exists - copying to wiremock __files directory
	cp -rv ${endpoint}/file.json $WIREMOCK_HOME/__files/$serviceName.json
else
	echo THERE IS NO JSON FILE FOR ${endpoint}/file.json - PLEASE CHECK FOLDERS
fi

echo Done


