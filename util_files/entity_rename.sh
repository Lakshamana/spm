#!/bin/bash

for file in `find . -regextype posix-extended -regex '.*[Aa]utomatic[_]*[Aa]ctivity.*'`; do
  echo $file
  replace=`echo $file | sed -r 's/(.*)([Aa]utomatic)[_]*[Aa]ctivity(.*)/\1\2\3/'`
  mv $file $replace
done

echo 'Finish renaming entity...'
