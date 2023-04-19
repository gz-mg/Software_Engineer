package com.edu.hzau.cocs.fe.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auther myCui
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisDrugTarGenePath implements Cloneable{
    private int orphanId;
    private String orphanName;
    private String drugId;
    private String drugName;
    private String targetId;
    private String targetName;
    private String geneId;
    private String geneSymbol;
    private String pathId;
    private String pathName;
    private String pathGraphUrl;
 @Override
  public DisDrugTarGenePath clone() {
    try {
      return (DisDrugTarGenePath) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
  }
}
