package com.pthana.demo.batch.centercut.domain;


import com.pthana.demo.batch.centercut.domain.pk.BatchCentercutTargetPk;
import com.pthana.demo.batch.centercut.dto.CenterCutGroupMinMax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/** BATCH_CENTERCUT_TARGET[Batch Centet Cut Target Object] DAO Repository AUTO Generate */
public interface BatchCentercutTargetRepository extends JpaRepository<BatchCentercutTarget, BatchCentercutTargetPk>
{


    @Query(value = "select b.grpCd," +
            "         count(*) as cnt," +
            "         min(b.seqNo) as minSeqNo," +
            "         max(b.seqNo) as maxSeqNo " +
            " from BatchCentercutTarget b " +
            " where b.stCd = '0' " +
            " and b.bascDt = ?1 " +
            " and b.grpCd = ?2" +
            " group by grpCd" +
            " order by grpCd asc", nativeQuery = true)
    public ArrayList<CenterCutGroupMinMax> getMinMax(LocalDate bascDt, String grpCd);


//    public default Long getMaxSeq(LocalDate bascDt, String grpCd)
//    {
//
//        return queryFactory.select(CENTERCUT.seqNo.max().coalesce(Long.valueOf(0)))
//                .from(CENTERCUT)
//                .where(CENTERCUT.bascDt.eq(bascDt)
//                        , CENTERCUT.grpCd.eq(grpCd))
//                .fetchOne();
//    }

}
