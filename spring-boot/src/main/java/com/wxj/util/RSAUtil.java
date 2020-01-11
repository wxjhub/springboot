package com.wxj.util;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * RSA加密算法
 * @author wangxinji
 */
@Component
@PropertySource({"classpath:/rsa.properties"})
public class RSAUtil {
    private static Environment environment;
    private static String publicKeyString;
    private static String privateKeyString;
    private static Map<Integer,String> keymap = new HashMap<>();
    private final static String CYPHER="RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING";
    /**
     * 随机生成密钥对
     * @throws NoSuchAlgorithmException
     */
    public static void genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(2048,new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
        // 将公钥和私钥保存到Map
        keymap.put(0,publicKeyString);  //0表示公钥
        keymap.put(1,privateKeyString);  //1表示私钥
    }

    /**
     * RSA公钥加密
     *
     * @param str
     *            加密字符串
     * @return 密文
     * @throws Exception
     *             加密过程中的异常信息
     */
    public static String encrypt( String str) throws Exception{
        String configFilePath = System.getProperty("user.dir")+ File.separator+"rsa.properties";
        InputStreamReader i = new InputStreamReader(new FileInputStream(new File(configFilePath)));
        Properties p = new Properties();
        p.load(i);
        publicKeyString = p.getProperty("publicKeyString");
        //base64编码的公钥
        //publicKeyString = environment.getProperty("publicKeyString");
        byte[] decoded = Base64.decodeBase64(publicKeyString);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance(CYPHER);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    /**
     * RSA私钥解密
     *
     * @param str
     *            加密字符串
     *      *            私钥
     * @return 铭文
     * @throws Exception
     *             解密过程中的异常信息
     */
    public static String decrypt(String str) throws Exception{
        String configFilePath = System.getProperty("user.dir")+ File.separator+"rsa.properties";
        InputStreamReader i = new InputStreamReader(new FileInputStream(new File(configFilePath)));
        Properties p = new Properties();
        p.load(i);
        privateKeyString = p.getProperty("privateKeyString");
        //privateKeyString = environment.getProperty("privateKeyString");
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKeyString);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance(CYPHER);
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte),"UTF-8");
        return outStr;
    }

    public static void main(String[] args) throws Exception {
        //生成公钥和私钥
        genKeyPair();
        //加密字符串
        String message = "123456";
        System.out.println("随机生成的公钥为:" + keymap.get(0));
        System.out.println("随机生成的私钥为:" + keymap.get(1));
        String messageEn = encrypt(message);
        System.out.println(message + "\t加密后的字符串为:" + messageEn);
        String messageDe = decrypt(messageEn);
        System.out.println("还原后的字符串为:" + messageDe);
    }
}
