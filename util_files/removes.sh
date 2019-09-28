#!/bin/bash

justneed='Resource Artifact Agent Group Activity'

for file in *ServicesImpl.java; do
	ename=`echo "$file" | sed -r 's/(.*)ServicesImpl.java/\1/'`
	seek=`echo "$justneed" | grep "$ename"`
  if [[ $seek == '' ]]; then
    rm $file
  fi
done
