package com.example.hakan.androidrsa;



import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity
{


    public static PublicKey publicKey;
    public static PrivateKey privateKey;
    public static String encodlu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button enButton=(Button)findViewById(R.id.enbutton);
        final Button deButton=(Button)findViewById(R.id.debutton);
        final ImageButton pubbuton=(ImageButton)findViewById(R.id.publicbutton);
        final ImageButton prbuton=(ImageButton)findViewById(R.id.privatebutton);

        final EditText input=(EditText)findViewById(R.id.Input);
        final EditText Raw=(EditText)findViewById(R.id.raw);
        final EditText output=(EditText)findViewById(R.id.originText);


        try {

            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(1024); //1024 used for normal securities
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            final RSAPublicKeySpec rsaPubKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
            final RSAPrivateKeySpec rsaPrivKeySpec = keyFactory.getKeySpec(privateKey, RSAPrivateKeySpec.class);


            SavePreferences("publicmod",rsaPubKeySpec.getModulus().toString() );
            SavePreferences("publicexp",rsaPubKeySpec.getPublicExponent().toString() );

            SavePreferences("privatemod",rsaPrivKeySpec.getModulus().toString() );
            SavePreferences("privateexp",rsaPrivKeySpec.getPrivateExponent().toString() );


            // encodlu= encode(new String(encryptData(input.getText().toString())));

            //Encrypt Data using Public Key
            final byte[] encryptedData = encryptData(input.getText().toString());

            //Descypt Data using Private Key
            final byte[] descryptedData= decryptData(encryptedData);

            enButton.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    //Raw.setText(new String(encryptedData));
                    //Raw.setText(ReadPreferences("mod"));
                    try {
                        //Raw.setText(new String(encryptData(input.getText().toString())));
                        Raw.setText(encode((encryptData(input.getText().toString()))));

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    };

                }
            });

            deButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    //output.setText(new String(descryptedData));
                    try {
                        // output.setText(new String(decryptData((encryptData(input.getText().toString())))));
                        byte[] kk=(decode(encode((encryptData(input.getText().toString())))));
                        //output.setText(kk);
                        //byte[] bytes = kk.getBytes("UTF-8");
                        //Log.e("data", new String(decryptData(kk.getBytes())));
                        output.setText(new String(decryptData(kk)));
                        //output.setText((decryptData(decode(encode(new String(encryptData(input.getText().toString())))).getBytes())).toString());
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });

            pubbuton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent=new Intent(MainActivity.this,Keys.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("gidecekpubmod",rsaPubKeySpec.getModulus().toString() );
                    bundle.putString("gidecekpubexp",rsaPubKeySpec.getPublicExponent().toString() );
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            prbuton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent=new Intent(MainActivity.this,Keys2.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("gidecekprivatemod",rsaPrivKeySpec.getModulus().toString() );
                    bundle.putString("gidecekprivateexp",rsaPrivKeySpec.getPrivateExponent().toString() );
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }catch (InvalidKeySpecException e) {
            e.printStackTrace();

            //	} catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    private void SavePreferences(String key, String value) {
        // shared preferences ile keys adı altında bir tane xml dosya açıyor
        SharedPreferences sharedPreferences = getSharedPreferences("keys",
                MODE_PRIVATE);
        // dosyaya yazmamıza yardımcı olacak bir tane editör oluşturdum.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // editör aracılığı ile key value değerlerini yazdım.

        editor.putString(key, value);




        editor.commit();
    }
    private String ReadPreferences(String key) {
        // oluşturduğum SP dosyasını çağırdım.
        SharedPreferences sharedPreferences = getSharedPreferences("keys",
                MODE_PRIVATE);
        // key değerini vererek value değerini aldım.
        String strSavedMem1 = sharedPreferences.getString(key, "");



        return strSavedMem1;
    }


    public final static byte[] encryptData(String data) throws IOException {

        byte[] dataToEncrypt = data.getBytes();
        byte[] encryptedData = null;
        try {


            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            encryptedData = cipher.doFinal(dataToEncrypt);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return encryptedData;
    }

    public final static String encode(byte[] encryteddata )
    {
        // byte[] data = null;
        //try {
        //  data = encrypteddata.getBytes("UTF-8");
        // } catch (UnsupportedEncodingException e1) {
        //   e1.printStackTrace();
        //}
        String base64 = Base64.encodeToString(encryteddata, Base64.DEFAULT);
        //byte[] base64 = Base64.encode(data, Base64.DEFAULT);
        return base64;
    }

    public final static byte[] decryptData(byte[] data) throws IOException {

        byte[] descryptedData = null;

        try {

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            descryptedData = cipher.doFinal(data);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return descryptedData;

    }

    public final static byte[] decode(String encodeddata)
    {
        byte[] data1 = Base64.decode(encodeddata, Base64.DEFAULT);
        //  String text1 = null;
        // try {
        //   text1 = new String(data1, "UTF-8");
        // } catch (UnsupportedEncodingException e) {
        //   e.printStackTrace();
        //}

        return data1;
    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


