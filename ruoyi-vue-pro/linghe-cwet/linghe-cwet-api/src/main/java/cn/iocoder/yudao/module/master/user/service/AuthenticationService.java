package cn.iocoder.yudao.module.master.user.service;

import cn.iocoder.yudao.module.utils.LoginUtil;
import cn.hutool.json.JSONUtil;
import cn.iocoder.yudao.module.common.exception.BusinessException;
import cn.iocoder.yudao.module.enums.AuthenticationErrorCode;
import cn.iocoder.yudao.module.master.file.entity.FileMap;
import cn.iocoder.yudao.module.master.file.service.FileMapService;
import cn.iocoder.yudao.module.master.user.entity.AuthenticationResponse;
import cn.iocoder.yudao.module.master.user.entity.UserIdentityVerification;
import cn.iocoder.yudao.module.master.user.entity.Users;
import cn.iocoder.yudao.module.master.user.mapper.UserIdentityVerificationMapper;
import cn.iocoder.yudao.module.master.user.mapper.UsersMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
public class AuthenticationService {

    @Value("${aliyun.appcode}")
    private String appCode;

    @Value("${aliyun.auth_url}")
    private String authUrl;

    @Value("${aliyun.ocr_url}")
    private String ocrUrl;

    @Value("${path.profile}")
    private String profile;

    @Resource
    private FileMapService fmService;

    @Resource
    private UsersMapper usersMapper;

    @Resource
    private UserIdentityVerificationMapper uidmapper;

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final OkHttpClient client = new OkHttpClient();

