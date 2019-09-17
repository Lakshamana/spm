#!/bin/bash
utildir="`readlink -m .`/util_files"
cd $HOME/src/main/java/br/ufpa/labes/spm/repository
classes=` find . -regextype posix-extended -regex '(.*)DAO.java'`

for c in $classes; do
  class_name=`echo "$c" | cut -d'/' -f4 | sed -r 's/(.*).java/\1/'`

  # Add @Inheritance hibernate annotation
  sed -ri "/public class $c/ s/(public class .*)/@Inheritance(strategy=InheritanceType.JOINED)\n\1/" $class_path
  sed -ri "/package/ s/(package .*)/\1\n\nimport javax.persistence.Inheritance;\nimport javax.persistence.InheritanceType;\n/" $class_path

  # Add 'extends' to subclasses
  subclasses=`cat "$utildir/subclasses.txt" | grep -w "$c" | awk '{print $2}' | sed -r "/^$c$/d"`
  for s in $subclasses; do
    # record=`echo getThe"$c"Super "$s"`
    # echo $record
    # echo $record >> "$utildir/subclasses.txt"
    sub_path="$s.java"
    sed -ri "/public class/ s/public class $s (.*)/public class $s extends $c \1/" $sub_path
  done
done

echo 'Finish adding inheritance...'
