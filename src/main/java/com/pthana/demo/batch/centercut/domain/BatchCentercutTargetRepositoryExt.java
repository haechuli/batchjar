package com.pthana.demo.batch.centercut.domain;



import com.pthana.demo.batch.centercut.dto.CenterCutGroupMinMax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;


@Repository
public class BatchCentercutTargetRepositoryExt
{
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


//    @Query(value = "select b.grpCd," +
//            "         count(*) as cnt," +
//            "         min(b.seqNo) as minSeqNo," +
//            "         max(b.seqNo) as maxSeqNo " +
//            " from BatchCentercutTarget b " +
//            " where b.stCd = '0' " +
//            " and b.bascDt = ?1 " +
//            " and b.grpCd = ?2" +
//            " group by grpCd" +
//            " order by grpCd asc", nativeQuery = true)

    public List<CenterCutGroupMinMax> getMinMax(LocalDate bascDt, String grpCd) {

        StringBuilder sb = new StringBuilder();

        sb.append("\n select b.GRP_CD," );
        sb.append("\n         count(*) as cnt," );
        sb.append("\n         min(b.SEQ_NO) as minSeqNo," );
        sb.append("\n         max(b.SEQ_NO) as maxSeqNo " );
        sb.append("\n from batch_centercut_target b " );
        sb.append("\n where b.ST_CD = '0' " );
        sb.append("\n and b.BASC_DT = :bascDt ");
        sb.append("\n and b.GRP_CD = :grpCd");
        sb.append("\n group by GRP_CD");
        sb.append("\n order by GRP_CD asc");

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("bascDt", bascDt);
        paramMap.put("grpCd", grpCd);

        return namedParameterJdbcTemplate.query(sb.toString(), paramMap, new BeanPropertyRowMapper<>(CenterCutGroupMinMax.class));
    }

    public List<CenterCutGroupMinMax> getMinMaxExt(LocalDate bascDt, String grpCd, Long startNo, Long endNo) {

        StringBuilder sb = new StringBuilder();

        sb.append("\n select b.GRP_CD," );
        sb.append("\n         count(*) as cnt," );
        sb.append("\n         min(b.SEQ_NO) as minSeqNo," );
        sb.append("\n         max(b.SEQ_NO) as maxSeqNo " );
        sb.append("\n from batch_centercut_target b " );
        sb.append("\n where b.ST_CD = '0' " );
        sb.append("\n and b.BASC_DT = :bascDt ");
        sb.append("\n and b.GRP_CD = :grpCd");
        sb.append("\n and b.SEQ_No >= :startNo");
        sb.append("\n and b.SEQ_No <= :endNo");
        sb.append("\n group by GRP_CD");
        sb.append("\n order by GRP_CD asc");

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("bascDt", bascDt);
        paramMap.put("grpCd", grpCd);
        paramMap.put("startNo", startNo);
        paramMap.put("endNo", endNo);

        return namedParameterJdbcTemplate.query(sb.toString(), paramMap, new BeanPropertyRowMapper<>(CenterCutGroupMinMax.class));
    }
}
