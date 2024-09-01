package cn.iocoder.yudao.module.common.encrypt;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.symmetric.AES;

import java.nio.charset.StandardCharsets;

/**
 * AES、RSA 加密&解密
 *
 * @author hjp
 * @since 2024-07-10
 */
public class GlobalEncryptHelper {

    /**
     * AES签名加密解密
     */
    private static AES aes;

    public static void init() {
        // 初始化AES
        aes = SecureUtil.aes("acdtr98lnsmvftrvoafg".getBytes(StandardCharsets.UTF_8));
    }

    /**
     * RSA加密
     *
     * @param text    明文
     * @param keyType 秘钥类型
     * @return 密文
     */
    public static String rsaEncoder(RSA rsa, String text, KeyType keyType) {
        return rsa.encryptBase64(text, CharsetUtil.CHARSET_UTF_8, keyType);
    }

    /**
     * RSA解密
     *
     * @param text    密文
     * @param keyType 秘钥类型
     * @return 明文
     */
    public static String rsaDecoder(RSA rsa, String text, KeyType keyType) {
        return rsa.decryptStr(text, keyType, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * AES加密
     *
     * @param text 明文
     * @return 密文
     */
    public static String aesEncoder(String text) {
        return aes.encryptBase64(text, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * AES解密
     *
     * @param text 密文
     * @return 明文
     */
    public static String aesDecoder(String text) {
        return aes.decryptStr(text, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 密码处理：先RSA解密，后AES加密
     *
     * @param text RSA加密的文本
     * @return AES加密的文本
     */
    public static String pwdRsaDeToAesEn(RSA rsa, String text) {
        text = GlobalEncryptHelper.rsaDecoder(rsa, text, KeyType.PrivateKey);
        return GlobalEncryptHelper.aesEncoder(text);
    }

}
