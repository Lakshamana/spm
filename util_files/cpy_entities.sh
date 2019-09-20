#!/bin/bash

olddir="$HOME/Documentos/spm_root/SPMDTO/src/org/qrconsult/spm/dtos/"
newdir='src/main/java/br/ufpa/labes/spm/service/dto'
diffs='util_files/entitydiffs.txt'
models=`find "$olddir" -regextype posix-extended -regex '(.*)DTO.java' | sed -e '/policies/d' -e '/help/d' -e '/knowledge/d'`

for m in $models; do
  ename=`echo "$m" | cut -d/ -f13 | sed -r 's/(.*).java/\1/'`
  # package=`echo "$m" | cut -d/ -f12`
  # seek=`cat "$diffs" | grep -w "$ename" | awk '{print $2}'`

  target=$ename
  # if [[ $seek != '' ]]; then
  #   target=$seek
  # fi
  cp $m "$newdir/$target.java"
  # echo $target
  # echo 'cp' $m "$newdir/$target.java"
done
