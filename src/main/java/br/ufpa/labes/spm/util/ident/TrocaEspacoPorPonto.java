package br.ufpa.labes.spm.util.ident;

public class TrocaEspacoPorPonto implements MetodoDeConversao {

  @Override
  public String gerarIdent(String stringParaConverter) {
    stringParaConverter = stringParaConverter.replaceAll(" ", ".");
    return stringParaConverter;
  }
}
