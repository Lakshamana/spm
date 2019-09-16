#!/bin/bash
cd $HOME/Documentos/spm_root/SPMServices/ejbModule/org/qrconsult/spm
classes=`grep -iRl '@Inheritance(strategy=InheritanceType.JOINED)' | sed '/policies/d' | cut -d'/' -f3 | sed -r 's/(.*).java/\1/'`

cd $HOME/git/spm/
model_path='src/main/java/br/ufpa/labes/spm/domain'
cd $model_path

for c in $classes; do
  class_path="$c.java"

  # Add @Inheritance hibernate annotation
  sed -ri "/public class $c/ s/(public class .*)/@Inheritance(strategy=InheritanceType.JOINED)\n\1/" $class_path
  sed -ri "/package/ s/(package .*)/\1\n\nimport javax.persistence.Inheritance;\nimport javax.persistence.InheritanceType;\n/" $class_path

  # Add 'extends' to subclasses
  subclasses=`grep -iRl getThe"$c"Super | sed -r 's/(.*).java/\1/'`
  for s in $subclasses; do
    sub_path="$s.java"
    sed -ri "/public class/ s/public class $s (.*)/public class $s extends $c \1/" $sub_path
  done
done

echo 'Finish adding inheritance...'
