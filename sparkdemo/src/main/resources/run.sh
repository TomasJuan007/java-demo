#!/usr/bin/env bash

# Execution directory
_bindir=`dirname $0`
_basedir=`cd ${_bindir}/.. && pwd`

LIB="${_basedir}/lib/*"

# java options
JAVA_OPTS="-server -Xms128m -Xmx256m"
JAVA_ARGS="sparkdemo/src/main/resources/run.sh sparkdemo/src/main/resources/dump"

java ${JAVA_OPTS} -cp "${LIB}" -Dspark.master=local com.example.demo.WordCount JAVA_ARGS "$@"