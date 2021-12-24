package com.pthana.demo.batch.centercut.domain.pk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;


//Auto-generated Dto                 
@Data @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(callSuper=false) //lombok
public class BatchCentercutTargetPk implements Serializable { 

	private static final long serialVersionUID = 1L;
	/** Base Date            */ @Id @Column(name="BASC_DT"                                , nullable=false  ) private LocalDate            bascDt                         ;
	/** Grrup Code           */ @Id @Column(name="GRP_CD"             , length=20         , nullable=false  ) private String               grpCd                          ;
	/** Sequence Number      */ @Id @Column(name="SEQ_NO"             , length=10         , nullable=false  ) private Long                 seqNo                     = 0L ;

 }