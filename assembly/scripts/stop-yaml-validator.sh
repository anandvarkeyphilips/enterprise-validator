#!/usr/bin/env bash
#Active Profile(YAML)
ACTIVE_PROFILE="preprod"
#Base Folder Path like "/folder/packages"
CURRENT_DIR=$(readlink -f "$0")
BASE_PACKAGE="${CURRENT_DIR%/bin/*}"
# Shell Script file name after removing path like "start-yaml-validator.sh"
SHELL_SCRIPT_FILE_NAME=$(basename -- "$0")
# Shell Script file name after removing extension like "start-yaml-validator"
SHELL_SCRIPT_FILE_NAME_WITHOUT_EXT="${SHELL_SCRIPT_FILE_NAME%.*}"
# App name after removing start/stop strings like "yaml-validator"
APP_NAME=${SHELL_SCRIPT_FILE_NAME_WITHOUT_EXT:5}

# Script to stop the application
PID_PATH="$BASE_PACKAGE/config/$APP_NAME/$APP_NAME.pid"

if [ ! -f "$PID_PATH" ]; then
   echo "Process Id FilePath($PID_PATH) Not found"
else
    PROCESS_ID=`cat $PID_PATH`
    if [ ! -e /proc/$PROCESS_ID -a /proc/$PROCESS_ID/exe ]; then
        echo "$APP_NAME was not running with PROCESS_ID:$PROCESS_ID.";
    else
        kill $PROCESS_ID;
        echo "Gracefully stopping $APP_NAME with PROCESS_ID:$PROCESS_ID..."
        sleep 5s
    fi
fi
PIDS=`/bin/ps aux |/bin/grep [j]ava.*-Dspring.profiles.active=$ACTIVE_PROFILE.*$APP_NAME.*jar | /bin/awk {'print $2'}`
if [ -z "$PIDS" ]; then
  echo "All instances of $APP_NAME with profile:$ACTIVE_PROFILE has has been successfully stopped now..." 1>&2
else
  for PROCESS_ID in $PIDS; do
    counter=1
    until [ $counter -gt 150 ]
        do
            if ps -p $PROCESS_ID > /dev/null; then
                echo "Waiting for the process($PROCESS_ID) to finish on it's own for $(( 300 - $(( $counter*5)) ))seconds..."
                sleep 2s
                ((counter++))
            else
                echo "$APP_NAME with PROCESS_ID:$PROCESS_ID is stopped now.."
                exit 0;
            fi
    done
    echo "Forcefully Killing $APP_NAME with PROCESS_ID:$PROCESS_ID."
    kill -9 $PROCESS_ID
  done
fi