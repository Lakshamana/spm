#!/bin/bash

for file in `find . -regextype posix-extended -regex '.*/([I]*)([^(Work)]*)(Group)(.*)DAO.java'`; do
  echo $file
  replace=`echo $file | sed -r 's/(.*)\/([I]*)([^(Work)]*)(Group)(.*)DAO.java/\1\/\2\3Work\4\5DAO.java/'`
  mv $file $replace
done

echo 'Finish renaming entity...'
