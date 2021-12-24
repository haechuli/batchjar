package com.pthana.demo.batch.centercut;

import com.pthana.demo.batch.centercut.domain.BatchCentercutTargetRepositoryExt;
import com.pthana.demo.batch.centercut.dto.CenterCutGroupMinMax;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Simple minded partitioner for a range of values of a column in a database
 * table. Works best if the values are uniformly distributed (e.g.
 * auto-generated primary key values).
 *
 * @author Dave Syer
 *
 */

/** @Project : tifsBatch
 * @FileName : ColumnRangePartitioner.java
 * @Date   : 2017. 12. 19.
 * @Author  : haechuli
 * @History :
 * @Description :
 */
@Component
public class CenterCutPartitioner implements Partitioner {

    public Logger logger = LoggerFactory.getLogger(CenterCutPartitioner.class);


//    @Autowired
//    private DaoCommonExt daoCommonExt;

    private final BatchCentercutTargetRepositoryExt batchCentercutTargetRepositoryExt;

    private String table = "";

    private String column = "" ;

    private String partitionType = "";

    private String grpCd;
    private LocalDate bascDt;
    private Long startNo;
    private Long endNo;

    public CenterCutPartitioner(BatchCentercutTargetRepositoryExt batchCentercutTargetRepositoryExt) {
        this.batchCentercutTargetRepositoryExt = batchCentercutTargetRepositoryExt;
    }

    public void setStartNo(Long startNo) {
        this.startNo = startNo;
    }

    public void setEndNo(Long endNo) {
        this.endNo = endNo;
    }


    public void setGrpCd(String grpCd) {
        this.grpCd = grpCd;
    }

    public void setBascDt(LocalDate bascDt) {
        this.bascDt = bascDt;
    }

    /**
     * The name of the SQL table the data are in.
     *
     * @param table the name of the table
     */
     public void setTable(String table) {
         this.table = table;
     }

    /**
     * The name of the column to partition.
     *
     * @param column the column name.
     */
    public void setColumn(String column) {
        this.column = column;
    }

    /**
     * The data source for connecting to the database.
     *
     * @param dataSource a {@link DataSource}
     */
//    public void setDataSource(DataSource dataSource) {
//        jdbcTemplate = new JdbcTemplate(dataSource);
//    }

    /**
     * The type of the partition.
     *
     * @param partitionType the Partition Type.
     */
    public void setPartitionType(String partitionType) {
        this.partitionType = partitionType;
    }

    /**
     * Partition a database table assuming that the data in the column specified
     * are uniformly distributed. The execution context values will have keys
     * <code>minValue</code> and <code>maxValue</code> specifying the range of
     * values to consider in each partition.
     *
     * @see Partitioner#partition(int)
     */
    @Override
    public Map<String, ExecutionContext> partition(int gridSize ) {


        int number = 0;

        List<CenterCutGroupMinMax> ccList = null;

        logger.debug("bascDt=[{}], grpCd=[{}]",bascDt,grpCd);
        ccList = batchCentercutTargetRepositoryExt.getMinMaxExt(bascDt,grpCd,startNo,endNo);   // By GrpCd,  MIN.MAX,COUNT
        
        Map<String, ExecutionContext> result = new HashMap<>();
        if(ccList.size() == 0 ) return result;

        CenterCutGroupMinMax ccMinMax = ccList.get(0);

        logger.debug("ccMinMax=[{}]",ccMinMax);
        //int max = ccMinMax.getCnt();
        Long max = ccMinMax.getMaxSeqNo();
        Long targetSize = (max-ccMinMax.getMinSeqNo()) / gridSize + 1;

        Long start = ccMinMax.getMinSeqNo();
        Long end = start + targetSize - 1;

        while (start <= max) {
            ExecutionContext value = new ExecutionContext();
            result.put("partition" + number, value);

            if (end >= max) {
                end = max;
            }
            logger.info("start=" + start);
            logger.info("end=" + end);

            value.putString("grpCd"   , ccMinMax.getGrpCd()    );
            value.putString("minValue", String.valueOf( start ));
            value.putString("maxValue", String.valueOf( end   ));

            start += targetSize;
            end += targetSize;
            number++;
        }

        return result;
    }
}
