package com.pthana.demo.util.mapper;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.pthana.demo.config.DataSourceConfiguration;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalDate;


public class LocalDateDeserializer extends JsonDeserializer<LocalDate>
{


    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if( StringUtils.isBlank(p.getValueAsString()) )
        {
            return null;
        }
        else
        {
            return LocalDate.parse(p.getValueAsString(), DataSourceConfiguration.DATE_FORMATTER);
        }
    }
}