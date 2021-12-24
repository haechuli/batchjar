package com.pthana.demo.batch.centercut;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Getter
//@NoArgsConstructor
public class CreateJobParameter {



    @Value("#{jobParameters[grpCd]}") // (1)
    private String grpCd;


    private LocalDate bascDt;

    @Value("#{jobParameters[bascDt]}") // (2)
    public void setBascDt(String bascDt) throws Exception {

        if(bascDt == null )
        {
            throw new IllegalStateException();
        } else {
            this.bascDt = LocalDate.parse(bascDt, DateTimeFormatter.ofPattern("yyyyMMdd"));

        }

    }

    public CreateJobParameter() {

    }

}