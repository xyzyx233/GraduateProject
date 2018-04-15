package edu.andr.xyzyx.MyUtil;

import android.content.Context;
import android.icu.util.Output;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by asus on 2018/4/15.
 */

public class FilerHelper {
    public Context context;
    public FilerHelper(Context c){
        this.context=c;
    }
    public InputStream getFileInputStream(String path) throws FileNotFoundException {
        File file=new File(path);
        FileInputStream fileInputStream=new FileInputStream(file);
        return fileInputStream;
    }
    public OutputStream getFileOutputStream(String path) throws FileNotFoundException {
        File file=new File(path);
        FileOutputStream fos=new FileOutputStream(file);
        return fos;
    }
    public String readSDCardFile(String path) {

        File file = new File(path);

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            String result = streamRead(fis);
            return result;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    public String readRawFile(int fileId)  {

        // 取得输入流

        InputStream is = context.getResources().openRawResource(fileId);
        String result = streamRead(is);// 返回一个字符串
        return result;

    }



    private String streamRead(InputStream is)  {

        int buffersize = 0;// 取得输入流的字节长度
        try {
            buffersize = is.available();
            byte buffer[] = new byte[buffersize];
            is.read(buffer);// 将数据读入数组
            is.close();// 读取完毕后要关闭流。
            String result = new String(buffer, "UTF-8");// 设置取得的数据编码，防止乱码
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }



    // 在assets文件夹下的文件，同样是只能读取不能写入

    public String readAssetsFile(String filename) throws IOException {

        // 取得输入流
        InputStream is = null;
            is = context.getResources().getAssets().open(filename);
        String result = streamRead(is);// 返回一个字符串
        return result;

    }



    // 往sd卡中写入文件

    public void writeSDCardFile(String path, byte[] buffer)  {

        File file = new File(path);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(buffer);// 写入buffer数组。如果想写入一些简单的字符，可以将String.getBytes()再写入文件;
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    // 将文件写入应用的data/data的files目录下

    public void writeDateFile(String fileName, byte[] buffer) throws IOException {

        byte[] buf = fileName.getBytes();
        fileName = new String(buf, "utf-8");
        // Context.MODE_PRIVATE：为默认操作模式，代表该文件是私有数据，只能被应用本身访问，在该模式下，写入的内容会覆盖原文件的内容，如果想把新写入的内容追加到原文件中。可以使用Context.MODE_APPEND
        // Context.MODE_APPEND：模式会检查文件是否存在，存在就往文件追加内容，否则就创建新文件。
        // Context.MODE_WORLD_READABLE和Context.MODE_WORLD_WRITEABLE用来控制其他应用是否有权限读写该文件。
        // MODE_WORLD_READABLE：表示当前文件可以被其他应用读取；MODE_WORLD_WRITEABLE：表示当前文件可以被其他应用写入。
        // 如果希望文件被其他应用读和写，可以传入：
        // openFileOutput("output.txt", Context.MODE_WORLD_READABLE +
        // Context.MODE_WORLD_WRITEABLE);
        FileOutputStream fos = context.openFileOutput(fileName,Context.MODE_APPEND);// 添加在文件后面
        fos.write(buffer);
        fos.close();


    }



    // 读取应用的data/data的files目录下文件数据

    public String readDateFile(String fileName) throws Exception {

        FileInputStream fis = context.openFileInput(fileName);

        String result = streamRead(fis);// 返回一个字符串

        return result;

    }
}
