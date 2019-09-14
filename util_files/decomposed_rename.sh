#!/bin/bash

for file in `find . -regextype posix-extended -regex '.*[Dd]ecomposed[_]*[Aa]ctivity.*'`; do
  echo $file
  replace=`echo $file | sed -r 's/(.*)([Dd]ecomposed)[_]*[Aa]ctivity(.*)/\2\3/'`
  mv $file $replace
done

echo 'Finish renaming DecomposedActivity to Decomposed entity...'