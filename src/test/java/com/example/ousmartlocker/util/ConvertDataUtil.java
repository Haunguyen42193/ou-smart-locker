package com.example.ousmartlocker.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class ConvertDataUtil {
    public static <T> T convertDataToObject(String data, Class<T> aClass) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(data, aClass);
    }

    public static <T> List<T> convertDataToListObject(String data, Class<T> aClass) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<T> result = new ArrayList<>();
        var objects = mapper.readValue(data, ArrayList.class);
        for (var a: objects) {
            result.add(mapper.convertValue(a, aClass));
        }
        return result;
    }
}
