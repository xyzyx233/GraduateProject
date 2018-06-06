package edu.andr.xyzyx.MyUtil;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by asus on 2018/6/4.
 */

public class RSAwithKey implements ConstantArgument{

    private PublicKey publicKey = null;
    private PrivateKey privateKey = null;
    private InputStream inPublic = null;
    private InputStream inPrivate = null;

    public void encryptkey(String pubkeypath, String filepath, Context context,String source) {
        RSA rsa = new RSA();
        FilerHelper filerHelper = new FilerHelper(context);
        try {
            inPublic = filerHelper.getFileInputStreamR(PUBKEY);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            publicKey = rsa.loadPublicKey(inPublic);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 加密
        byte[] encryptByte = new byte[0];
        try {
            encryptByte = rsa.encryptByPublicKeyForSpilt(source.getBytes(), publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 为了方便观察吧加密后的数据用base64加密转一下，要不然看起来是乱码,所以解密是也是要用Base64先转换
        String afterencrypt = new String(Base64.encode(encryptByte, Base64.DEFAULT));
//        Log.i("testx",afterencrypt);
        filerHelper.writeSDCardFile(filepath,afterencrypt.getBytes());
    }
    public String decryptkey(String prikeypath, String filepath, Context context,String source){
        RSA rsa = new RSA();
        FilerHelper filerHelper = new FilerHelper(context);
        byte[] rawkey = Base64.decode(filerHelper.readSDCardFile(filepath), Base64.DEFAULT);
        try {
            inPrivate = filerHelper.getFileInputStreamR(PRIKEY);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            privateKey = rsa.loadPrivateKey(inPrivate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] decryptByte = new byte[0];
        try {
            decryptByte = rsa.decryptByPrivateKeyForSpilt(rawkey, privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(decryptByte);
    }
}
