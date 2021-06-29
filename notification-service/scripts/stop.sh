#!/bin/sh

if [ `ps -ef | grep java | grep -v grep | grep "threads-demo" |wc -l` -eq 1 ]
	then
		echo "Stopping threads-demo service..."
		sleep 2;
		ps -ef | grep java | grep -v grep | grep "threads-demo" | awk {'print$2'}| xargs kill
		echo "Stopped threads-demo service"
	else 
		echo 'threads-demo is not running '
fi 

exit 0; 
