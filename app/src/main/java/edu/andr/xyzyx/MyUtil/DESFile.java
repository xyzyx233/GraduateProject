package edu.andr.xyzyx.MyUtil;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by asus on 2018/6/4.
 */

public class DESFile {
    /**加密解密的key*/
    private Key mKey;
    /**解密的密码*/
    private Cipher mDecryptCipher;
    /**加密的密码*/
    private Cipher mEncryptCipher;

    public DESFile(String key) throws Exception
    {
        initKey(key);
        initCipher();
    }

    /**
     * 创建一个加密解密的key
     * @param keyRule
     */
    public void initKey(String keyRule) {
        byte[] keyByte = keyRule.getBytes();
        // 创建一个空的八位数组,默认情况下为0
        byte[] byteTemp = new byte[8];
        // 将用户指定的规则转换成八位数组
        for (int i = 0; i < byteTemp.length && i < keyByte.length; i++) {
            byteTemp[i] = keyByte[i];
        }
        mKey = new SecretKeySpec(byteTemp, "DES");
    }

    /***
     * 初始化加载密码
     * @throws Exception
     */
    private void initCipher() throws Exception
    {
        mEncryptCipher = Cipher.getInstance("DES");
        mEncryptCipher.init(Cipher.ENCRYPT_MODE, mKey);

        mDecryptCipher = Cipher.getInstance("DES");
        mDecryptCipher.init(Cipher.DECRYPT_MODE, mKey);
    }

    /**
     * 加密文件
     * @param in
     * @param savePath 加密后保存的位置
     */
    private void doEncryptFile(InputStream in, String savePath)
    {
        if(in==null)
        {
            System.out.println("inputstream is null");
            return;
        }
        try {
            CipherInputStream cin = new CipherInputStream(in, mEncryptCipher);
            OutputStream os = new FileOutputStream(savePath);
            byte[] bytes = new byte[1024];
            int len = -1;
            while((len=cin.read(bytes))>0)
            {
                os.write(bytes, 0, len);
                os.flush();
            }
            os.close();
            cin.close();
            in.close();
            System.out.println("加密成功");
        } catch (Exception e) {
            System.out.println("加密失败");
            e.printStackTrace();
        }
    }

    /**
     * 加密文件
     * @param filePath 需要加密的文件路径
     * @param savePath 加密后保存的位置
     * @throws FileNotFoundException
     */
    public void doEncryptFile(String filePath,String savePath) throws IOException
    {
        InputStream is=new FileInputStream(filePath);
        doEncryptFile(is, savePath);
        is.close();
    }


    /**
     * 解密文件
     * @param in
     */
    private void doDecryptFile(InputStream in,String savePath)
    {
        if(in==null)
        {
            System.out.println("inputstream is null");
            return;
        }
        try {
            CipherInputStream cin = new CipherInputStream(in, mDecryptCipher);
//            BufferedReader reader = new BufferedReader(new InputStreamReader(cin)) ;
//            String line = null;
            OutputStream os = new FileOutputStream(savePath);
            int bytesRead=0;
            byte[] bytes = new byte[1024];
            while ((bytesRead = cin.read(bytes, 0, 1024)) != -1) {
                os.write(bytes, 0, bytesRead);
            }
            os.close();
//            while((line=reader.readLine())!=null)
//            {
//                os.write(line.getBytes(), 0, line.length());
//                os.flush();
//            }
//            reader.close();
            cin.close();
            in.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 解密文件
     * @param filePath  文件路径
     * @throws Exception
     */
    public void doDecryptFile(String filePath,String savePath) throws Exception
    {
        InputStream is=new FileInputStream(filePath);
        doDecryptFile(is,savePath);
//        is.close();
    }
}
