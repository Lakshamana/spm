#!/bin/bash

for file in `find . -regextype posix-extended -regex '.*[I]*[Req]*GroupDAO.java'`; do
  echo $file
  replace=`echo $file | sed -r 's/(.*[I]*[Req]*)(GroupDAO.java)/\1Work\2/'`
  mv $file $replace
done

echo 'Finish renaming entity...'
