package com.pthana.demo.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pthana.demo.config.TifsBatchCTX;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;


@PropertySources({
    @PropertySource("classpath:config/application.yml")
})

@Slf4j
public class CbsTran
{

    private String cbsAddr;
    private String userId;
    private String passwd;

    private HashMap<String,Object> sysInfo = new HashMap<>();
    private HashMap<String,Object> userMap = new HashMap<>();

    public String login() throws Exception {
        cbsAddr = TifsBatchCTX.getCbsAddr();
        userId  = "";
        passwd  = "";
        return login(userId,passwd);
    }

    public String login(String userId, String passwd) throws Exception {
        cbsAddr = TifsBatchCTX.getCbsAddr();
        return login(cbsAddr,userId,passwd);
    }

    public String login(String onlineIp,String userId, String passwd) throws Exception
    {

        this.sysInfo = new HashMap<>();
        log.debug("CbsTrean Login Start!!!");
        String token = "";
        Duration connectTimeout = Duration.ofSeconds(5);  // jhc 
        Duration readTimeout = Duration.ofSeconds(18);    // jhc 
        RestTemplateBuilder rtb = new RestTemplateBuilder();
        rtb.setConnectTimeout(connectTimeout);  // jhc 
        rtb.setReadTimeout(readTimeout);        // jhc 
        RestTemplate restTemplate =  rtb.build();

        HashMap<String,Object> svcResult = new HashMap<>();

        this.cbsAddr = onlineIp;


        log.debug("CbsTrean Server {}", this.cbsAddr);

        restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

        userMap = new HashMap<>();
        sysInfo = getSysInfo();

        log.debug("CbsTrean SysInfo {}", sysInfo);

        HashMap<String,Object> input = new HashMap<>();
        HashMap<String,Object> param = new HashMap<>();


        input.put("userId",userId   );
        input.put("password",passwd );


        param.put("SysInfo",sysInfo);
        param.put("input",input);

        log.debug(mapper.writeValueAsString(param));



        try
        {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("X-Requested-With", "XMLHttpRequest");

            log.debug("ServiceURL=[{}]",cbsAddr + "LOGIN.SVC");

            RequestEntity<String> req = new RequestEntity<>(mapper.writeValueAsString(param),
                                                                  headers,
                                                                  HttpMethod.POST,
                                                                  new URI( cbsAddr + "LOGIN.SVC" ) );

            ResponseEntity<String> response = restTemplate.exchange(req, String.class);

            String resultString = response.getBody();
            HttpHeaders responseHeaders = response.getHeaders();

            svcResult = mapper.readValue(resultString, HashMap.class);


            log.debug("cbsLogin result = {}", svcResult );
            log.debug("cbsLogin headers = {}", responseHeaders.toSingleValueMap().toString() );

            userMap = (HashMap<String, Object>) svcResult.get("output");
            sysInfo = (HashMap<String, Object>) svcResult.get("SysInfo");

            userMap.put("SysInfo", sysInfo);

            token =  responseHeaders.toSingleValueMap().get("X-Auth-Token");
        }
        catch (HttpStatusCodeException e)
        {
            log.error("Service Call Error [{}]", e.getResponseBodyAsString(), e);

            throw e;
        }
        catch (Exception e)
        {
            log.error("Service Call Error [{}]", e.getMessage(), e);
            throw e;
        }

        return token;
    }

    public HashMap<String, Object> getSysInfo()
    {
        if(sysInfo == null)
        {
            sysInfo = new HashMap<>();
            sysInfo.put("chnlTypCd", "IBT" );
        }

        sysInfo.put("chnlTypCd", "IBT" );


        return sysInfo;
    }

    public HashMap<String, Object> setAcSysInfo(String trscBrNo, String acBrNo, LocalDate trscDt)
    {
        return setAcSysInfo(trscBrNo, acBrNo, trscDt, "");
    }

    public HashMap<String, Object> setAcSysInfo(String trscBrNo, String acBrNo, LocalDate trscDt, String scrnId)
    {
        if(sysInfo == null)
        {
            sysInfo = new HashMap<>();
        }

        String userId = "B" + trscBrNo +"U1";
        sysInfo.put("opNo", userId);
        sysInfo.put("tlrNo", userId);
        sysInfo.put("userId", userId);

        sysInfo.put("istBrNo", trscBrNo);
        sysInfo.put("acBrNo", acBrNo);
        sysInfo.put("trscBrNo", trscBrNo);
        sysInfo.put("trscDt", trscDt);

        sysInfo.put("scrnId", scrnId);

        return sysInfo;
    }

