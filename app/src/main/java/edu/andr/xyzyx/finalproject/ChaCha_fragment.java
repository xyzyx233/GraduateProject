package edu.andr.xyzyx.finalproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

import edu.andr.xyzyx.MyUtil.ChaCha;
import edu.andr.xyzyx.MyUtil.ClockBean;
import edu.andr.xyzyx.MyUtil.ConstantArgument;
import edu.andr.xyzyx.MyUtil.FilerHelper;
import edu.andr.xyzyx.MyUtil.GetChachaKeyandIV;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChaCha_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChaCha_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChaCha_fragment extends Fragment implements ConstantArgument{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private byte[] key,iv;
    private View view;
    private TextView textView,output;
    private Button button;
    private EditText chachakey;

    private Snackbar.Callback callback=new Snackbar.Callback() {
        @Override
        public void onDismissed(Snackbar snackbar, int event) {
            chachakey.setEnabled(true);
        }

        @Override
        public void onShown(Snackbar snackbar) {
            chachakey.setEnabled(false);
        }
    };

    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what>=0&&msg.what<TEST_FILE_NUM) {
                //do something,refresh UI;
                int x=msg.what+1;
                ClockBean clockBean = (ClockBean) msg.obj;
                output.append("\n");
                output.append("第"+x+"个测试文件加密用时:");
                output.append(String.valueOf(clockBean.getDecrypt()));
                output.append("ms\n");
                output.append("第"+x+"个测试文件解密用时:");
                output.append(String.valueOf(clockBean.getDecrypt())+"ms");
            }
            if(msg.what==6){
                output.append("\n");
                output.append("测试结束.");
                button.setEnabled(true);
            }
        }

    };

//    private OnFragmentInteractionListener mListener;

    public ChaCha_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChaCha_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChaCha_fragment newInstance(String param1, String param2) {
        ChaCha_fragment fragment = new ChaCha_fragment();
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
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_cha_cha_fragment, container, false);
        initview();
        chachakey.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String tkey=chachakey.getText().toString();
                    if(tkey.getBytes().length==0)
                        Snackbar.make(v,"密钥格式错误",Snackbar.LENGTH_INDEFINITE)
                        .setAction("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                chachakey.setFocusable(true);
                            }
                        })
                        .addCallback(callback).show();
                    key=tkey.getBytes();
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!check()){
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "密钥为空", Snackbar.LENGTH_INDEFINITE)
                            .setAction("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    chachakey.setFocusable(true);
                                }
                            })
                            .show();
                    return;
                }
//                Log.i("teSt",GetChachaKeyandIV.sha(chachakey.getText().toString()));
//                Log.i("teSt",String.valueOf(GetChachaKeyandIV.sha(chachakey.getText().toString()).length()));
                key=GetChachaKeyandIV.sha(chachakey.getText().toString()).substring(0, 32).getBytes();
                iv=GetChachaKeyandIV.sha(chachakey.getText().toString()).substring(32, 40).getBytes();
//                Log.i("teSt",new String(key));
//                Log.i("teSt",String.valueOf(key.length));
//                Log.i("teSt",new String(iv));
//                Log.i("teSt",String.valueOf(iv.length));
                ChachaThread chachaThread=new ChachaThread();
                output.setText("结果：");
                Toast.makeText(getContext(),"正在加密解密测试,请稍等....",Toast.LENGTH_SHORT).show();
                chachaThread.execute();
                button.setEnabled(false);
            }
        });
        return view;
    }

    private boolean check() {
        if(chachakey.getText().toString().length()==0)
            return false;
        return true;
    }

    private void initview() {
        textView=(TextView)view.findViewById(R.id.about_chacha);
        button=(Button)view.findViewById(R.id.btn_chacha);
        chachakey=(EditText)view.findViewById(R.id.chacha_key);
        output=(TextView)view.findViewById(R.id.chachatest);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri=Uri.parse("https://zh.wikipedia.org/wiki/Salsa20#ChaCha20");
                Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });
        Snackbar.make(getActivity().findViewById(android.R.id.content), "密钥长度任意", Snackbar.LENGTH_INDEFINITE)
                .addCallback(callback)
                .setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chachakey.setEnabled(true);
                    }
                })
                .show();

    }
    class ChachaThread extends AsyncTask<String,ClockBean,String>{
        @Override
        protected String doInBackground(String... strings) {
            String in="",out;
            ByteArrayOutputStream m=null;
            InputStream fis=null,isDec=null;
            OutputStream fos=null;
            ByteArrayOutputStream osEnc= new ByteArrayOutputStream();
            ChaCha chaCha=new ChaCha();
//            FilerHelper filerHelper=new FilerHelper(getContext());
//            try {
//                chaCha.chachaString(filerHelper.readAssetsFile(TESTFILE[0]).getBytes(),key,iv);
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//            }
            FilerHelper filerHelper=new FilerHelper(getContext());
            for (int i=0;i<TEST_FILE_NUM;i++) {
                try {
                    in = filerHelper.readAssetsFile(TESTFILE[i]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                long startTime= System.currentTimeMillis();
                fis = new ByteArrayInputStream(in.getBytes());
                ByteArrayOutputStream osDec = new ByteArrayOutputStream();
                long endTime=0;
                long finishTime=0;
                try {
                    chaCha.encChaCha(fis, osEnc, key, iv);
                    endTime= System.currentTimeMillis();
                    isDec = new ByteArrayInputStream(osEnc.toByteArray());
                    chaCha.decChaCha(isDec, osDec, key, iv);
                    finishTime = System.currentTimeMillis();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ClockBean clockBean=new ClockBean(startTime,endTime,finishTime);
                clockBean.setTime(i);
                publishProgress(clockBean);
                Log.i("test","3_"+(i+1));
                Log.i("test",String.valueOf(clockBean.getEncrypt()));
                Log.i("test",String.valueOf(clockBean.getDecrypt()));
                Log.i("test","3_"+(i+1));
//            Log.i("test",new String(osDec.toByteArray()));
                try {
                    filerHelper.writeDateFile(CHAOUT[i], osDec.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
