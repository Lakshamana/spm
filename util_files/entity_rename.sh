#!/bin/bash

for file in `find . -regextype posix-extended -regex '.*([I]*)ToolParametersDAO.java'`; do
  echo $file
  replace=`echo $file | sed -r 's/(.*)([I]*)ToolParametersDAO.java/\1\2ToolParameterDAO.java/'`
  mv $file $replace
done

echo 'Finish renaming entity...'
