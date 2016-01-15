package camilog.authorityapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Object;


public class ConfigurationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        Intent intent = new Intent(ConfigurationActivity.this, FileChooserActivity.class);
        startActivityForResult(intent,1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        File keyPath;
        String keyInfo;
        BufferedReader reader;
        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                keyPath = new File(intent.getStringExtra("keyPath"));
                try {
                    keyInfo = readFile(keyPath);
                    configureAuthKey(keyInfo);
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    private String readFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader (file));
        String line;

        try {
            line = reader.readLine();
            return line;
        } finally {
            reader.close();
        }
    }

    private void configureAuthKey(String authPrivateKeyString) {
        File authPrivateKeyDir = getApplicationContext().getDir("authPrivateKey", Context.MODE_PRIVATE);
        File authPrivateKeyFile = new File(authPrivateKeyDir, "authPrivateKey.key");

        try {
            if (authPrivateKeyFile.exists())
                authPrivateKeyFile.delete();

            authPrivateKeyFile.createNewFile();

            BufferedWriter writer = new BufferedWriter(new FileWriter(authPrivateKeyFile, true));
            writer.write(authPrivateKeyString);
            writer.close();

            Toast toast = Toast.makeText(this, "Authority Private Key recorded successfully!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent2 = new Intent(ConfigurationActivity.this, MainActivity.class);
        startActivity(intent2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_configuration, menu);
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
