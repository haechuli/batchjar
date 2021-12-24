package com.pthana.demo.util.mapper;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;

/** 객체의 유틸 클래스
 *  Class 로딩 등의 객체관련 기능을 제공하는 유틸이다.
 *
 * @FileName : ObjectUtil.java
 * @Project  : tifs
 * @Date     : 2017. 11. 23.
 * @author   : SonJooArm
 * @Change History :
 * @Description :
 */
@Slf4j
public class MapperUtil
{

    public static final ObjectMapper mapper = getObjectMapper();


    public static ObjectMapper getObjectMapper()
    {
        Jackson2ObjectMapperBuilder j2om = Jackson2ObjectMapperBuilder.json();


        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer( Integer.class    , new IntegerDeserializer()    );
        javaTimeModule.addDeserializer( Long.class       , new LongDeserializer()       );
        javaTimeModule.addDeserializer( BigDecimal.class , new BigDecimalDeserializer() );

        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer());
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());

        javaTimeModule.addSerializer(ZonedDateTime.class, new ZonedDateTimeSerializer());
        javaTimeModule.addDeserializer(ZonedDateTime.class, new ZonedDateTimeDeserializer());



        j2om.modules(javaTimeModule);

        ObjectMapper mapper = j2om.build();
        SerializerProvider sp = mapper.getSerializerProvider();
        sp.setNullValueSerializer( new NullSerializer() );


//        // 모르는 property에 대해 무시하고 넘어간다. DTO의 하위 호환성 보장에 필요하다.
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        // ENUM 값이 존재하지 않으면 null로 설정한다. Enum 항목이 추가되어도 무시하고 넘어가게 할 때 필요하다.
        mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, false);
//
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);


//
//        // Enum 값을 String으로 처리
        mapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
//
//
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);

        //mapper.setPropertyNamingStrategy( new CamelCaseStrategy() );

        mapper.configure(Feature.ALLOW_NUMERIC_LEADING_ZEROS , true);
        

        return mapper;
      }


}
