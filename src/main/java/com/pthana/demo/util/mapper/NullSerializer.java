package com.pthana.demo.util.mapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;


/** @Project  : tifs
 *  @FileName : NullSerializer.java
 *  @Date     : 2017. 12. 11.
 *  @author   : SonJooArm
 *  @Description :
 */
public class NullSerializer extends JsonSerializer<Object>
{
    @Override
    public void serialize(Object value, JsonGenerator jsongen, SerializerProvider provider) throws IOException {
        jsongen.writeString("");
    }
}