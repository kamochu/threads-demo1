#!/bin/sh 

echo "Pulling latest changes" 
git pull 

echo "Packaging.." 

cd ..

mvn clean package

echo "Done packaging" 

cd scripts

exit 0; 
