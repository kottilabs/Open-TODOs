#!/bin/sh
set -e TODO_OPTS=$TODO_OPTS
java $TODO_OPTS -Duser.timezone=UTC -jar app.jar