#!/bin/bash

for file in `find . -regextype posix-extended -regex '.*Tag[Ss]tat[^s]*'`; do
  echo $file
  replace=`echo $file | sed -r 's/(.*)(Tag[Ss]tat)(.*)/\1\2s\3/'`
  mv $file $replace
done

echo 'Finish renaming entity...'
