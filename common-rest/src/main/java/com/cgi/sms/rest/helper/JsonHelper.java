package com.cgi.sms.rest.helper;

import com.cgi.sms.rest.converter.ZonedDateTimeDeserializer;
import com.cgi.sms.rest.converter.ZonedDateTimeSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.log4j.Log4j;

import java.time.ZonedDateTime;
import java.util.Optional;

@Log4j
public class JsonHelper {
    public static Optional<String> convertObjectToJson(Object obj) {
        try {
            ObjectMapper objectMapper = buildObjectMapper();
            String jsonInString = objectMapper.writeValueAsString(obj);
            return Optional.of(jsonInString);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static ObjectMapper buildObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = buildObjectModule();
        objectMapper.registerModule(module);
        return objectMapper;
    }

    private static SimpleModule buildObjectModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(ZonedDateTime.class, new ZonedDateTimeSerializer());
        module.addDeserializer(ZonedDateTime.class, new ZonedDateTimeDeserializer());
        return module;
    }
}
