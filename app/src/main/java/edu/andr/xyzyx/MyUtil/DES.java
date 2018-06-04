package edu.andr.xyzyx.MyUtil;


import android.util.Log;

import org.spongycastle.crypto.BufferedBlockCipher;
import org.spongycastle.crypto.CryptoException;
import org.spongycastle.crypto.engines.BlowfishEngine;
import org.spongycastle.crypto.modes.CBCBlockCipher;
import org.spongycastle.crypto.modes.PaddedBlockCipher;
import org.spongycastle.crypto.params.KeyParameter;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by asus on 2018/3/3.
 */

public class DES {
    private BufferedBlockCipher cipher;
    private KeyParameter key;
    private byte[] rr={1,2,3,4,5,6,7,8};
    public DES(){
    }
    public DES( byte[] key ){
        /*
        cipher = new PaddedBlockCipher(
                 new CBCBlockCipher(
                 new DESEngine() ) );
         */

        cipher = new PaddedBlockCipher(
                new CBCBlockCipher(
                        new BlowfishEngine() ) );

        this.key = new KeyParameter( key );
    }

    // 初始化加密引擎.
    // 字符串key的长度至少应该是8个字节.

    public DES( String key ){
        this( key.getBytes() );
    }
    // 做加密解密的具体工作

    private byte[] callCipher( byte[] data ) throws CryptoException {
        int size =cipher.getOutputSize( data.length );
        byte[] result = new byte[ size ];
        int olen = cipher.processBytes( data, 0,data.length, result, 0 );
        olen += cipher.doFinal( result, olen );
        if( olen < size ){
            byte[] tmp = new byte[ olen ];
            System.arraycopy(result, 0, tmp, 0, olen );
            result = tmp;
        }

        return result;
    }

    // 加密任意的字节数组，以字节数组的方式返回被加密的数据

    public synchronized byte[] encrypt( byte[] data ) throws CryptoException {
        if( data == null || data.length == 0 ){
            return new byte[0];
        }
        cipher.init( true, key );
        return callCipher( data );
    }

    // 加密一个字符串

    public byte[] encryptString( String data )
            throws CryptoException {
        if( data == null || data.length() == 0 ){
            return new byte[0];
        }

        return encrypt( data.getBytes() );
    }

    // 解密一个字节数组

    public synchronized byte[] decrypt( byte[] data )
            throws CryptoException {
        if( data == null || data.length == 0 ){
            return new byte[0];
        }

        cipher.init( false, key );
        return callCipher( data );
    }

    // 解密一个字符串

    public String decryptString( byte[] data )
            throws CryptoException {
        if( data == null || data.length == 0 ){
            return "";
        }

        return new String( decrypt( data ) );
    }
    public  byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("DES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG","Crypto");
        sr.setSeed(seed);
        kgen.init(56, sr);
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }

    public  byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(
                new byte[cipher.getBlockSize()]));
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    public   byte[] decrypt(byte[] raw, byte[] encrypted)
            throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "DES");
        Log.i("test",skeySpec.toString());
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(
                new byte[cipher.getBlockSize()]));
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }
}
