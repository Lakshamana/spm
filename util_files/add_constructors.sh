#!/bin/bash

# 1.
olddir="$HOME/Documentos/spm_root/SPMServices/ejbModule/org/qrconsult/spm/model"
newdir='src/main/java/br/ufpa/labes/spm/domain'
diffs='util_files/entitydiffs.txt'

# 2.
# models=`find "$olddir" -regextype posix-extended -regex '(.*).java' | sed -e '/policies/d' -e '/IPersistent/d' -e '/help/d' -e '/knowledge/d'`
models="$olddir/agent/AgentPlaysRole.java $olddir/agent/AgentHasAbility.java $olddir/agent/AgentAffinityAgent.java"

# 3.
for m in $models; do
  new=''
  ename=`echo "$m" | cut -d/ -f13 | sed -r 's/(.*).java/\1/'`
  seek=`cat "$diffs" | grep -w "$ename" | awk '{print $2}'`

  if [[ $seek != '' ]]; then
    new=$seek
  else
    new=$ename
  fi

  file="$newdir/$new.java"
  echo entity: $m

  if test -f $file; then
    # 3.1
    constructors=`sed -rn "s/public $ename([ ]*\((.*)\).*)/public "$ename"\1;/gp" "$m" | tr ' ' '_'`
    # echo olds: $constructors

    # 3.2
    if [[ $constructors != '' ]]; then
      inputs=''
      list=`echo "$constructors" | tr ';' "\n"`
      for mthd in $list; do
        method=`echo $mthd | tr '_' ' '`
        # echo method: $method
        selfclose=`echo $method | grep {}`
        if [[ $selfclose != '' ]]; then
          inputs+="\t$method\n\n"
        else
          # retrieve for method in old class
          input=`sed -n "/$method/, /^\t}/p" "$m"`
          # then add it to the new class
          inputs+="$input\n"
        fi
      done

      # add input methods to new class
      # echo -e "$inputs"
      trigger='jhipster-needle-entity-add-field'
      echo -e "$(node util_files/insert.js "$inputs" "$file" $trigger)" > $file
      # sed -i "s/^}$//" $file
      # echo -e "$inputs" >> $file
      # echo -e '}' >> $file
    fi
  fi
done
