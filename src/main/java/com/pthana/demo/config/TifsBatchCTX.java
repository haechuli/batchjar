package com.pthana.demo.config;


import com.pthana.demo.util.CbsTran;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


/** @Project  : tifs.batch
 *  @FileName : TifsBatchCTX.java
 *  @Date     : 2020. 08. 20.
 *  @author   : Jung Hae chul
 *  @History :
 *  @Description :
 */
@Slf4j
@Data
@Component
public class TifsBatchCTX
{

    @Value("${batch.onlineIp:}")
    public String cbsAddr;

    private static String token = "";
    private static String onlineIp="";

    @Bean
    public void onlineLogIn() throws  Exception {

        log.debug("TifsBatchCTX 시작 {} ", this.cbsAddr);
        onlineIp = this.cbsAddr;
        CbsTran cbsTran = new CbsTran();
        /*********************************************************
         *  1. TifsBatchCTX Online Servie Login
         **********************************************************/
        token = cbsTran.login(this.cbsAddr,"B0888U1", "b0888u1admin!");
    }


    public static String getToken() {
        return token;
    }

    public static String getCbsAddr() {
        return onlineIp;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setCbsAddr(String cbsAddr) {
        this.cbsAddr = cbsAddr;
    }
}
