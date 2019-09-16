package org.qrconsult.spm.util.ident;

public class ConversorDeIdent {

  private String stringParaConverter;

  private ConversorDeIdent(String stringParaConverter) {
    this.stringParaConverter = stringParaConverter.toLowerCase();
  }

  public static ConversorDeIdent de(String stringParaConverter) {
    return new ConversorDeIdent(stringParaConverter);
  }

  public ConversorDeIdent para(MetodoDeConversao metodo) {
    stringParaConverter = metodo.gerarIdent(stringParaConverter);
    return this;
  }

  public String ident() {
    return stringParaConverter;
  }
}
