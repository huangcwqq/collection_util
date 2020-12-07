import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class SignExample {
    private static final String CHARACTER_ENCODING = "UTF-8";
    private static final String ALGORITHM = "HmacSHA256";

    public static void main(String[] args) throws Exception {
        // 修改成合作方的 appId
        String appId = "97457866";

        // 修改成合作方的密钥
        String secretKey = "GhPvxERwMW2p8a6hD4eM6N3itojRsAyuowD4ACDX9qYQQqPWB3fnl1XqceS7";

        Map<String, String> parameters = new HashMap<>();
        long a = System.currentTimeMillis();
        System.out.println("signTimestamp:"+ a);

        // 签名参数
        parameters.put("signAppId", appId);
        parameters.put("signTimestamp", String.valueOf(a));
        parameters.put("signVersion", "1");

        // 对参数进行排序
        Map<String, String> sorted = new TreeMap<>(parameters);

        StringBuilder paramData = new StringBuilder();
        Iterator<Map.Entry<String, String>> pairs = sorted.entrySet().iterator();
        while (pairs.hasNext()) {
            Map.Entry<String, String> pair = pairs.next();
            if (pair.getValue() != null) {
                paramData.append(pair.getKey()).append("=").append(pair.getValue());
            } else {
                paramData.append(pair.getKey()).append("=");
            }

            if (pairs.hasNext()) {
                paramData.append("&");
            }
        }

        // 构建签名数据
        String path = "/openapi/v1/sandbox/signTest";
        String data = path + paramData;
        String signature = sign(data, secretKey);
        System.out.println(signature);
    System.out.println(URLEncoder.encode(signature));
    }

    private static String sign(String data, String secretKey)
            throws NoSuchAlgorithmException, InvalidKeyException, IllegalStateException,
            UnsupportedEncodingException {
        Mac mac = Mac.getInstance(ALGORITHM);
        mac.init(new SecretKeySpec(secretKey.getBytes(CHARACTER_ENCODING), ALGORITHM));
        byte[] signature = mac.doFinal(data.getBytes(CHARACTER_ENCODING));
        return new String(Base64.encodeBase64(signature), CHARACTER_ENCODING);
    }
}
