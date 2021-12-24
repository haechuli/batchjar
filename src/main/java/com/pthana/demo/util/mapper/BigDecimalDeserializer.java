package com.pthana.demo.util.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;


public class BigDecimalDeserializer extends JsonDeserializer<BigDecimal>
{
    @Override
    public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        if( StringUtils.isBlank(p.getValueAsString()) )
        {
            return BigDecimal.ZERO;
        }
        else
        {
            return new BigDecimal(p.getValueAsString());
        }
    }
}