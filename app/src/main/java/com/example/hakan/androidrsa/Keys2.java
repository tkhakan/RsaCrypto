package com.example.hakan.androidrsa;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import static com.example.hakan.androidrsa.R.menu.menu_keys2;

public class Keys2 extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keys2);

        final EditText prmod = (EditText) findViewById(R.id.editmodprivate);
        final EditText prexp=(EditText) findViewById(R.id.editexprivate);

        Intent i=getIntent();
        Bundle b = i.getExtras();


        String alinanprivatemod = b.getString("gidecekprivatemod");
        String alinanprivateexp= b.getString("gidecekprivateexp");



        prmod.setText(alinanprivatemod);
        prexp.setText(alinanprivateexp);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(menu_keys2, menu);
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
