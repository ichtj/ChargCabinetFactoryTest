package com.face.chargcabinetfactorytest.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

public class GsonTools {
    private static Gson filterNullGson = (new GsonBuilder()).enableComplexMapKeySerialization().setDateFormat("yyyy-MM-dd HH:mm:ss:SSS").create();
    private static Gson nullableGson = (new GsonBuilder()).enableComplexMapKeySerialization().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss:SSS").create();

    protected GsonTools() {
    }

    public static String toJsonWtihNullField(Object obj) {
        return nullableGson.toJson(obj);
    }

    public static String toJsonFilterNullField(Object obj) {
        return filterNullGson.toJson(obj);
    }

    public static <T> T fromJson(String json, Type type) {
        return nullableGson.fromJson(json, type);
    }
}