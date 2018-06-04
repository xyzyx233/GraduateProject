package edu.andr.xyzyx.finalproject;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

import edu.andr.xyzyx.MyUtil.ClockBean;
import edu.andr.xyzyx.MyUtil.ConstantArgument;
import edu.andr.xyzyx.MyUtil.FilerHelper;
import edu.andr.xyzyx.MyUtil.RSA;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RSA_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RSA_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RSA_fragment extends Fragment implements ConstantArgument{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String pubkeypath="",prikeypath="";
    private View view;

    private TextView textView,output;
    private Button pubbtn,pribtn,button;

//    private OnFragmentInteractionListener mListener;

    private Snackbar.Callback callback=new Snackbar.Callback() {
        @Override
        public void onDismissed(Snackbar snackbar, int event) {
            pubbtn.setEnabled(true);
            pribtn.setEnabled(true);
        }

        @Override
        public void onShown(Snackbar snackbar) {
            pubbtn.setEnabled(false);
            pribtn.setEnabled(false);
        }
    };

    public RSA_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RSA_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RSA_fragment newInstance(String param1, String param2) {
        RSA_fragment fragment = new RSA_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_rsa_fragment, container, false);
        initview();
        pubbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent,1);
            }
        });
        pribtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent,2);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RSAThread rsaThread=new RSAThread();
                output.setText("结果：");
                Toast.makeText(getContext(),"正在加密解密测试,请稍等....",Toast.LENGTH_SHORT).show();
                rsaThread.execute();
                button.setEnabled(false);
            }
        });
        return view;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == 1) {
                Uri uri = data.getData();
                if (isExternalStorageDocument(uri)) {
                    String docId = DocumentsContract.getDocumentId(uri);
                    String[] split = docId.split(":");
                    String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        prikeypath=Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                }
                Log.i("test",prikeypath);
            }if (requestCode == 2) {
                Uri uri = data.getData();
                if (isExternalStorageDocument(uri)) {
                    String docId = DocumentsContract.getDocumentId(uri);
                    String[] split = docId.split(":");
                    String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        pubkeypath=Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                }
                Log.i("test",pubkeypath);
            }
        }
    }
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    private void initview() {
        textView=(TextView)view.findViewById(R.id.about_rsa);
        button=(Button)view.findViewById(R.id.btn_rsa);
        output=(TextView)view.findViewById(R.id.rsatest);
        pubbtn=(Button)view.findViewById(R.id.btn_pubkey);
        pribtn=(Button)view.findViewById(R.id.btn_prikey);
        Snackbar.make(getActivity().findViewById(android.R.id.content), "如不选择使用默认密钥组", Snackbar.LENGTH_INDEFINITE)
                .setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pubbtn.setFocusable(true);
                    }
                }).addCallback(callback)
                .show();
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri=Uri.parse("https://zh.wikipedia.org/wiki/RSA%E5%8A%A0%E5%AF%86%E6%BC%94%E7%AE%97%E6%B3%95");
                Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });
    }
class RSAThread extends AsyncTask<String,ClockBean,String> {
    @Override
    protected String doInBackground(String... strings) {
        PublicKey publicKey = null;
        PrivateKey privateKey=null;
        InputStream inPublic=null;
        InputStream inPrivate=null;
        FilerHelper filerHelper=new FilerHelper(getContext());
        RSA rsa=new RSA();
        String source="";
        for(int i=0;i<TEST_FILE_NUM;i++) {
            try {
                source= filerHelper.readAssetsFile(TESTFILE[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (prikeypath == "" && pubkeypath == "") {
                // 从字符串中得到公钥
                // PublicKey publicKey = RSAUtils.loadPublicKey(PUCLIC_KEY);
                // 从文件中得到公钥
                try {
                    inPublic = getResources().getAssets().open("rsa_public_key.pem");
                    inPrivate = getResources().getAssets().open("pkcs8_rsa_private_key.pem");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    inPublic = filerHelper.getFileInputStream(pubkeypath);
                    inPrivate = filerHelper.getFileInputStream(prikeypath);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            long startTime= System.currentTimeMillis();
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
            long endTime = System.currentTimeMillis();
            try {
                privateKey = rsa.loadPrivateKey(inPrivate);
            } catch (Exception e) {
                e.printStackTrace();
            }
            byte[] decryptByte = new byte[0];
            try {
                decryptByte = rsa.decryptByPrivateKeyForSpilt(Base64.decode(afterencrypt, Base64.DEFAULT), privateKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
            long finishTime = System.currentTimeMillis();
            String decryptStr = new String(decryptByte);
            ClockBean clockBean=new ClockBean(startTime,endTime,finishTime);
            clockBean.setTime(i);
            publishProgress(clockBean);
            Log.i("test","4_"+(i+1));
            Log.i("test",String.valueOf(clockBean.getEncrypt()));
            Log.i("test",String.valueOf(clockBean.getDecrypt()));
            Log.i("test","4_"+(i+1));
            try {
                filerHelper.writeDateFile(RSAOUT[i],decryptStr.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
//            Log.i("test", decryptStr);
        }
        return null;
    }
    @Override
    protected void onProgressUpdate(ClockBean... values) {
        super.onProgressUpdate(values);
        output.append("\n");
        output.append("第"+values[0].getTime()+"个测试文件加密用时:");
        output.append(String.valueOf(values[0].getEncrypt()));
        output.append("ms\n");
        output.append("第"+values[0].getTime()+"个测试文件解密用时:");
        output.append(String.valueOf(values[0].getDecrypt())+"ms");
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        output.append("\n");
        output.append("测试结束.");
        button.setEnabled(true);
    }
}
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
