package br.ufpa.labes.spm.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Criptografa em MD5
 *
 * @author Bruno
 */
public class Md5 {

  /** Metodo construtor padrao */
  public Md5() {}

  /**
   * Metodo criptografador
   *
   * @param input String a ser criptografada
   * @return String criptografada
   */
  public static String getMd5Digest(String input) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] messageDigest = md.digest(input.getBytes());
      BigInteger number = new BigInteger(1, messageDigest);
      return number.toString(16);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  public static void main(String args[]) {
    System.out.println(Md5.getMd5Digest("123456"));
  }
}
