#!/bin/bash

# move to dir
dir=$1
cd dir

# remove ejb import and annotation
for file in `find . -iname *DAO.java`; do
  str=`sed -rn "s/import javax\.ejb\.(.*);/\1/gp" $file`
  sed -i "/$str/d" $file
done

echo 'Finish javax.ejb.* annotations removes...'