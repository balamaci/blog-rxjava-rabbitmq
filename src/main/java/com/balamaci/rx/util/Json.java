package com.balamaci.rx.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import rx.functions.Func1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.function.Predicate;

/**
 * @author Serban Balamaci
 */
public class Json {

    public static JsonArray readJsonArray(String fileName) {
        File jsonTestFile = new File(fileName);
        JsonParser jsonParser = new JsonParser();
        try {
            return (JsonArray) jsonParser.parse(new FileReader(jsonTestFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public static Func1<JsonObject, String> propertyStringValue(String name) {
        return jsonObject -> {
            JsonElement propertyValue = jsonObject.get(name);
            if(propertyValue != null) {
                return propertyValue.getAsString();
            }

            return null;
        };
    }

    public static Func1<JsonObject, Boolean> checkProperty(String propertyName, Predicate<String> condition) {
        return jsonObject -> {
            JsonElement jsonElement = jsonObject.get(propertyName);
            if(jsonElement != null) {
                return condition.test(jsonElement.getAsString());
            }

            return Boolean.FALSE;
        };
    }


}
