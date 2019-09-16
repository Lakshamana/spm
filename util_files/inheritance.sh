#!/bin/bash
utildir="`readlink -m .`/util_files"
cd $HOME/Documentos/spm_root/SPMServices/ejbModule/org/qrconsult/spm
classes=`grep -iRl '@Inheritance(strategy=InheritanceType.JOINED)' | sed '/policies/d' | cut -d'/' -f3 | sed -r 's/(.*).java/\1/'`

echo "current: $currentdir"

cd $HOME/git/spm/
model_path='src/main/java/br/ufpa/labes/spm/domain'
cd $model_path

for c in $classes; do

  if [[ $c == 'Branch' ]]; then
    c='BranchCon'
  elif [[ $c == 'Join' ]]; then
    c='JoinCon'
  elif [[ $c == 'PeopleInstantiationSuggestion' ]]; then
    c='PeopleInstSug'
  elif [[ $c == 'Parameters' ]]; then
   c='Parameter'
  fi

  class_path="$c.java"

  # Add @Inheritance hibernate annotation
  sed -ri "/public class $c/ s/(public class .*)/@Inheritance(strategy=InheritanceType.JOINED)\n\1/" $class_path
  sed -ri "/package/ s/(package .*)/\1\n\nimport javax.persistence.Inheritance;\nimport javax.persistence.InheritanceType;\n/" $class_path

  # Add 'extends' to subclasses
  subclasses=`cat "$utildir/subclasses.txt" | grep "$c" | awk '{print $2}'`
  for s in $subclasses; do
    record=`echo getThe"$c"Super "$s"`
    # echo $record
    # echo $record >> "$utildir/subclasses.txt"
    sub_path="$s.java"
    sed -ri "/public class/ s/public class $s (.*)/public class $s extends $c \1/" $sub_path
  done
done

echo 'Finish adding inheritance...'
