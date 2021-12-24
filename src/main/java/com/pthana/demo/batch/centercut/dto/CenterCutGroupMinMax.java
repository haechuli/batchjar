package com.pthana.demo.batch.centercut.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false) //lombok
public class CenterCutGroupMinMax {
    private String grpCd ;
    private Long minSeqNo;
    private Long maxSeqNo;
    private Long cnt;



}
