package edu.andr.xyzyx.finalproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.spongycastle.crypto.CryptoException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;

import edu.andr.xyzyx.MyUtil.AES;
import edu.andr.xyzyx.MyUtil.AESFile;
import edu.andr.xyzyx.MyUtil.Base64File;
import edu.andr.xyzyx.MyUtil.ChaCha;
import edu.andr.xyzyx.MyUtil.DESFile;
import edu.andr.xyzyx.MyUtil.FilerHelper;
import edu.andr.xyzyx.MyUtil.GetChachaKeyandIV;
import edu.andr.xyzyx.MyUtil.TriDES;
import edu.andr.xyzyx.MyUtil.TriDESFile;
import edu.andr.xyzyx.MyUtil.nDES;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FileCrypt_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FileCrypt_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FileCrypt_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String filepath,fileoutpath;
    private View view;
    private String[] li={"a","b","c","d","e"};
    private int pos=0;
    private Spinner selectspinner;
    private Button getfile,enc,dec;
    private EditText endekey,inen,outde;
    private TextView pathtext;
    private Switch sw;
    private boolean isencrypt=false;

//    private OnFragmentInteractionListener mListener;

    public FileCrypt_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FileCrypt_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FileCrypt_Fragment newInstance(String param1, String param2) {
        FileCrypt_Fragment fragment = new FileCrypt_Fragment();
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_file_crypt_fragment, container, false);
        initview();
        selectspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] algorithms = getResources().getStringArray(R.array.algorithms);
                pos=position;
                Toast.makeText(getContext(), "你点击的是:"+algorithms[pos], Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent,3);
            }
        });
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isencrypt=true;
                }else {
                    isencrypt=false;
                }
            }
        });
        enc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key=endekey.getText().toString();
                if(isencrypt){
                    switch (pos){
                        case 0:
                            nDES nDES=new nDES(key);
                            try {
                                DESFile desFile=new DESFile(key);
                                desFile.doEncryptFile(filepath,filepath+"."+li[pos]);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case 1:
                            TriDESFile triDESFile=new TriDESFile(filepath,filepath+li[pos],key,getContext());
                            try {
                                triDESFile.encryptfile();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                            break;
                        case 2:
                            AESFile aesFile=new AESFile();
                            try {
                                aesFile.en(filepath,filepath+"."+li[pos],key,getContext());
                            } catch (NoSuchProviderException e) {
                                e.printStackTrace();
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (BadPaddingException e) {
                                e.printStackTrace();
                            } catch (IllegalBlockSizeException e) {
                                e.printStackTrace();
                            } catch (ShortBufferException e) {
                                e.printStackTrace();
                            } catch (NoSuchPaddingException e) {
                                e.printStackTrace();
                            } catch (InvalidKeyException e) {
                                e.printStackTrace();
                            }

                            break;
                        case 3:
                            ChaCha chaCha=new ChaCha();
                            FilerHelper filerHelper=new FilerHelper(getContext());
                            byte[] keys= GetChachaKeyandIV.sha(key).substring(0, 32).getBytes();
                            byte[] iv=GetChachaKeyandIV.sha(key).substring(32, 40).getBytes();
                            try {
                                chaCha.encChaCha(filerHelper.getFileInputStream(filepath),filerHelper.getFileOutputStream(filepath+"."+li[pos]),keys,iv);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 4:
                            Base64File base64File=new Base64File();
                            base64File.encryptfile(getContext(),filepath,filepath+"."+li[pos]);
                            break;
                        default:
                            Toast.makeText(getContext(),"不懂",Toast.LENGTH_LONG);
                            break;
                    }
                }else {
                    String in= inen.getText().toString();
                    switch (pos){
                        case 0:
                            nDES nDES=new nDES(key);
                            try {
                                String out=new String(Base64.encode(nDES.encryptString(in),Base64.DEFAULT));
                                outde.setText(out);
                            } catch (CryptoException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 1:
                            TriDES triDES=new TriDES();
                            String out=new String(Base64.encode(triDES.encryptMode(in.getBytes(), key),Base64.DEFAULT));
                            outde.setText(out);
                            break;
                        case 2:
                            AES aes=new AES();
                            try {
                                byte[] rawkey = aes.getRawKey(key.getBytes());
                                String out1 = new String(Base64.encode(aes.encrypt(rawkey, in.getBytes()),Base64.DEFAULT));
                                outde.setText(out1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case 3:
                            ChaCha chaCha=new ChaCha();
                            byte[] keys= GetChachaKeyandIV.sha(key).substring(0, 32).getBytes();
                            byte[] iv=GetChachaKeyandIV.sha(key).substring(32, 40).getBytes();
                            try (InputStream isEnc = new ByteArrayInputStream(in.getBytes());
                                 ByteArrayOutputStream osEnc = new ByteArrayOutputStream())
                            {
                                chaCha.encChaCha(isEnc, osEnc, keys, iv);
                                byte[] encoded = osEnc.toByteArray();
                                outde.setText(new String(Base64.encode(encoded,Base64.DEFAULT)));

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 4:
                            outde.setText(new String(Base64.encode(in.getBytes(),Base64.DEFAULT)));
                            break;
                        default:
                            Toast.makeText(getContext(),"不懂",Toast.LENGTH_LONG);
                            break;
                    }
                }
            }
        });
        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key=endekey.getText().toString();
                if(isencrypt){
                    switch (pos){
                        case 0:
                            nDES nDES=new nDES(key);
                            try {
                                DESFile desFile=new DESFile(key);
                                desFile.doDecryptFile(filepath,filepath+"."+li[pos]);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case 1:
                            TriDESFile triDESFile=new TriDESFile(filepath,filepath+li[pos],key,getContext());
                            triDESFile.decryptfile();

                            break;
                        case 2:
                            AESFile aesFile=new AESFile();
                            try {
                                aesFile.de(filepath,filepath+"."+li[pos],key,getContext());
                            } catch (NoSuchProviderException e) {
                                e.printStackTrace();
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (BadPaddingException e) {
                                e.printStackTrace();
                            } catch (IllegalBlockSizeException e) {
                                e.printStackTrace();
                            } catch (ShortBufferException e) {
                                e.printStackTrace();
                            } catch (NoSuchPaddingException e) {
                                e.printStackTrace();
                            } catch (InvalidKeyException e) {
                                e.printStackTrace();
                            }

                            break;
                        case 3:
                            ChaCha chaCha=new ChaCha();
                            FilerHelper filerHelper=new FilerHelper(getContext());
                            byte[] keys= GetChachaKeyandIV.sha(key).substring(0, 32).getBytes();
                            byte[] iv=GetChachaKeyandIV.sha(key).substring(32, 40).getBytes();
                            try {
                                chaCha.decChaCha(filerHelper.getFileInputStream(filepath),filerHelper.getFileOutputStream(filepath+"."+li[pos]),keys,iv);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 4:
                            Base64File base64File=new Base64File();
                            base64File.decryptfile(getContext(),filepath,filepath+"."+li[pos]);
                            break;
                        default:
                            Toast.makeText(getContext(),"不懂",Toast.LENGTH_LONG);
                            break;
                    }
                }else {
                    String in= inen.getText().toString();
                    switch (pos){
                        case 0:
                            nDES nDES=new nDES(key);
                            try {
                                String out=new String(nDES.decryptString(Base64.decode(in,Base64.DEFAULT)));
                                outde.setText(out);
                            } catch (CryptoException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 1:
                            TriDES triDES=new TriDES();
                            String out=new String(triDES.decryptMode(Base64.decode(in,Base64.DEFAULT), key));
                            outde.setText(out);
                            break;
                        case 2:
                            AES aes=new AES();
                            try {
                                byte[] rawkey = aes.getRawKey(key.getBytes());
                                String out1 = new String(aes.decrypt(rawkey, Base64.decode(in,Base64.DEFAULT)));
                                outde.setText(out1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case 3:
                            ChaCha chaCha=new ChaCha();
                            byte[] keys= GetChachaKeyandIV.sha(key).substring(0, 32).getBytes();
                            byte[] iv=GetChachaKeyandIV.sha(key).substring(32, 40).getBytes();
                            try (InputStream isDec = new ByteArrayInputStream(Base64.decode(in,Base64.DEFAULT));
                                 ByteArrayOutputStream osDec = new ByteArrayOutputStream())
                            {
                                chaCha.decChaCha(isDec, osDec, keys, iv);
                                byte[] decoded = osDec.toByteArray();
                                outde.setText(new String(decoded));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 4:
                            outde.setText(new String(Base64.decode(in.getBytes(),Base64.DEFAULT)));
                            break;
                        default:
                            Toast.makeText(getContext(),"不懂",Toast.LENGTH_LONG);
                            break;
                    }

                }
            }
        });
        return view;
//        /storage/emulated/0/666/Duan/file_8858757.png
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == 3) {
                Uri uri = data.getData();
                    String docId = DocumentsContract.getDocumentId(uri);
                    String[] split = docId.split(":");
                    String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        filepath= Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                    pathtext.setText(filepath);
                Log.i("test",filepath);
            }
        }
    }
    private void initview(){
        selectspinner=view.findViewById(R.id.spinner);
        pathtext=view.findViewById(R.id.showoutfile);
        getfile=view.findViewById(R.id.getoutfile);
        enc=view.findViewById(R.id.EncBtn);
        dec=view.findViewById(R.id.decBtu);
        endekey=view.findViewById(R.id.Keyedit);
        inen=view.findViewById(R.id.inputText);
        outde=view.findViewById(R.id.outputText);
        sw=view.findViewById(R.id.switch2);
        Snackbar.make(getActivity().findViewById(android.R.id.content), "使用DES时，注意密钥长度", Snackbar.LENGTH_INDEFINITE)
                .setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .show();
    }

// // TODO: Rename method, update argument and hook method into UI event
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
