package camilog.authorityapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;

import paillierp.PaillierThreshold;
import paillierp.PartialDecryption;
import paillierp.key.PaillierPrivateThresholdKey;
import paillierp.zkp.DecryptionZKP;


public class DecryptActivity extends Activity {

    static private final String multBallotsServer ="http://cjgomez.cl:5984/multiplied_ballots";
    static private final String partialDecryptionsServer = "http://cjgomez.cl:5984/partial_decryptions";
    static private final int authId = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrypt);

        new Thread() {
            public void run() {
                try {
                    downloadMultBallotsAndUploadResult();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void downloadMultBallotsAndUploadResult() throws IOException, ClassNotFoundException {
        URL obj = new URL(multBallotsServer);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.getResponseCode();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);
        in.close();

        String jsonString = response.toString();
        Gson gson = new Gson();
        MultipliedBallot[] multipliedBallots = gson.fromJson(jsonString, MultipliedBallot[].class);
        String multipliedBallotsString = multipliedBallots[0].value;

        PartialDecryption share = decryptMultipliedBallots(multipliedBallotsString);

        uploadPartialDecryption(share);
        Intent intent = new Intent(DecryptActivity.this, MainActivity.class);
        startActivity(intent);

    }

    // TODO: Verify this works
    private void uploadPartialDecryption(PartialDecryption share) throws IOException {
        // Set the URL where to POST the public key
        URL obj = new URL(partialDecryptionsServer);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // Add request header
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");

        // Create JSON with the parameters
        String urlParameters = " {\"partial_decryption\":{\"authId\": " + share.getID() + " ,\"value\": " + share.getDecryptedValue() + " }} ";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();
        con.getResponseCode();

        Toast toast = Toast.makeText(this, "Ballots partially decrypted and uploaded successfully!", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 25, 400);
        toast.show();
    }

    private PartialDecryption decryptMultipliedBallots(String multipliedBallotsString) throws IOException, ClassNotFoundException {
        PartialDecryption share;
        PaillierThreshold authPrivateKey = getAuthorityPrivateKey();

        share = authPrivateKey.decrypt(new BigInteger(multipliedBallotsString));

        return share;
    }

    private PaillierThreshold getAuthorityPrivateKey() throws IOException, ClassNotFoundException {
        PaillierThreshold authPrivateKey = null;

        if (isExternalStorageReadable()) {
            File authPrivateKeyDir = getApplicationContext().getDir("authPrivateKey", Context.MODE_PRIVATE);
            File authPrivateKeyFile = new File(authPrivateKeyDir, "authPrivateKey.key");

            if(!authPrivateKeyFile.exists()){
                //Quizás arrojar error que no existe el archivo
            }

            try {
                BufferedReader privateKeyIn = new BufferedReader(new FileReader(authPrivateKeyFile));
                String privateKeyRecoveredJson = privateKeyIn.readLine();
                PrivateKey privateKey = new Gson().fromJson(privateKeyRecoveredJson, PrivateKey.class);
                authPrivateKey = new PaillierThreshold(new PaillierPrivateThresholdKey(privateKey.n, Integer.parseInt(privateKey.l.toString()), Integer.parseInt(privateKey.w.toString()), privateKey.v, privateKey.vi, privateKey.si, Integer.parseInt(privateKey.i.toString()), new SecureRandom().nextLong()));

            } catch (IOException e){
                e.printStackTrace();
            }
        }

       return authPrivateKey;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_decrypt, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
