#!/bin/sh

# kill docker process 
if [ `ps -ef | grep java | grep -v grep | grep "threads-demo" |wc -l` -eq 1 ]
then
        echo "Killing the old threads-demo service - docker instance"

        ps -ef | grep "dcc-gateway" | grep -v grep  | awk '{ print $2 }' | xargs kill -9

        echo "Killed the old threads-demo process"
fi

if [ `ps -ef | grep java | grep -v grep | grep "threads-demo" |wc -l` -eq 1 ]
then
	echo "threads-demo is already running. "
else
  echo "threads-demo service starting. Please wait..."

	export APP_HOME=..
	export CONFIG_DIR=$APP_HOME/configs
	export LIB_DIR=$APP_HOME/target/
	export LOG4J_CONF=$CONFIG_DIR/log4j.properties
	export RUN_LOG=/logs/adp/engine_run.log

	export CLASSPATH=$CONFIG_DIR:$LIB_DIR

	for i in `find $LIB_DIR -type f -name *.jar`; do
			CLASSPATH=$i:$CLASSPATH
	done

	export CONFIG_ARGS="-Dhello=World "
	export JVM_ARGS="$CONFIG_ARGS -Dlog4j.configuration=$LOG4J_CONF -Djava.library.path=$LIB_DIR -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=5601 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false  -Djava.net.preferIPv4Stack=true -XX:+UseParallelGC -XX:+AggressiveOpts -XX:+UseFastAccessorMethods -Xms128M -Xmx2048M"
	export MAIN_ARGS="threads-demo service > $RUN_LOG &"

	java $JVM_ARGS -classpath $CLASSPATH tech.meliora.natujenge.threads.ThreadApp $MAIN_ARGS&
	
	echo "threads-demo started. "
fi
exit 0;
