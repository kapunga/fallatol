#!/bin/bash

set -o nounset
set -o errexit

set -o xtrace

sbt +scalafmtAll; sbt +scalafixAll; sbt laikaSite; sbt headerCheckAll

MODIFIED=$(git ls-files --modified --exclude-standard)
if [[ -n "$MODIFIED" ]]; then
  echo "ERROR: Formatting and publish changed some files."
  echo "$MODIFIED"
  exit 1
fi
