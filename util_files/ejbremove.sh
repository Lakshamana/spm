#!/bin/bash

# move to dir
dir="`pwd`/src/main/java/br/ufpa/labes/spm/service"
cd `echo $dir`

# remove ejb import and annotation
for file in `find . -iname *Services*.java`; do
  echo $file
  str=`sed -rn "s/import javax\.ejb\.(.*);/\1/gp" $file`
  echo $str
  if [[ $str ]]; then
    sed -i "/$str/d" $file
  fi
done

echo 'Finish javax.ejb.* annotations removes...'
