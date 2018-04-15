package edu.andr.xyzyx.MyUtil;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by asus on 2018/3/3.
 */

public class TriDES {
    // 定义加密算法，DESede即3DES
    private static final String Algorithm = "DESede";
    // 加密密钥
//    private static final String  Trikey= "zhaokaiqiang1992";

    /**
     * 加密方法
     *
     * @param src
     *            源数据的字节数组
     * @return
     */
    public byte[] encryptMode(byte[] src,String Trikey) {
        try {
            // 生成密钥
            SecretKey deskey = new SecretKeySpec(
                    build3DesKey(Trikey), Algorithm);
            // 实例化Cipher
            Cipher cipher = Cipher.getInstance(Algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, deskey);
            return cipher.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     * 解密函数
     *
     * @param src
     *            密文的字节数组
     * @return
     */
    public byte[] decryptMode(byte[] src,String Trikey) {
        try {
            SecretKey deskey = new SecretKeySpec(
                    build3DesKey(Trikey), Algorithm);
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     * 根据字符串生成密钥24位的字节数组
     *
     * @param keyStr
     * @return
     * @throws UnsupportedEncodingException
     */
    public byte[] build3DesKey(String keyStr)
            throws UnsupportedEncodingException {
        byte[] key = new byte[24];
        byte[] temp = keyStr.getBytes("UTF-8");

        if (key.length > temp.length) {
            System.arraycopy(temp, 0, key, 0, temp.length);
        } else {
            System.arraycopy(temp, 0, key, 0, key.length);
        }
        return key;
    }
}
