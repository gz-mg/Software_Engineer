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
public class SwineMetabolismHmdbRes implements Cloneable{
    private int swineIndex;
    private int metabolismIndex;
    private String metabolismName;
    private int hmdbInfoIndex;
    private String metabolismHmdbInfoIndex;
    private String hmdbPathway;
    private String hmdbPathwayUrl;

    @Override
    public SwineMetabolismHmdbRes clone() {
        try {
            return (SwineMetabolismHmdbRes) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
