#!/bin/bash

# 1. Obtém os diretórios das entidades novas e velhas (models)

# 2. Obtém a lista de entidades velhas, sem as que foram excluídas
# Obs: entidades são strings

# 3.
# para cada entidade velha v:
#   n = nada ainda
#   se v estiver na lista de diferenças:
#     n = entidade nova
#   cc, n = entidade velha

#   # 3.1:
#   Obtém a lista de métodos da entidade velha, idem para a nova correspondente

    # 3.2
#   para m em metodos de v:
#     se v não está em lista de n:
#       use sed para colocar o método no final da classe nova correspondente

# 1.
olddir="$HOME/Documentos/spm_root/SPMServices/ejbModule/org/qrconsult/spm/model"
newdir='src/main/java/br/ufpa/labes/spm/domain'
diffs='util_files/entitydiffs.txt'

# 2.
models=`find "$olddir" -regextype posix-extended -regex '(.*).java' | sed -e '/policies/d' -e '/IPersistent/d' -e '/help/d' -e '/knowledge/d'`

# 3.
for m in $models; do
  new=''

  ename=`echo "$m" | cut -d/ -f13 | sed -r 's/(.*).java/\1/'`
  # package=`echo "$m" | cut -d/ -f12`
  seek=`cat "$diffs" | grep -w "$ename" | awk '{print $2}'`
  if [[ $seek != '' ]]; then
    new=$seek
  else
    new=$ename
  fi

  file="$newdir/$new.java"

  # 3.1
  old_methods=`sed -rn "s/public (.*) (.*)\((.*)\) \{/\r\2/p" "$m"`
  new_methods=`sed -rn "s/public (.*) (.*)\((.*)\) \{/\r\2/p" $file`

  # 3.2
  for m in $old_methods; do
    lookup=`echo $old_methods | grep -w "$ename"`
    if [[ $lookup != '' ]]; then
      # retrieve for method in old class
      input=`sed -n "/$lookup/, /}/ p" "$m"`

      # then add it to the new class
      sed -ri "s/\}$/`echo -e $test`\n\}/" $file
    fi
  done
done
