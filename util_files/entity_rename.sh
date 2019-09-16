#!/bin/bash

for file in `find . -regextype posix-extended -regex '.*(Join|Branch)(AND)*([^(Con)]*)(.*)DAO.java'`; do
  echo $file
  replace=`echo $file | sed -r 's/(.*)(Join|Branch)(AND)*([^(Con)]*)(.*)DAO.java/\1\2\3Con\5DAO.java/'`
  mv $file $replace
done

echo 'Finish renaming entity...'
