package edu.andr.xyzyx.MyUtil;

import android.content.Context;

import java.io.UnsupportedEncodingException;

/**
 * Created by asus on 2018/6/4.
 */

public class TriDESFile {
    private String infile;
    private String outfile;
    private String TriDESkey;
    private Context context;

    public TriDESFile(String infile, String outfile, String triDESkey, Context context) {
        this.infile = infile;
        this.outfile = outfile;
        TriDESkey = triDESkey;
        this.context = context;
    }

    public void encryptfile() throws UnsupportedEncodingException {
        TriDES triDES=new TriDES();
        FilerHelper filerHelper=new FilerHelper(context);
        byte[] in =filerHelper.readfilewithbytes(infile);
        byte[] cipher=triDES.encryptMode(in,TriDESkey);
        filerHelper.writeilewithbytes(outfile,cipher);
    }
    public void decryptfile(){
        TriDES triDES=new TriDES();
        FilerHelper filerHelper=new FilerHelper(context);
        byte[] in =filerHelper.readfilewithbytes(infile);
        byte[] cipher=triDES.decryptMode(in,TriDESkey);
        filerHelper.writeilewithbytes(outfile,cipher);
    }
}
