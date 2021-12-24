package com.pthana.demo.util.mapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.pthana.demo.config.DataSourceConfiguration;


import java.io.IOException;
import java.time.LocalDate;


public class LocalDateSerializer extends JsonSerializer<LocalDate>
{
    @Override
    public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException
    {
        if(value == null)
        {
            gen.writeString("");
        }
        else
        {
            gen.writeString(value.format(DataSourceConfiguration.DATE_FORMATTER));
        }
    }
}