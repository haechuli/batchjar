package com.pthana.demo.batch.centercut;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pthana.demo.batch.centercut.domain.BatchCentercutTarget;
import com.pthana.demo.util.CbsTran;
import com.pthana.demo.util.ObjectUtil;
import com.pthana.demo.util.StringUtil;
import com.pthana.demo.util.mapper.MapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;


/** @Project : tifsBatch
 * @FileName : AutoTansferItemProcessor.java
 * @Date   : 2017. 12. 19.
 * @Author  : haechuli
 * @History :
 * @Description :
 */
@Slf4j
public class CenterCutProcessor implements ItemProcessor<BatchCentercutTarget, BatchCentercutTarget>
{
    @Value("#{stepExecution.jobExecution.id}")
    private Long jobId;

    @Autowired private ObjectMapper mapper;
    //@Autowired private CbsTran cbsTran;


    @SuppressWarnings("unchecked")
    @Override
    public BatchCentercutTarget process(BatchCentercutTarget itemObj)
    {

        try {

            log.debug("process=[{}]",itemObj);

            /* Online Service Call */
            CbsTran cbsTran = new CbsTran();
            

//            log.debug("process1=[{}]",itemObj.getSeqNo());
//
//            /*********************************************************
//             *  1. Login Process
//             **********************************************************/
//            cbsTran.login("B0888U1", "b0888u1admin!");
//
//            log.debug("process2=[{}]",itemObj.getSeqNo());
            
            
            /*********************************************************
             *  2. Service Input Set 
             **********************************************************/  
            /* Setting  Parameter */
            HashMap<String, Object> input = MapperUtil.mapper.readValue(itemObj.getInputParam(), HashMap.class);
            input.put("ServiceUrl", itemObj.getSvcNm());
            
            /*********************************************************
             *  3. Service SysInfo Set 
             **********************************************************/  
            /* DB의 SYSTEM_PARAM의 내용을 SysInfo에 설정한다 */
            HashMap<String,Object> sSysInfo  = mapper.readValue(itemObj.getSystemParam(), HashMap.class);
            ObjectUtil.mergeObject(cbsTran.getSysInfo(), sSysInfo);
           // cbsTran.getSysInfo().put("trsCDt", jobCTX.getBizDayInfo().getBussDt());
            cbsTran.getSysInfo().put("chnlTypCd","ICT"); // CenterCut

            log.debug("cbsTran.getSysInfo=[{}]",cbsTran.getSysInfo());
            log.debug("process4=[{}]",itemObj.getSeqNo());
               
            /*********************************************************
             *  4. Call Online Service 
             **********************************************************/

            log.debug("=======================================================================");
            log.debug("  OnLineService Call ({})                                               ", itemObj.getSvcNm());
            log.debug("=======================================================================");

            HashMap<String, Object> result = new HashMap<>();
            result = cbsTran.CallService(input);

            if ( !StringUtil.equals((String)result.get("cbsCallResult"),"SUCCESS"))
            {
                result = cbsTran.CallService(input);
            }


            /*********************************************************
             *  5. Result Process
             **********************************************************/  
            log.debug("process5=[{}]",itemObj.getSeqNo());
            log.debug("result=[{}]",result);

            if ( cbsTran.getSysInfo().get("userId") == null )
                itemObj.setUpdEmpNo("B0888U1");
            else
                itemObj.setUpdEmpNo(String.format("B%sU1",cbsTran.getSysInfo().get("trscBrNo")));


            itemObj.setSysUpdDtm(ZonedDateTime.now());
            /* GlobalID */
            itemObj.setGrobalId((String)result.get("GlobalID"));

            /* Job Execution Id */
            if ( StringUtil.equals((String)result.get("cbsCallResult"),"SUCCESS"))
            {
                itemObj.setStCd("1");  // SUCCESS

                /* OutPut */
                String output = mapper.writeValueAsString(result.get("output"));
                log.debug("output=[{}]",output);
                itemObj.setOutput(output);
                itemObj.setErrMsg("");
                itemObj.setErrCd("");
            }
            else
            {
                itemObj.setStCd("9");  // FAILED

                /* errorMessage */
                ArrayList<String> errMsg = (ArrayList<String>)result.get("errorMessage");
                for (String msg : errMsg)
                {
                    if(StringUtil.isNotBlank(msg))
                    {
                        //TODO : 에러메세지 4000 이상인 경우 저장시 오류발생함 -> 메세지를 잘라야함
                        itemObj.setErrMsg(msg);
                        break;
                    }
                }

                itemObj.setErrCd(ObjectUtil.getMapValue(result,"errorCode",String.class));
            }

            itemObj.setJobExecutionId(jobId);
        }
        catch (Exception e) {
            log.error("Error CbsTran !",e);
            itemObj.setStCd("9");  // FAILED
            itemObj.setErrMsg(e.getMessage());
            itemObj.setErrCd("999999999");
        }

        itemObj.setRegEmpNo("B0888U1");
        itemObj.setSysRegDtm(ZonedDateTime.now());
        itemObj.setUpdEmpNo("B0888U1");
        itemObj.setSysUpdDtm(ZonedDateTime.now());

        log.debug("itemObj=[{}]",itemObj);
        return itemObj;
    }
}


