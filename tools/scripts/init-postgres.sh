#!/bin/bash

if [ -z $POSTGRES_HOME ]; then
    echo "System variable POSTGRES_HOME has not been set"
    exit 1
fi

PSQL=$POSTGRES_HOME/bin/psql

$PSQL -U postgres -f ../../core/dal/src/main/resources/sql/1.0/postgres/create_database.sql
result=$?
if [ $result -ne 0 ]
then
   echo "Create Database script unsuccessful: " $result
   exit $result
fi

temp_dir="generated"
if [ ! -d "$temp_dir" ]
then
    mkdir "$temp_dir"
fi

export PGPASSWORD=jewelry

generate-sql.sh $temp_dir jewelry
$PSQL -U jewelry -f $temp_dir/jewelry.sql
result=$?
if [ $result -ne 0 ]
then
   echo "Schema recreation script unsuccessful: " $result
   exit $result
fi
rm $temp_dir/jewelry.sql

java -jar tools/db-init/target/jewelry-db-init.jar

generate-sql.sh $temp_dir test
$PSQL -U jewelry -f $temp_dir/test.sql
result=$?
if [ $result -ne 0 ]
then
   echo "Schema recreation script unsuccessful: " $result
   exit $result
fi
rm $temp_dir/test.sql
rmdir "$temp_dir"