#!/bin/bash

for file in `find . -regextype posix-extended -regex '.*[Pp]lain[_]*[Aa]ctivity.*'`; do
  echo $file
  replace=`echo $file | sed -r 's/(.*)([Pp]lain)[_]*[Aa]ctivity(.*)/\1\2\3/'`
  mv $file $replace
done

echo 'Finish renaming entity...'
