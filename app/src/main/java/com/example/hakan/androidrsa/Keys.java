package com.example.hakan.androidrsa;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import static com.example.hakan.androidrsa.R.menu.menu_keys;

public class Keys extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keys);
        final EditText pubmod=(EditText) findViewById(R.id.editmod);
        final EditText pubexp=(EditText) findViewById(R.id.editexp);


        Intent i=getIntent();
        Bundle b = i.getExtras();
        String alinanpubmod= b.getString("gidecekpubmod");
        String alinanpubexp= b.getString("gidecekpubexp");



        pubmod.setText(alinanpubmod);
        pubexp.setText(alinanpubexp);







    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(menu_keys, menu);
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
