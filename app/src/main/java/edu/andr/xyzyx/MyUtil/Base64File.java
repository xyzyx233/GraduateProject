package edu.andr.xyzyx.MyUtil;

import android.content.Context;
import android.util.Base64;

/**
 * Created by asus on 2018/6/4.
 */

public class Base64File {
    public void encryptfile(Context context,String infile,String outfile){
        FilerHelper filerHelper=new FilerHelper(context);
        byte[] a=filerHelper.readfilewithbytes(infile);
        filerHelper.writeSDCardFile(outfile,Base64.encode(a,Base64.DEFAULT));
    }
    public void decryptfile(Context context,String infile,String outfile){
        FilerHelper filerHelper=new FilerHelper(context);
        String a=filerHelper.readSDCardFile(infile);
        filerHelper.writeSDCardFile(outfile,Base64.decode(a,Base64.DEFAULT));
    }
}
