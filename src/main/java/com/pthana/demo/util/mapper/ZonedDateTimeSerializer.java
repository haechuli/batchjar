package com.pthana.demo.util.mapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.pthana.demo.config.DataSourceConfiguration;


import java.io.IOException;
import java.time.ZonedDateTime;


public class ZonedDateTimeSerializer extends JsonSerializer<ZonedDateTime>
{


    @Override
    public void serialize(ZonedDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException
    {
        if(value == null)
        {
            gen.writeString("");
        }
        else
        {
            try
            {
                gen.writeString(value.format(DataSourceConfiguration.ZONED_DATETIME_FORMATTER));
                return;
            }
            catch (Exception e)
            {
                //e.printStackTrace();
            }


            gen.writeString(value.format(DataSourceConfiguration.ZONED_DATETIME_FORMATTER));
        }
    }
}