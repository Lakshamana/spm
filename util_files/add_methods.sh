#!/bin/bash

# 1. Obtém os diretórios das entidades novas e velhas (models)

# 2. Obtém a lista de entidades velhas, sem as que foram excluídas
# Obs: entidades são strings

# 3.
para cada entidade velha v:
  n = nada ainda
  se v estiver na lista de diferenças:
    n = entidade nova
  cc, n = entidade velha

  # 3.1:
  Obtém a lista de métodos da entidade velha, idem para a nova correspondente

  para m em metodos de v:
    se v não está em lista de n:
      use sed para colocar o método no final da classe nova correspondente
