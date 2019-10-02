package br.ufpa.labes.spm.service.dto;

@SuppressWarnings("serial")
public class DecomposedDTO extends ActivityDTO {

  private ActivitysDTO normals;
  private DecomposedDTO decomposed;

  public DecomposedDTO() {}

  public DecomposedDTO(ActivitysDTO activitysDTO) {
    this.normals = activitysDTO;
  }

  public DecomposedDTO(ActivitysDTO normals, DecomposedDTO decomposed) {
    this.normals = normals;
    this.decomposed = decomposed;
  }

  public ActivitysDTO getNormals() {
    return normals;
  }

  public void setNormals(ActivitysDTO normals) {
    this.normals = normals;
  }

  public DecomposedDTO getDecomposed() {
    return decomposed;
  }

  public void setDecomposed(DecomposedDTO decomposed) {
    this.decomposed = decomposed;
  }

  @Override
  public String toString() {
    return "[ident: " + this.getIdent() + "\n" + this.normals.getActivitys() + " ]";
  }
}
