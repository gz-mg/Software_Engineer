package com.edu.hzau.cocs.fe.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author yue
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SwineMicrobeGeneKeggRes implements Cloneable{
    private int swineIndex;
    private int microbeId;
    private String microbeName;
    private int ncbiGeneId;
    private String geneSymbol;
    private int geneKeggIndex;
    private String geneKeggPathway;

    @Override
    public SwineMicrobeGeneKeggRes clone() {
        try {
            return (SwineMicrobeGeneKeggRes) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