    /**
     * 对path进行判断，如果是本地文件就二进制读取并base64编码，如果是url,则返回
     */
    private String imgBase64(String path) {
        String imgBase64 = "";
        if (path.startsWith("http")) {
            imgBase64 = path;
        } else {
            try {
                File file = new File(path);
                byte[] content = new byte[(int) file.length()];
                FileInputStream finputstream = new FileInputStream(file);
                finputstream.read(content);
                finputstream.close();
                imgBase64 = Base64.encodeBase64String(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return imgBase64;
    }

    /**
     * 进行OCR识别
     *
     * @param imgPath 图像路径或URL
     * @param side    身份证正反面 face/back
     * @return 识别结果
     * <p>
     * 返回结果如下：
     * 正面返回结果：
     * {
     * "address"    : "浙江省杭州市余杭区文一西路969号",   #地址信息
     * "config_str" : "{\\\"side\\\":\\\"face\\\"}",    #配置信息，同输入configure
     * "face_rect":{       #人脸位置
     * "angle": -90,   #angle表示矩形顺时针旋转的度数
     * "center":{      #center表示人脸矩形中心坐标
     * "x" : 952,
     * "y" : 325.5
     * },
     * "size":{        #size表示人脸矩形长宽
     * "height":181.99,
     * "width":164.99
     * }
     * },
     * "card_region":[  #身份证区域位置，四个顶点表示
     * {"x":165,"y":657},
     * {"x":534,"y":658},
     * {"x":535,"y":31},
     * {"x":165,"y":30}
     * ],
     * "face_rect_vertices":[  #人脸位置，四个顶点表示
     * {
     * "x":1024.6600341796875,
     * "y":336.629638671875
     * },
     * {
     * "x":906.66107177734375,
     * "y":336.14801025390625
     * },
     * {
     * "x":907.1590576171875,
     * "y":214.1490478515625
     * },
     * {
     * "x":1025.157958984375,
     * "y":214.63067626953125
     * }
     * ],
     * "name" : "张三",                 #姓名
     * "nationality": "汉"，            #民族
     * "num" : "1234567890",            #身份证号
     * "sex" : "男",                    #性别
     * "birth" : "20000101",            #出生日期
     * "is_fake": false,                   #是否是复印件
     * "warning": {         # 身份证质量分结果，只有当请求参数 quality_info=true 时，才会返回此字段
     * "completeness_score": 100,         # 完整度评分（浮点类型）
     * "is_copy": 0,          # 是否是复印件（1:是，0:否）
     * "is_reshoot": 0,               # 是否是翻拍（1:是，0:否）
     * "quality_score": 89.296059,         # 整体质量分数（浮点类型）
     * "tamper_score": 99.99968.            # 篡改分数（数值越大表示篡改可能性越大，推荐阈值：60）
     * },
     * "success" : true                 #识别结果，true表示成功，false表示失败
     * }
     * <p>
     * <p>
     * 反面返回结果:
     * {
     * "config_str" : "{\\\"side\\\":\\\"back\\\"}",  #配置信息，同输入configure
     * "start_date" : "19700101",       #有效期起始时间
     * "end_date" : "19800101",         #有效期结束时间
     * "issue" : "杭州市公安局",         #签发机关
     * "is_fake": false,                   #是否是复印件
     * "success" : true                 #识别结果，true表示成功，false表示失败
     * }
     */
    public Map<String, Object> recognizeIDCard(String imgPath, String side) {
        String imgBase64 = imgBase64(imgPath);

        // 配置JSON
        JsonNode configNode = objectMapper.createObjectNode().put("side", side);

        // 请求Body
        JsonNode requestNode = objectMapper.createObjectNode()
                .put("image", imgBase64)
                .set("configure", configNode);

        String bodys = requestNode.toString();

        RequestBody body = RequestBody.create(bodys, MediaType.get("application/json; charset=UTF-8"));
        Request request = new Request.Builder()
                .url(ocrUrl)
                .addHeader("Authorization", "APPCODE " + appCode)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.out.println("Http code: " + response.code());
                System.out.println("Http body error msg:" + response.body().string());
                throw new IOException("Unexpected code " + response);
            }

            String res = response.body().string();
            Map<String, Object> resMap = objectMapper.readValue(res, new TypeReference<>() {
            });
            return resMap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 对用户身份证信息进行验证
     *
     * @param name 用户实际名称
     * @param idNo 身份证信息
     * @return 验证之后的返回结果
     */
    public AuthenticationResponse authenticate(String name, String idNo) {
        RequestBody formBody = new FormBody.Builder()
                .add("name", name)
                .add("idNo", idNo)
                .build();
        Request request = new Request.Builder()
                .url(authUrl)
                .addHeader("Authorization", "APPCODE " + appCode)
                .post(formBody)
                .build();

        Call call = client.newCall(request);
        Response response;
        try {
            response = call.execute();
        } catch (IOException e) {
            System.out.println("execute failed, message:" + e.getMessage());
            throw new RuntimeException("execute failed, message:" + e.getMessage());
        }

        if (response == null || !response.isSuccessful()) {
            assert response != null;
            System.out.println("request failed----返回状态码" + response.code() + ", message:" + response.message());
            throw new RuntimeException("Request failed with status code: " + response.code());
        }

        String result;
        try {
            result = response.body().string();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read response body, message:" + e.getMessage());
        }

        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(result);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse response body, message:" + e.getMessage());
        }

        String respCode = jsonNode.get("respCode").asText();
        String respMessage = AuthenticationErrorCode.getMessageByCode(respCode);

        AuthenticationResponse authResponse;
        try {
            authResponse = objectMapper.treeToValue(jsonNode, AuthenticationResponse.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert JSON to AuthenticationResponse, message:" + e.getMessage());
        }

        authResponse.setRespMessage(respMessage);

        return authResponse;
    }

    /**
     * 对上传的身份证图片进行识别，识别成功就返回识别成功的参数
     *
     * @param file 上传的身份证图片
     * @return 返回成功消息
     */
    public AuthenticationErrorCode AuthId(MultipartFile file) {
        UserIdentityVerification verification = uidmapper.selectOne(new LambdaQueryWrapper<UserIdentityVerification>()
                .eq(UserIdentityVerification::getUserId, LoginUtil.getLoginIdAsLong()));
        if (verification != null) {
            throw new BusinessException("不能重复实名认证！");
        }
        // 对图片进行上传
        if (file != null) {
            // 上传图片
            FileMap fileName = fmService.saveLocalFile(file);
            // 图片上传之后，对图片进行识别, 识别正面
            String filePath = fmService.getLocalFilePath(fileName);
            Map<String, Object> faceFes = recognizeIDCard(filePath, "face");
            AuthenticationResponse authRes;
            AuthenticationErrorCode code = AuthenticationErrorCode.UNKNOWN_ERROR;
            try {
                // 对图片中的身份信息进行识别
                authRes = authenticate((String) faceFes.get("name"), (String) faceFes.get("num"));
                code = AuthenticationErrorCode.fromCode(authRes.getRespCode());
                // 判断是否验证成功
                if (code.getCode().equals("0000")) {
                    // 如果验证成功储存该验证信息
                    var data = new UserIdentityVerification()
                            .setJson(JSONUtil.toJsonStr(faceFes))
                            .setUserId(LoginUtil.getLoginIdAsLong())
                            .setRealName((String) faceFes.get("name"))
                            .setRealSex((String) faceFes.get("sex"))
                            .setIdentity((String) faceFes.get("num"))
                            .setDomicile((String) faceFes.get("address"))
                            .setBirthday((String) faceFes.get("birth"))
                            .setIsFake((Boolean) faceFes.get("is_fake"))
                            .setNationality((String) faceFes.get("nationality"));
                    // 如果插入成功，更新用户状态
                    if(uidmapper.insert(data) > 0) {
                        var wrapper = new LambdaUpdateWrapper<Users>();
                        wrapper.set(Users::getIsRealName, true)
                                .eq(Users::getId, LoginUtil.getLoginIdAsLong());
                        usersMapper.update(wrapper);
                    }
                }
            } catch (Exception e) {
                log.error("身份验证失败： {}", e.getMessage());
            }
            return code;
        }
        return AuthenticationErrorCode.UNKNOWN_ERROR;
    }

    /**
     * 判断该当前用户是否经过实名认证
     */
    public boolean isRealName() {
        var query = new LambdaQueryWrapper<UserIdentityVerification>()
                .eq(UserIdentityVerification::getUserId, LoginUtil.getLoginIdAsLong());
        var userIdEn = uidmapper.selectOne(query);
        return userIdEn != null;
    }
}
