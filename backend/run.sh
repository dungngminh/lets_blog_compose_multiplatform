#!/bin/sh

# input dev or prod or contest
read -p "dev/prod/contest: " mode

if [ $mode == "dev" ]
then
    export $(egrep -v '^#' .env.local | xargs)
else if [ $mode == "prod" ]
    export $(egrep -v '^#' .env | xargs)
else
    export $(egrep -v '^#' .env.contest | xargs)
fi

# Export the environment variables
PGHOST=$PGHOST PGDATABASE=$PGDATABASE PGUSER=$PGUSER PGPASSWORD=$PGPASSWORD PGPORT=$PGPORT CLOUDINARY_APISECRET=$CLOUDINARY_APISECRET CLOUDINARY_APIKEY=$CLOUDINARY_APIKEY CLOUDINARY_CLOUDNAME=$CLOUDINARY_CLOUDNAME dart_frog dev