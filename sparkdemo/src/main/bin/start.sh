#!/usr/bin/env bash

# Execution directory
_bindir=`dirname $0`
_basedir=`cd ${_bindir}/.. && pwd`

LIB="${_basedir}/lib/*"

# java options
JAVA_OPTS="-server -Xms128m -Xmx512m"
SOURCE="${_basedir}/config/application-test.properties"
RESULT_DIR="${_basedir}/dump"

java ${JAVA_OPTS} -cp "${LIB}" -Dspark.master=local com.example.demo.WordCount "$SOURCE" "$RESULT_DIR"