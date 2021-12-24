package com.pthana.demo.util.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;


public class IntegerDeserializer extends JsonDeserializer<Integer>
{
    @Override
    public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        if( StringUtils.isBlank(p.getValueAsString()) )
        {
            return 0;
        }
        else
        {
            return Integer.parseInt(p.getValueAsString());
        }
    }
}