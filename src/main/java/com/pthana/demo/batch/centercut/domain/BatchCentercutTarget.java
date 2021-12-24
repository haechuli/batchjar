package com.pthana.demo.batch.centercut.domain;

import com.pthana.demo.batch.centercut.domain.pk.BatchCentercutTargetPk;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;


//Auto-generated Dto                 
@IdClass(BatchCentercutTargetPk.class)
@Entity @Table(name = "BATCH_CENTERCUT_TARGET")
@Data @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(callSuper=false) //lombok
public class BatchCentercutTarget implements Serializable {

	private static final long serialVersionUID = 1L;
	/** Base Date            */ @Id @Column(name="BASC_DT"                                , nullable=false  ) private LocalDate            bascDt                         ;
	/** Grrup Code           */ @Id @Column(name="GRP_CD"             , length=20         , nullable=false  ) private String               grpCd                          ;
	/** Sequence Number      */ @Id @Column(name="SEQ_NO"             , length=10         , nullable=false  ) private Long                 seqNo                     = 0L ;

	@Column(name = "REG_EMP_NO", nullable = false, length = 8)
	private String regEmpNo;

	@Column(name = "SYS_REG_DTM", nullable = false)
	private ZonedDateTime sysRegDtm;

	@Column(name = "UPD_EMP_NO", nullable = false, length = 8)
	private String updEmpNo;

	@Column(name = "SYS_UPD_DTM", nullable = false)
	private ZonedDateTime sysUpdDtm;

	/** Status Code(0: before call Service, 1 : After Call Service, 9: COMPLETE)  */     @Column(name="ST_CD"              , length=1                            ) private String               stCd                           ;
	/** Service Name         */     @Column(name="SVC_NM"             , length=20                           ) private String               svcNm                          ;
	/** System Parameter For Service(JSON TYPE) */     @Column(name="SYSTEM_PARAM"       , length=1024                         ) private String               systemParam                    ;
	/** Input Parameter For Service(JSON TYPE) */     @Column(name="INPUT_PARAM"        , length=4000                         ) private String               inputParam                     ;
	/** Result Value After Call Service */     @Column(name="OUTPUT"             , length=4000                         ) private String               output                         ;
	/** Grobal Id(unique)    */     @Column(name="GROBAL_ID"          , length=32                           ) private String               grobalId                       ;
	/** Error Code after call Service */     @Column(name="ERR_CD"             , length=10                           ) private String               errCd                          ;
	/** Error Message after call Service */     @Column(name="ERR_MSG"            , length=4000                         ) private String               errMsg                         ;
	/** Relation Number to Batch Execution */     @Column(name="JOB_EXECUTION_ID"   , length=19                           ) private Long                 jobExecutionId            = 0L ;

 }