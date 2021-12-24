package com.pthana.demo.util.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;


public class LongDeserializer extends JsonDeserializer<Long>
{
    @Override
    public Long deserialize(JsonParser p, DeserializationContext ctx) throws IOException {

        if( StringUtils.isBlank(p.getValueAsString()) )
        {
            return 0L;
        }
        else
        {
            return Long.parseLong(p.getValueAsString());
        }
    }
}