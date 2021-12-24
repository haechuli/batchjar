package com.pthana.demo.batch.centercut;

import com.pthana.demo.batch.centercut.domain.BatchCentercutTarget;
import com.pthana.demo.batch.centercut.listener.BatchJobListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;


@Slf4j
//@Import(CreateJobParameter.class)
@Configuration
@RequiredArgsConstructor
public class CenterCutMain {

   

    @Value("${cc.limit.process.core.count:16}")
    private int corePoolSize;

    @Value("${cc.limit.process.max.count:16}")
    private int maxPoolSize;

    @Value("${cc.log.prefix:Thread_CenterCut-}")
    private String ccLogPrefix;

    @Value("${cc.commit.unit:1}")
    private int ccCommitUnit;

    @Value("${cc.chunkSize:1}")
    private int chunkSize;

    private final EntityManagerFactory entityManagerFactory;

    private final CreateJobParameter jobCTX;

    @Bean
    @JobScope
    public CreateJobParameter jobCTX() {
        return new CreateJobParameter();
    }

    private static final String QUERY_SELECT_CC_JOB =
            "\n SELECT B.BASC_DT                               " +
            "\n        ,B.GRP_CD                                " +
            "\n        ,B.SEQ_NO                                " +
            //"\n        ,B.REG_EMP_NO                            " +
            ///"\n        ,B.SYS_REG_DTM                           " +
            //"\n        ,B.UPD_EMP_NO                            " +
            //"\n        ,B.SYS_UPD_DTM                           " +
            "\n        ,B.ST_CD                                 " +
            "\n        ,B.SVC_NM                                " +
            "\n        ,B.SYSTEM_PARAM                          " +
            "\n        ,B.INPUT_PARAM                           " +
            "\n        ,B.OUTPUT                                " +
            "\n        ,B.GROBAL_ID                             " +
            "\n        ,B.ERR_CD                                " +
            "\n        ,B.ERR_MSG                               " +
            "\n        ,B.JOB_EXECUTION_ID                      " +
            "\n FROM BATCH_CENTERCUT_TARGET B                  " +
            "\n WHERE GRP_CD = ?                               " +
            "\n AND   B.SEQ_NO BETWEEN ? and ?                 " +
            "\n AND   B.ST_CD = '0'                            " +
            "\n ORDER BY B.SEQ_NO                              ";

    private static final String QUERY_UPDATE_CC_JOB =
            "\n UPDATE BATCH_CENTERCUT_TARGET      " +
            "\n SET   ST_CD = :stCd                       " +
            "\n      ,GROBAL_ID = :grobalId               " +
            "\n      ,ERR_CD    = :errCd                  " +
            "\n      ,ERR_MSG   = :errMsg                 " +
            "\n      ,JOB_EXECUTION_ID = :jobExecutionId  " +
            "\n      ,OUTPUT    = :output                 " +
            "\n      ,UPD_EMP_NO   = :updEmpNo            " +
            "\n      ,SYS_UPD_DTM  = :sysUpdDtm           " +
            "\n WHERE BASC_DT = :bascDt                   " +
            "\n AND   GRP_CD  = :grpCd                    " +
            "\n AND   SEQ_NO  = :seqNo                    " +
            "\n AND   ST_CD = '0'                         " ;


//      @Bean
//      @StepScope
//      public JpaPagingItemReader<BatchCentercutTarget> centerCutReader(
//                                                            @Value("#{stepExecutionContext[grpCd]}") String grpCd
//                                                          , @Value("#{stepExecutionContext[minValue]}") Long minValue
//                                                          , @Value("#{stepExecutionContext[maxValue]}") Long maxValue
//                                                          , DataSource dataSource ) {
//          JpaPagingItemReader<BatchCentercutTarget> jpaPagingItemReader = new JpaPagingItemReader<>();
//          jpaPagingItemReader.setQueryString(
//                  "select u from BatchCentercutTarget as u where u.grpCd = :grpCd and u.stCd = '0' and u.seqNo >= :minValue  and u.seqNo <= :maxValue order by u.seqNo"
//          );
//          HashMap<String, Object> map = new HashMap<>();
//
//
//          map.put("grpCd", grpCd);
//          map.put("minValue", minValue);
//          map.put("maxValue", maxValue);
//
//          jpaPagingItemReader.setParameterValues(map);
//          jpaPagingItemReader.setEntityManagerFactory(entityManagerFactory);
//          jpaPagingItemReader.setPageSize(chunkSize);
//          return jpaPagingItemReader;
//      }


//    @Bean
//    @StepScope
//    public JdbcPagingItemReader<BatchCentercutTarget> centerCutReader(
//                                                            @Value("#{stepExecutionContext[grpCd]}") String grpCd
//                                                          , @Value("#{stepExecutionContext[minValue]}") String minValue
//                                                          , @Value("#{stepExecutionContext[maxValue]}") String maxValue
//                                                          , DataSource dataSource )
//    {
//        log.debug("=======================================================================");
//        log.debug("  CenterCut Reader GroupCd={}, Min={},Max={}                      ", grpCd,minValue,maxValue);
//        log.debug("=======================================================================");
//
//        Map<String, Object> params = new HashMap<>();
//        params.put("minValue", minValue);
//        params.put("maxValue", maxValue);
//        params.put("grpCd", grpCd);
//
//        HashMap<String, Order> sortKeys = new HashMap<>(1);
//        sortKeys.put("seq_No", Order.ASCENDING);
//
//        return new JdbcPagingItemReaderBuilder<BatchCentercutTarget>()
//                .pageSize(chunkSize)
//                .fetchSize(chunkSize)
//                .dataSource(dataSource)
//                .rowMapper(new BeanPropertyRowMapper<>(BatchCentercutTarget.class))
//                .selectClause(" SELECT B.*                                     ")
//                .fromClause(" FROM BATCH_CENTERCUT_TARGET B                  " )
//                .whereClause( " WHERE GRP_CD = :grpCd                          " +
//                              " AND   B.SEQ_NO BETWEEN :minValue and :maxValue " +
//                              " AND   B.ST_CD = '0'                            "
//                            )
//                .parameterValues(params)
//                .sortKeys(sortKeys)
//                .name("centerCutReader")
//                .build();
//
//
//
//
//
//    }


