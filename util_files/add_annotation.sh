#!/bin/bash
utildir="`readlink -m .`/util_files"
cd src/main/java/br/ufpa/labes/spm/repository
classes=`find . -regextype posix-extended -regex '(.*)Repository.java' | cut -d'/' -f2 | sed -r 's/(.*)Repository.java/\1/'`

for c in $classes; do
  class_name=`echo "$c" | cut -d'/' -f4 | sed -r 's/(.*).java/\1/'`

  # Add DAO inheritance
  super=`echo I"$c"DAO`
  sed -ri "/public class/ s/public class $c (.*)/public class $c extends $super \1/" $class_path
done

echo 'Finish adding inheritance...'
