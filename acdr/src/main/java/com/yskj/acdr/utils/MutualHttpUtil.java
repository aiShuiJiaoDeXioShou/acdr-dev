package com.yskj.acdr.utils;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;

import java.util.Map;

/**
 * 与商城系统交互的工具类
 */
public class MutualHttpUtil {
    private static final String TENANT_ID_HEADER = "tenant-id";
    private static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";

    /**
     * 发送GET请求
     *
     * @param url      请求的URL
     * @param params   请求参数
     * @param tenantId 租户ID
     * @return 响应字符串
     */
    public static String sendGetRequest(String url, Map<String, Object> params, String tenantId) {
        HttpRequest request = HttpUtil.createGet(url)
                .addHeaders(Map.of(TENANT_ID_HEADER, tenantId))
                .form(params);

        HttpResponse response = request.execute();
        return response.body();
    }

    /**
     * 发送POST请求
     *
     * @param url      请求的URL
     * @param body     请求体
     * @param tenantId 租户ID
     * @return 响应字符串
     */
    public static String sendPostRequest(String url, Map<String, Object> body, String tenantId) {
        HttpRequest request = HttpUtil.createPost(url)
                .addHeaders(Map.of(
                        TENANT_ID_HEADER, tenantId,
                        "Content-Type", CONTENT_TYPE_JSON))
                .body(JSONUtil.toJsonStr(body));

        HttpResponse response = request.execute();
        return response.body();
    }
}
