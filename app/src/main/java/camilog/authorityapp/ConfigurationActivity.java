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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;


public class ConfigurationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        intent.putExtra("SCAN_CAMERA_ID", 0);

        startActivityForResult(intent, 0);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK){
                String authPrivateKeyString = intent.getStringExtra("SCAN_RESULT");

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

                } catch (Exception e) {}

                Intent intent2 = new Intent(ConfigurationActivity.this, MainActivity.class);
                startActivity(intent2);

            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
                Toast toast = Toast.makeText(this, "Scan was Cancelled!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
                finish();

            }
        }
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