    //@SuppressWarnings("unchecked")
    public HashMap<String, Object> CallService( HashMap<String, Object> input ) throws Exception
    {
        //TifsBatchCTX tifsBatchCTX = (TifsBatchCTX)BatchUtil.getBean("TifsBatchCTX");

        RestTemplateBuilder rtb = new RestTemplateBuilder();
        ObjectMapper mapper = new ObjectMapper();
        Duration connectTimeout = Duration.ofSeconds(5); 
        Duration readTimeout = Duration.ofSeconds(18); 
        rtb.setConnectTimeout(connectTimeout);
        rtb.setReadTimeout(readTimeout);
        RestTemplate restTemplate =  rtb.build();

        HashMap<String, Object> rtnMap = new HashMap<>();
        String ServiceUrl = "";

        cbsAddr = TifsBatchCTX.getCbsAddr();

        if( ! input.containsKey("ServiceUrl"))
        {
            throw new Exception("ServiceUrl is Required Value!");
        }

        ServiceUrl = (String) input.get("ServiceUrl");
        input.remove("ServiceUrl");


        HashMap<String,Object> param = new HashMap<>();
        //HashMap<String,Object> sysInfo = new HashMap<>();

        sysInfo.remove("log");

        if(sysInfo.get("trscDt") != null)
        {
            String trscDtTmp = sysInfo.get("trscDt").toString();
            sysInfo.remove("trscDt");
            sysInfo.put("trscDt", trscDtTmp);
        }

        param.put("SysInfo",sysInfo);


        // param.put("input",input);
        param.put("input", ObjectUtil.copyObject(input, HashMap.class));

        try
        {

            log.debug("ServiceURL=[{}]",cbsAddr + ServiceUrl);

            RequestEntity<String> req = new RequestEntity<>(mapper.writeValueAsString(param), getHeader(),
                    HttpMethod.POST, new URI(cbsAddr + ServiceUrl));
            ResponseEntity<String> response = restTemplate.exchange(req, String.class);
            String resultString = response.getBody();


            log.debug("Service Call Result [{}]", resultString);

            rtnMap =  mapper.readValue(resultString, HashMap.class);
            rtnMap.put("cbsCallResult","SUCCESS");
            return rtnMap;
        }
        catch (HttpStatusCodeException e)
        {
            log.error("Service Call Error [{}]", e.getResponseBodyAsString(), e);
            rtnMap = mapper.readValue(e.getResponseBodyAsString(), HashMap.class);
            rtnMap.put("cbsCallResult","ERROR");
            return rtnMap;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            rtnMap.put("errorMessage", e.getMessage());

            return rtnMap;
        }
    }


    public HashMap<String, Object> CallServiceException( HashMap<String, Object> input ) throws Exception
    {
        String svcID = ObjectUtil.getMapValue( input, "ServiceUrl", String.class);
        HashMap<String, Object> output = CallService(input);


        if( ObjectUtil.getMapValue(output, "cbsCallResult", String.class).compareTo("ERROR")  == 0 )
        {
            ArrayList<String> errMsg = (ArrayList<String>) output.get("errorMessage");

            if(errMsg != null)
            {
                errMsg.add(0, "Call "+svcID+" Error");
                errMsg.add(MessageFormatter.format("GlobalID[{}]", output.get("GlobalID")).getMessage());
                throw new Exception("Response Error");
            }
            else
            {
                throw new Exception("Call "+svcID+" Error");
//                throw new TifsException("999999999"
//                                        ,"Call "+svcID+" Error"
//                                        , ObjectUtil.getMapValue(output, "GlobalID", String.class)
//                                        );
            }
        }

        return output;
    }
    public HttpHeaders getHeader()
    {
        HttpHeaders header = new HttpHeaders();


        header.setContentType(MediaType.APPLICATION_JSON);
        header.add("X-Requested-With", "XMLHttpRequest");

        header.add("X-Auth-Token", GetToken());

        log.debug("Request Header {}", header);
        return header;
    }



    private String GetToken()
    {
        return (String) TifsBatchCTX.getToken();
    }



}
