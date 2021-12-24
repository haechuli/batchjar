package com.pthana.demo.util.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Collection;


public class ListDeserializer extends StdDeserializer<Object> implements ContextualDeserializer {

    /**
     *
     */
    private static final long serialVersionUID = 5847990739743865369L;


    private final BeanProperty    property;

    /**
     * Default constructor needed by Jackson to be able to call 'createContextual'.
     * Beware, that the object created here will cause a NPE when used for deserializing!
     */
    public ListDeserializer()
    {
      super(Collection.class);
      this.property = null;
    }

    /**
     * Constructor for the actual object to be used for deserializing.
     *
     * @param property this is the property/field which is to be serialized
     */
    private ListDeserializer(BeanProperty property)
    {
      super(property.getType());
      this.property = property;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException
    {
      return new ListDeserializer(property);
    }


    @Override
    public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException
    {
        switch (jp.getCurrentToken())
        {
            case VALUE_STRING:
                // value is a string but we want it to be something else: unescape the string and convert it
                return MapperUtil.mapper.readValue(jp.getText(), property.getType());
            default:
                // continue as normal: find the correct deserializer for the type and call it
                return ctxt.findContextualValueDeserializer(property.getType(), property).deserialize(jp, ctxt);
        }
    }
  }