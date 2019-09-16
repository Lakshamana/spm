package br.ufpa.labes.spm.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Criteria {
  EnumCriteriaType type();
}
