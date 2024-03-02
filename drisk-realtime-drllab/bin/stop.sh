#!/usr/bin/env bash

# -----------------------------------------------------------------------------
# Environment Variable Prerequisites
#
#   DRISK_HOME      Point to the drisk-realtime-server home directory.
#
#   PID_FILE        Server pid file name when running.
# -----------------------------------------------------------------------------

# Check environment variables
if [ -z "${DRISK_HOME}" ]; then
  DRISK_HOME="$(cd `dirname $0`;cd ..;pwd)"
fi
if [ -z "${DRISK_ENV}" ]; then
  echo "DRISK_ENV environment variable not defined, value should be DEV/CI/SIT/UAT/PRD."
  exit 1
fi

# Check config file
DRISK_CONFIG_FILE=$1
if [ -z "${DRISK_CONFIG_FILE}" ]; then
  DRISK_CONFIG_ENV=$(echo ${DRISK_ENV} | tr '[A-Z]' '[a-z]')
  DRISK_CONFIG_FILE="application-${DRISK_CONFIG_ENV}.properties"
fi
DRISK_CONFIG="${DRISK_HOME}/config/${DRISK_CONFIG_FILE}"
if [ ! -f "${DRISK_CONFIG}" ]; then
  echo "Config file ${DRISK_CONFIG} not found."
  exit 1
fi

# Check server port, default is 9877
PORT=`grep "server.port" "${DRISK_CONFIG}" | cut -d "=" -f 2 | tr -d "\r"`
if [ -z "${PORT}" ]; then
  PORT=9888
fi

# Set PID file
PID_FILE="${DRISK_HOME}/${PORT}.pid"

# Check if server is running
if [ ! -f "${PID_FILE}" ]; then
  echo "PID file ${PID_FILE} not found. May the server is not running."
  exit 1
fi

PID=`cat "${PID_FILE}"`

echo "--------------------------------------------------"
echo "Stoping DRisk Server"
echo "--------------------------------------------------"
echo "DRISK_HOME   : ${DRISK_HOME}"
echo "DRISK_CONFIG : ${DRISK_CONFIG}"
echo "PID          : ${PID}"
echo "--------------------------------------------------"

# Stop server
`kill "${PID}"`
if [ $? -eq 1 ]; then
  exit 1
fi

echo -n "Waiting for server to stop."
while [ 1 ] ; do
  num=`ps -p "${PID}" | grep "${PID}" | wc -l`
  if [ ${num} -eq 0 ]; then
    break
  fi
  echo -n "."
  sleep 1
done
echo

`rm "${PID_FILE}"`