    @Bean
    @StepScope
    public JdbcCursorItemReader<BatchCentercutTarget> centerCutReader(
                                                            @Value("#{stepExecutionContext[grpCd]}") String grpCd
                                                          , @Value("#{stepExecutionContext[minValue]}") String minValue
                                                          , @Value("#{stepExecutionContext[maxValue]}") String maxValue
                                                          , DataSource dataSource )
    {
        log.debug("=======================================================================");
        log.debug("  CenterCut Reader GroupCd={}, Min={},Max={}                      ", grpCd,minValue,maxValue);
        log.debug("=======================================================================");


        JdbcCursorItemReader<BatchCentercutTarget> reader = new JdbcCursorItemReader<>();

        try {
            reader.setDataSource(dataSource);

            log.info("minValue=" + minValue           );
            log.info("maxValue=" + maxValue           );
            log.info("grpCd=" + grpCd           );

            reader.setRowMapper(new BeanPropertyRowMapper<>(BatchCentercutTarget.class));
            reader.setSql(QUERY_SELECT_CC_JOB);


//            List<Object> parameters = new ArrayList<>();
//            parameters.add(grpCd);
//            parameters.add(Integer.parseInt(minValue));
//            parameters.add(Integer.parseInt(maxValue));
//            // ANCHOR ListPreparedStatementSetter => ArgumentPreparedStatementSetter로 대체함
//            //ListPreparedStatementSetter pss = new ListPreparedStatementSetter(parameters);
//		    //ArgumentPreparedStatementSetter pss = new ArgumentPreparedStatementSetter(parameters.toArray());
//
            log.info("SQL=" + reader.getSql());
            reader.setPreparedStatementSetter(new PreparedStatementSetter() {
                                        @Override
                                        public void setValues(PreparedStatement ps) throws SQLException {
                                            ps.setString(1, grpCd);
                                            ps.setInt(2, Integer.parseInt(minValue));
                                            ps.setInt(3, Integer.parseInt(maxValue));
                                        }
                                    });


        } catch ( Exception e ) {
            log.info(e.getMessage());
        }

        return reader;
    }

    @Bean
    @StepScope
    public ItemProcessor<BatchCentercutTarget,BatchCentercutTarget> CenterCurItemProcessor() {
        return new CenterCutProcessor();
    }


    @Bean
    @StepScope
    public JpaItemWriter<BatchCentercutTarget> CenterCutWriter() {

        JpaItemWriter<BatchCentercutTarget> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);

        return jpaItemWriter;
    }


