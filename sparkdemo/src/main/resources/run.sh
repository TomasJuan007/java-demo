#!/usr/bin/env bash

# Execution directory
_bindir=`dirname $0`
_basedir=`cd ${_bindir}/.. && pwd`

LIB="${_basedir}/dependency/*"

# java options
JAVA_OPTS="-server -Xms128m -Xmx256m"
JAVA_ARGS="${_basedir}/classes/run.sh" "${_basedir}/dump"

java ${JAVA_OPTS} -cp "${LIB}" -Dspark.master=local com.example.demo.WordCount ${JAVA_ARGS}