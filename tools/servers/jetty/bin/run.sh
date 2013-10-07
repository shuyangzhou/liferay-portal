#!/bin/sh

if [ ! $JAVA_HOME ]
then
	echo JAVA_HOME not defined.
	exit
fi

export JAVA_OPTS="-Dfile.encoding=UTF8 -Djava.net.preferIPv4Stack=true ${java.security.manager.option} -Djava.security.policy==${app.server.jetty.dir}/lib/policy/server.policy -Djetty.home=${app.server.jetty.dir} -Duser.timezone=GMT -Xmx1024m -XX:MaxPermSize=256m"

$JAVA_HOME/bin/java $JAVA_OPTS -jar ../start.jar