//    @Bean
//    @StepScope
//    public JdbcBatchItemWriter<BatchCentercutTarget> CenterCutWriter( DataSource dataSource) {
//
//        log.debug("=======================================================================");
//        log.debug("  CenterCut Writer   ");
//        log.debug("=======================================================================");
//
//        /* wirte execute result */
//        JdbcBatchItemWriter<BatchCentercutTarget> writer = new JdbcBatchItemWriter<>();
//        writer.setAssertUpdates(true);
//        writer.setDataSource(dataSource);
//        writer.setSql(QUERY_UPDATE_CC_JOB);
//
//        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<BatchCentercutTarget>());
//        return writer;
//    }

    /*  corePoolSize    : 풀 사이즈를 의미한다. 최초 생성되는 스레드 사이즈이며 해당 사이즈로 스레드가 유지된다.
                          해당 Job의 맞게 적절히 선택해야 한다. 많다고 성능이 잘나오는 것도 아니고 적다고 안나오는 것도 아니다.
                          충분히 테스트하면서 적절한 개수를 선택해야 한다.
        maxPoolSize      : 해당 풀에 최대로 유지할 수 있는 개수를 의미한다. 이 역시 Job에 맞게 적절히 선택해야 한다.
        keepAliveTime   : corePoolSize보다 스레드가 많아졌을 경우 maximumPoolSize까지 스레드가 생성이 되는데
                          keepAliveTime 시간만큼 유지했다가 다시 corePoolSize 로 유지되는 시간을 의미한다.
                          (그렇다고 무조건 maximumPoolSize까지 생성되는 건 아니다.)
        unit            : keepAliveTime 의 시간 단위를 의미한다.
        QueueCapacity   : corePoolSize보다 스레드가 많아졌을 경우, 남는 스레드가 없을 경우 해당 큐에 담는다.
     */


    @Bean
    public Step CenterCutStep(StepBuilderFactory steps )
    {

        return steps.get("CenterCutSlaveStep")
                .<BatchCentercutTarget,BatchCentercutTarget> chunk(chunkSize)
                .reader(centerCutReader(null,null,null,null))
                .processor(CenterCurItemProcessor())
                .writer(CenterCutWriter())
               //.faultTolerant()
                //.retryLimit(3)
                //.retry(SQLException.class)
                //.allowStartIfComplete(true)
                .build();
    }


    @Bean
    @Qualifier("CenterCutTaskExecutor")
    public ThreadPoolTaskExecutor CenterCutThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(corePoolSize);
        taskExecutor.setMaxPoolSize(maxPoolSize);
        taskExecutor.afterPropertiesSet();
        taskExecutor.setThreadNamePrefix(ccLogPrefix);
        taskExecutor.setQueueCapacity(100);
       // taskExecutor.setAllowCoreThreadTimeOut(true);
       // taskExecutor.setKeepAliveSeconds(0);
       // taskExecutor.setWaitForTasksToCompleteOnShutdown(Boolean.TRUE);
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Bean
    public PartitionHandler partitionHandler() throws Exception {
        TaskExecutorPartitionHandler retVal = new TaskExecutorPartitionHandler();
        retVal.setTaskExecutor(CenterCutThreadPoolTaskExecutor());
        retVal.setStep(CenterCutStep(null));
        retVal.setGridSize(corePoolSize);
        return retVal;
    }

    @Bean
    @JobScope
    public Step CenterCutStepManager(StepBuilderFactory steps) throws Exception
    {
        log.debug("GrdCd=[{}] NascDt=[{}]",jobCTX.getGrpCd(),jobCTX.getBascDt());
        centerCutPartitioner.setGrpCd(jobCTX.getGrpCd());
        centerCutPartitioner.setBascDt(jobCTX.getBascDt());
        centerCutPartitioner.setStartNo(jobCTX.getStartNo());
        centerCutPartitioner.setEndNo(jobCTX.getEndNo());
        centerCutPartitioner.setTable("BATCH_CENTERCUT_TARGET");
        centerCutPartitioner.setColumn("SEQ_NO");

        return steps.get("CenterCutMasterStep")
                // .partitioner(CenterCutSlaveStep(null))
                .partitioner("CenterCutSlaveStep", centerCutPartitioner)
                .partitionHandler(partitionHandler())
                // .taskExecutor(CenterCutThreadPoolTaskExecutor())
                // .gridSize(corePoolSize)
                .build();
    }

    @Bean
    public BatchJobListener batchJobListener() {
        return new BatchJobListener();
    }



    @Autowired
    private CenterCutPartitioner centerCutPartitioner;






    @Bean
    public Job CenterCut(JobBuilderFactory jobs) throws Exception
    {
        log.debug("=======================================================================");
        log.debug("  [[[[[ CenterCut Start Info ]]]]]]  ");
        log.debug("  corePoolSize = [{}] ",corePoolSize);
        log.debug("  maxPoolSize  = [{}] ",maxPoolSize);
        log.debug("  ccCommitUnit = [{}] ",ccCommitUnit);
        log.debug("=======================================================================");

        return   jobs.get("CenterCut")
            .incrementer(new RunIdIncrementer())
            .start(CenterCutStepManager(null))
            .listener(batchJobListener())
            .build();
    }

}
