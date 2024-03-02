#!/usr/bin/env bash

# -----------------------------------------------------------------------------
# Environment Variable Prerequisites
#
#   DRISK_HOME      Point to the drisk-drllab home directory.
#
#   DRISK_ENV       Should be DEV/CI/SIT/UAT/STR/PRE/PRD.
#
#   JAVA_HOME       Must point at your Java Development Kit installation.
#
#   JAVA_OPTS       (Optional) Java runtime options used when any command
#                   is executed.
# -----------------------------------------------------------------------------

# Main class of drisk-realtime-drllab
DRISK_MAIN="com.roy.drisk.drlLab.DRiskDrlLab"

# Check environment variables
if [ -z "${DRISK_HOME}" ]; then
  DRISK_HOME="$(cd `dirname $0`;cd ..;pwd)"
fi
if [ -z "${DRISK_ENV}" ]; then
  echo "DRISK_ENV environment variable not defined, value should be DEV/CI/SIT/UAT/STR/PRE/PRD."
  exit 1
fi
if [ -z "${JAVA_HOME}" ]; then
  JAVA_PATH=`which java 2>/dev/null`
  if [ "x${JAVA_PATH}" != "x" ]; then
    JAVA_HOME=`dirname $(dirname ${JAVA_PATH}) 2>/dev/null`
  fi
  if [ -z "${JAVA_HOME}" ]; then
    echo "JAVA_HOME environment variable not defined."
    exit 1
  fi
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
if [ -f "${PID_FILE}" ]; then
  EXIST_PID=`cat "${PID_FILE}"`
  num=`ps -p "${EXIST_PID}" | grep "${EXIST_PID}" | wc -l`
  if [ ${num} -ge 1 ]; then
    echo "Can't start drisk drllab, an existing server[${EXIST_PID}] is running."
    exit 1
  fi
fi

# Backup previous logs
LOG_DIR="${DRISK_HOME}/logs/${PORT}"
BACK_DIR="${DRISK_HOME}/backup/${PORT}"
if [ ! -d "${BACK_DIR}" ]; then
  mkdir -p "${BACK_DIR}"
fi
TS=`date +%Y%m%d%H%M%S`
if [ -d "${LOG_DIR}" ]; then
  mv "${LOG_DIR}" "${BACK_DIR}/${TS}"
fi

# Log files
`mkdir -p "${LOG_DIR}"`
OUT_FILE="${LOG_DIR}/server.out"
ERR_FILE="${LOG_DIR}/server.err"
GC_FILE="${LOG_DIR}/server-gc.log"

# Set options for server starting
MEM_SIZE_MB="512"
MEM_OPTS="-Xms${MEM_SIZE_MB}m -Xmx${MEM_SIZE_MB}m"
GC_OPTS="-XX:+UseG1GC -XX:MaxGCPauseMillis=50 -verbose:gc -Xloggc:${GC_FILE} -XX:+PrintGCDateStamps -XX:+PrintGCDetails"
DEBUG_OPTS="-Xverify:none -XX:+HeapDumpOnOutOfMemoryError -XX:+UnlockCommercialFeatures -XX:+FlightRecorder"
DEBUG_OPTS="-XX:AutoBoxCacheMax=10000 -XX:+PrintCommandLineFlags ${DEBUG_OPTS}"
JAVA_OPTS="${JAVA_OPTS} ${MEM_OPTS} ${DEBUG_OPTS} ${GC_OPTS}"

CLASSPATH="."
LIB_DIR="${DRISK_HOME}/lib"
if [ -d "$LIB_DIR" ]; then
  for f in `ls ${LIB_DIR}/*.jar | grep -v drisk-rules-0`
  do
    CLASSPATH="${CLASSPATH}:${f}"
  done
fi

DRISK_OPTS="-Ddrisk.env=${DRISK_ENV} -Ddrisk.home=${DRISK_HOME} -Ddrisk.log.dir=${LOG_DIR}"
SPRING_OPTS="--spring.config.location=file:${DRISK_CONFIG}"

echo "--------------------------------------------------"
echo "Starting DRisk DrlLab Server"
echo "--------------------------------------------------"
echo "DRISK_HOME   : ${DRISK_HOME}"
echo "DRISK_ENV    : ${DRISK_ENV}"
echo "DRISK_MAIN   : ${DRISK_MAIN}"
echo "DRISK_CONFIG : ${DRISK_CONFIG}"
echo "JAVA_HOME    : ${JAVA_HOME}"
echo "JAVA_OPTS    : ${JAVA_OPTS}"
echo "--------------------------------------------------"

# Start server
RUN_CMD="${JAVA_HOME}/bin/java ${JAVA_OPTS} -cp ${CLASSPATH} ${DRISK_OPTS} ${DRISK_MAIN} ${SPRING_OPTS}"
echo "Ready to run DRisk Server with command: " >${OUT_FILE}
echo "${RUN_CMD}" >>${OUT_FILE}
nohup ${RUN_CMD} >>${OUT_FILE} 2>${ERR_FILE} &

# Save PID file
PID=$!
echo ${PID} >"${PID_FILE}"

# Waiting for server starting
echo -n "Waiting for server[${PID}] at port[${PORT}] to start."
start_sec=0
max_sec=30
while [ ${start_sec} -lt ${max_sec} ] ; do
  num=`netstat -an | grep -w "${PORT}" | grep -w "LISTEN" | wc -l`
  if [ ${num} -ge 1 ]; then
    echo
    exit 0
  fi
  echo -n "."
  min=`expr ${start_sec} + 1`
  sleep 1
done
echo "Server did not started in ${max_sec} seconds, please check log files."
exit 1
