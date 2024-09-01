package cn.iocoder.yudao.module.handler;

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class ListStringTypeHandler extends AbstractJsonTypeHandler<List<String>> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public ListStringTypeHandler(Class<?> type) {
        super(type);
    }

    @Override
    public List<String> parse(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (Exception e) {
            throw new RuntimeException("JSON to List<String> conversion failed: " + json, e);
        }
    }

    @Override
    public String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Object to JSON conversion failed", e);
        }
    }
}



