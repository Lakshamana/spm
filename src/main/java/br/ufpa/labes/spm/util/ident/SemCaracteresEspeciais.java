package org.qrconsult.spm.util.ident;

import java.text.Normalizer;

public class SemCaracteresEspeciais implements MetodoDeConversao {

  @Override
  public String gerarIdent(String stringParaConverter) {
    stringParaConverter = Normalizer.normalize(stringParaConverter, Normalizer.Form.NFD);
    stringParaConverter = stringParaConverter.replaceAll("[^a-zA-Z0-9 ]", "");
    stringParaConverter = stringParaConverter.replaceAll("\\b\\w{2}\\b", "");
    stringParaConverter = stringParaConverter.replaceAll("  ", " ");
    return stringParaConverter;
  }

  // Exemplo
  public static void main(String[] args) {
    String ok =
        ConversorDeIdent.de("1752 Plano de AÃ§Ã£o1").para(new SemCaracteresEspeciais()).ident();
    System.out.println(ok);

    String ok2 =
        ConversorDeIdent.de("872.Template - Plano de Gerência de Documentos e Plano de Comunicação")
            .para(new SemCaracteresEspeciais())
            .ident();
    System.out.println(ok2);
  }
}
