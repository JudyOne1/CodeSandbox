package com.judy.codesandbox.config;

import com.google.gson.*;

import java.lang.reflect.Type;

public class NullValueHandlingTypeAdapter implements JsonSerializer<Integer>, JsonDeserializer<Integer> {

    @Override
    public JsonElement serialize(Integer src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src == null ? "null" : src.toString());
    }

    @Override
    public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String value = json.getAsString();
        if ("null".equals(value)) {
            return null;
        } else {
            return Integer.parseInt(value);
        }
    }
}