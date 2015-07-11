package com.grayraven.project0;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn1 = (Button) findViewById(R.id.btnStreamer);
        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showToast(getResources().getString(R.string.msgStreamer));
            }
        });

        Button btn2 = (Button) findViewById(R.id.btnScores);
        btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showToast(getResources().getString(R.string.msgScores));
            }
        });

        Button btn3 = (Button) findViewById(R.id.btnLibrary);
        btn3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showToast(getResources().getString(R.string.msgLibrary));
            }
        });

        Button btn4 = (Button) findViewById(R.id.btnBigger);
        btn4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showToast(getResources().getString(R.string.msgBigger));
            }
        });

        Button btn5 = (Button) findViewById(R.id.btnXyz);
        btn5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showToast(getResources().getString(R.string.msgXyz));
            }
        });

        Button btn6 = (Button) findViewById(R.id.btnCapstone);
        btn6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showToast(getResources().getString(R.string.msgCapstone));
            }
        });
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}
