package edu.andr.xyzyx.MyUtil;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by asus on 2018/6/4.
 */

public class ChaChaFile {
    private String infile;
    private String outfile;
    private byte[] key;
    private byte[] iv;
    private Context context;

    public void encryptfile() throws IOException {
        ChaCha chaCha=new ChaCha();
        FilerHelper filerHelper=new FilerHelper(context);
        chaCha.encChaCha(filerHelper.getFileInputStream(infile),filerHelper.getFileOutputStream(outfile),key,iv);
    }
    public void decryptfile() throws IOException{
        ChaCha chaCha=new ChaCha();
        FilerHelper filerHelper=new FilerHelper(context);
        chaCha.decChaCha(filerHelper.getFileInputStream(infile),filerHelper.getFileOutputStream(outfile),key,iv);
    }
}
