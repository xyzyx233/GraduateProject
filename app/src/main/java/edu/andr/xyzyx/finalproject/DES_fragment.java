package edu.andr.xyzyx.finalproject;

import android.content.Intent;
import android.net.Uri;
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

import org.spongycastle.crypto.CryptoException;

import java.io.IOException;

import edu.andr.xyzyx.MyUtil.ClockBean;
import edu.andr.xyzyx.MyUtil.ConstantArgument;
import edu.andr.xyzyx.MyUtil.DES;
import edu.andr.xyzyx.MyUtil.FilerHelper;
import edu.andr.xyzyx.MyUtil.RandomString;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DES_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DES_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DES_fragment extends Fragment implements ConstantArgument{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private TextView textView;
    private Button button;
    private EditText editText;
    private TextView output;

    private String deskey;

    private Snackbar.Callback callback=new Snackbar.Callback() {
        @Override
        public void onDismissed(Snackbar snackbar, int event) {
            editText.setEnabled(true);
        }

        @Override
        public void onShown(Snackbar snackbar) {
            editText.setEnabled(false);
        }
    };

    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what>0&&msg.what<6) {
                //do something,refresh UI;
                ClockBean clockBean = (ClockBean) msg.obj;
                textView.append("\n");
                textView.append("第"+msg.what+"个测试文件加密用时:");
                textView.append(String.valueOf(clockBean.getDecrypt()));
                textView.append("\n");
                textView.append("第"+msg.what+"个测试文件解密用时:");
                textView.append(String.valueOf(clockBean.getDecrypt()));
            }
            if(msg.what==6){
                textView.append("\n");
                textView.append("测试结束.");
            }
        }

    };
//    private OnFragmentInteractionListener mListener;

    public DES_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DES_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DES_fragment newInstance(String param1, String param2) {
        DES_fragment fragment = new DES_fragment();
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
        final View view= inflater.inflate(R.layout.fragment_des_fragment, container, false);
        this.view=view;
        initview();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!check())
                    return;
                DESThread desThread=new DESThread();
                desThread.run();
            }
        });
        return view;
    }

    private boolean check() {
        final String key=editText.getText().toString();
        if (key.length()!=8){
            Snackbar.make(view,"密钥非法,是否随机密钥？",Snackbar.LENGTH_INDEFINITE).setAction("确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deskey= RandomString.getRandomString(8);
                    editText.setText(deskey);
                }
            }) .addCallback(callback).show();
            return false;
        }
        deskey=key;
        return true;
    }

    private void initview(){
        textView=(TextView)view.findViewById(R.id.about_des);
        button=(Button)view.findViewById(R.id.btn_des);
        editText=(EditText)view.findViewById(R.id.des_key);
        output=(TextView)view.findViewById(R.id.destest);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri=Uri.parse("https://zh.wikipedia.org/wiki/%E8%B3%87%E6%96%99%E5%8A%A0%E5%AF%86%E6%A8%99%E6%BA%96");
                Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点
                    Snackbar.make(v, "密钥长度为8个字符", Snackbar.LENGTH_INDEFINITE)
                            .addCallback(callback)
                            .setAction("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    editText.setEnabled(true);
                                }
                            })
                            .show();
                } else {
                    // 失去焦点

                }
            }
        });

    }
class DESThread extends Thread{
    @Override
    public void run() {
        super.run();
        DES des=new DES(deskey);
        String result;
        String test;
        FilerHelper filehelper=new FilerHelper(getContext());
        for (int i=0;i<TEST_FILE_NUM;i++) {
            try {
                test = filehelper.readAssetsFile(TESTFILE[i]);
                long startTime= System.currentTimeMillis();
                byte[] b = des.encryptString(test);
                long endTime = System.currentTimeMillis();
                result = des.decryptString(b);
                long finishTime = System.currentTimeMillis();
                ClockBean clockBean=new ClockBean(startTime,endTime,finishTime);
                if(mHandler!=null){
                    Message message = mHandler.obtainMessage();
                    message.what=i;
                    message.obj=clockBean;
                    mHandler.sendMessage(message);
                }
                Log.i("test", result);
                filehelper.writeDateFile(DESOUT[i], result.getBytes());
            } catch (CryptoException e) {
                e.printStackTrace();
            } catch (IOException e) {
                Log.i("text", "no test data file import");
            }
        }
        if(mHandler!=null){
            Message message = mHandler.obtainMessage();
            message.what=6;
            mHandler.sendMessage(message);
        }
    }
}
    // TODO: Rename method, update argument and hook method into UI event
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
