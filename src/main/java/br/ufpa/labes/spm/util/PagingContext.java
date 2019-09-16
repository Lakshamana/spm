package br.ufpa.labes.spm.util;

public class PagingContext {

  private int pageNumber = 1;
  private int resultsPerPage = 10;

  public PagingContext(int pageNumber, int resultsPerPage) {
    this.pageNumber = pageNumber;
    this.resultsPerPage = resultsPerPage;
  }

  public int getPageNumber() {
    return pageNumber;
  }

  public int getResultsPerPage() {
    return resultsPerPage;
  }
}
