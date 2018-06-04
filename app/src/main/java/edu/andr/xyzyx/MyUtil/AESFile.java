package edu.andr.xyzyx.MyUtil;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;

/**
 * Created by asus on 2018/6/4.
 */

public class AESFile {
    private Key key;

    /**
     * 生成AES对称秘钥
     */
    private String generateKey(String seed) throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        sr.setSeed(seed.getBytes());
        keygen.init(128, sr);
        this.key = keygen.generateKey();
        return "Algorithm Format Encoded:" + key.getAlgorithm() + " - " + key.getFormat() + " - " + new String(key.getEncoded());
    }

    /**
     * 加密
     */
    private void encrypt(InputStream in) throws InvalidKeyException, ShortBufferException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IOException {
        this.crypt(in, null, Cipher.ENCRYPT_MODE);
    }

    /**
     * 解密
     */
    private String decrypt(InputStream in) throws InvalidKeyException, ShortBufferException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IOException {
        return this.crypt(in, Cipher.DECRYPT_MODE);
    }

    /**
     * 加密
     */
    private void encrypt(InputStream in, OutputStream out) throws InvalidKeyException, ShortBufferException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IOException {
        this.crypt(in, out, Cipher.ENCRYPT_MODE);
    }

    /**
     * 解密
     */
    private void decrypt(InputStream in, OutputStream out) throws InvalidKeyException, ShortBufferException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IOException {
        this.crypt(in, out, Cipher.DECRYPT_MODE);
    }

    /**
     * 实际的加密解密过程
     */
    private void crypt(InputStream in, OutputStream out, int mode) throws IOException, ShortBufferException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(mode, this.key);

        int blockSize = cipher.getBlockSize();
        int outputSize = cipher.getOutputSize(blockSize);
        byte[] inBytes = new byte[blockSize];
        byte[] outBytes = new byte[outputSize];

        int inLength = 0;
        boolean more = true;
        while (more) {
            inLength = in.read(inBytes);
            if (inLength == blockSize) {   //只要输入数据块具有全长度（长度可被8整除），调用update方法
                int outLength = cipher.update(inBytes, 0, blockSize, outBytes);
                if (out != null) out.write(outBytes, 0, outLength);
            } else {
                more = false;
            }
        }
        if (inLength > 0)   //不具有全长度，调用doFinal方法
            outBytes = cipher.doFinal(inBytes, 0, inLength);
        else
            outBytes = cipher.doFinal();
        if (out != null) {
            out.write(outBytes);
            out.flush();
        }
    }

    /**
     * 实际的加密解密过程
     */
    private String crypt(InputStream in, int mode) throws IOException, ShortBufferException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(mode, this.key);

        int blockSize = cipher.getBlockSize();
        int outputSize = cipher.getOutputSize(blockSize);
        byte[] inBytes = new byte[blockSize];
        byte[] outBytes = new byte[outputSize];

        int inLength = 0;
        boolean more = true;
        StringBuilder sb = new StringBuilder();
        while (more) {
            inLength = in.read(inBytes);
            if (inLength == blockSize) {   //只要输入数据块具有全长度（长度可被8整除），调用update方法
                int outLength = cipher.update(inBytes, 0, blockSize, outBytes);
            } else {
                more = false;
            }
        }
        if (inLength > 0)   //不具有全长度，调用doFinal方法
            outBytes = cipher.doFinal(inBytes, 0, inLength);
        else
            outBytes = cipher.doFinal();
        sb.append(new String(outBytes));
        return sb.toString();
    }


    public void setKey(Key key) {
        this.key = key;
    }

    public Key getKey() {
        return key;
    }
    public void en(String in, String out, String seed, Context context) throws NoSuchProviderException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, ShortBufferException, NoSuchPaddingException, InvalidKeyException {
        generateKey(seed);
        FilerHelper filerHelper=new FilerHelper(context);
        encrypt(filerHelper.getFileInputStream(in),filerHelper.getFileOutputStream(out));
    }
    public void de(String in,String out,String seed, Context context) throws NoSuchProviderException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, ShortBufferException, NoSuchPaddingException, InvalidKeyException {
        generateKey(seed);
        FilerHelper filerHelper=new FilerHelper(context);
        decrypt(filerHelper.getFileInputStream(in),filerHelper.getFileOutputStream(out));
    }
}
