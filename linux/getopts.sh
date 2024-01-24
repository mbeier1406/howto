#!/bin/bash

# Demonstriert die Anwendung von getopts
# Beispiel: $ ./getopts.sh -s -vl 3 xx
VERBOSE=0
SPEC=0
LEN=0

echo "${0}: anz=${#} (${@})"
while getopts "vsl:" OPTION; do
  case ${OPTION} in
    v) VERBOSE=1; ;;
    s) SPEC=1; ;;
    l) LEN=${OPTARG} ;;
    ?) exit 1; ;;
  esac
done
shift $((OPTIND-1))
echo "${0}: anz=${#} (${@}): VERBOSE=${VERBOSE}; SPEC=${SPEC}; LEN=${LEN}"

