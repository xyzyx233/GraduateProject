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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import edu.andr.xyzyx.MyUtil.ClockBean;
import edu.andr.xyzyx.MyUtil.ConstantArgument;
import edu.andr.xyzyx.MyUtil.FilerHelper;
import edu.andr.xyzyx.MyUtil.TriDES;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TriDES_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TriDES_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TriDES_fragment extends Fragment implements ConstantArgument{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String trideskey;
    private View view;
    private TextView textView;
    private Button button;
    private EditText editText;
    private TextView output;

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

//    private OnFragmentInteractionListener mListener;

    public TriDES_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TriDES_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TriDES_fragment newInstance(String param1, String param2) {
        TriDES_fragment fragment = new TriDES_fragment();
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
        view= inflater.inflate(R.layout.fragment_tri_des_fragment, container, false);
        initview();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!check())
                    return;
                TriDESThread triDESThread=new TriDESThread();
                output.setText("结果：");
                Toast.makeText(getContext(),"正在加密解密测试,请稍等....",Toast.LENGTH_SHORT).show();
                triDESThread.execute();
                button.setEnabled(false);
            }
        });
        return view;
    }

    private boolean check() {
        if(editText.getText().toString().length()<=0)
            return false;
        trideskey=editText.getText().toString();
        return true;
    }

    private void initview() {
        textView=(TextView)view.findViewById(R.id.about_tdes);
        button=(Button) view.findViewById(R.id.btn_tdes);
        editText=(EditText)view.findViewById(R.id.tdes_key);
        output=(TextView)view.findViewById(R.id.tdestest);
        Snackbar.make(getActivity().findViewById(android.R.id.content), "密钥长度为任意", Snackbar.LENGTH_INDEFINITE)
                .addCallback(callback)
                .setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .show();
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri=Uri.parse("https://zh.wikipedia.org/wiki/%E4%B8%89%E9%87%8D%E8%B3%87%E6%96%99%E5%8A%A0%E5%AF%86%E6%BC%94%E7%AE%97%E6%B3%95");
                Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });
    }

    class TriDESThread extends AsyncTask<String,ClockBean,String> {
        @Override
        protected String doInBackground(String... strings) {
            TriDES triDES=new TriDES();
            FilerHelper filerHelper=new FilerHelper(getContext());
            String in="";
            for (int i=0;i<TEST_FILE_NUM;i++) {
                try {
                    in = filerHelper.readAssetsFile(TESTFILE[i]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                long startTime= System.currentTimeMillis();
                byte[] m = triDES.encryptMode(in.getBytes(), trideskey);
                long endTime = System.currentTimeMillis();
                byte[] r=triDES.decryptMode(m, trideskey);
                long finishTime = System.currentTimeMillis();
                String result = new String(r);
                ClockBean clockBean=new ClockBean(startTime,endTime,finishTime);
                clockBean.setTime(i);
                publishProgress(clockBean);
                Log.i("test","5_"+(i+1));
                Log.i("test",String.valueOf(clockBean.getEncrypt()));
                Log.i("test",String.valueOf(clockBean.getDecrypt()));
                Log.i("test","5_"+(i+1));
                try {
                    filerHelper.writeDateFile(TDESOUT[i], result.getBytes());
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
