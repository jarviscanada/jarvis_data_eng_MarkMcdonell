#!/bin/bash
lscpu_out=$(lscpu)
export lscpu_out
function specs(){
  echo "$lscpu_out" | grep -E "$*"| awk 'BEGIN { FS = ":"} ;{print $2}' | xargs
  }
exit 0
