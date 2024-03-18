#!/bin/bash
#
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

BASE_DIR=$(dirname $0)/..

source "${BASE_DIR}"/bin/installer-env.sh
CONSOLE_OUTPUT_FILE="${LOG_DIR}/agent-out.log"

function help() {
  echo "Usage: agent.sh {status|start|stop|restart|clean}" >&2
  echo "       status:     the status of inlong agent"
  echo "       start:      start the inlong agent"
  echo "       stop:       stop the inlong agent"
  echo "       restart:    restart the inlong agent"
  echo "       clean:      unregister this node in manager"
  echo "       help:       get help from inlong agent"
}

function running() {
  process=$("$JPS" -l| grep "installer.Main" | grep -v grep)
  if [ "${process}" = "" ]; then
    return 1;
  else
    return 0;
  fi
}

# start installer
function start_installer() {
  if running; then
    echo "installer is running."
    exit 1
  fi
  nohup ${JAVA} ${INSTALLER_ARGS} org.apache.inlong.agent.installer.Main > /dev/null 2>&1 &
}

# stop installer
function stop_installer() {
  if ! running; then
    echo "installer is not running."
    exit 1
  fi
  count=0
  pid=$("$JPS" -l| grep "installer.Main"| grep -v grep | awk '{print $1}')
  while running;
  do
    (( count++ ))
    echo "Stopping installer $count times"
    if [ "${count}" -gt 10 ]; then
        echo "kill -9 $pid"
        kill -9 "${pid}"
    else
        kill "${pid}"
    fi
    sleep 6;
  done
  echo "Stop installer successfully."
}

# get status of installer
function status_installer() {
  if running; then
    echo "installer is running."
    exit 0
  else
    echo "installer is not running."
    exit 1
  fi
}

# clean installer
function clean_installer() {
  if running; then
    echo "installer is running. please stop it first."
    exit 0
  fi
  ${JAVA} ${INSTALLER_ARGS} org.apache.inlong.agent.core.HeartbeatManager
}

function help_installer() {
  ${JAVA} ${INSTALLER_ARGS} org.apache.inlong.agent.installer.Main -h
}

command=$1
shift 1
case $command in
  status)
    status_installer $@;
    ;;
  start)
    start_installer $@;
    ;;
  stop)
    stop_installer $@;
    ;;
  restart)
    $0 stop $@
    $0 start $@
    ;;
  clean)
    clean_installer $@;
    ;;
  help)
    help_installer;
    ;;
  *)
    help;
    exit 1;
    ;;
esac
