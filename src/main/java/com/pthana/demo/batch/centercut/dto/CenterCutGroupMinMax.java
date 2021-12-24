package com.pthana.demo.batch.centercut.dto;

public class CenterCutGroupMinMax {
    private String grpCd ;
    private Long minSeqNo;
    private Long maxSeqNo;
    private Long cnt;

    public String getGrpCd() {
        return grpCd;
    }

    public void setGrpCd(String grpCd) {
        this.grpCd = grpCd;
    }

    public Long getMinSeqNo() {
        return minSeqNo;
    }

    public void setMinSeqNo(Long minSeqNo) {
        this.minSeqNo = minSeqNo;
    }

    public Long getMaxSeqNo() {
        return maxSeqNo;
    }

    public void setMaxSeqNo(Long maxSeqNo) {
        this.maxSeqNo = maxSeqNo;
    }

    public Long getCnt() {
        return cnt;
    }

    public void setCnt(Long cnt) {
        this.cnt = cnt;
    }


    @Override
    public String toString() {
        return "MsgProduct [grpCd=" + grpCd + ", minSeqNo=" + minSeqNo + ", maxSeqNo=" + maxSeqNo +"]";
    }

}
