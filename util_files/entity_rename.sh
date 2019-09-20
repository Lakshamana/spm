#!/bin/bash

for file in `find . -regextype posix-extended -regex '(.*)DAO.java'`; do
  echo $file
  replace=`echo $file | sed -r 's/(.*)DAO.java/\1RepositoryQuery.java/'`
  mv $file $replace
done

echo 'Finish renaming entity...'
