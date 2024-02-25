package com.example.ousmartlocker.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

@Component
public class ReadToken {

    @Autowired
    private ObjectMapper mapper;

    public String getRequestAttribute(HttpServletRequest request, final String attributeName) {
        try {
            String jwt = request.getHeader("Authorization");
            if (jwt != null) {
                Map<String, Object> identity = getIdentity(jwt, request);
                return identity.get(attributeName).toString();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public Map<String, Object> getIdentity(String jwt, HttpServletRequest request) throws IOException {
        if (jwt == null) jwt = request.getHeader("Authorization");
        String tokenValue = jwt.replace("Bearer", "").trim();
        Map<String, Object> jwtBody = mapper.readValue(parseJWT(tokenValue), new TypeReference<>() {
        });
        return jwtBody;
    }

    public static String parseJWT(String jwtToken) {
        String[] splitArray = jwtToken.split("\\.");
        String base64EncodedBody = splitArray[1];
        return new String(Base64.getDecoder().decode(base64EncodedBody));
    }

}