package com.example.star114.fortune;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.fab).setOnClickListener(onButtonClick);
        findViewById(R.id.button1).setOnClickListener(onButtonClick);
        findViewById(R.id.button2).setOnClickListener(onButtonClick);
        findViewById(R.id.button3).setOnClickListener(onButtonClick);
        findViewById(R.id.button4).setOnClickListener(onButtonClick);

        // R.id 에서 button의 순서가 보장되지 않을 수 있다.
        //for (int i = 0; i < 5; i++) {
        //    findViewById(R.id.button1 + i).setOnClickListener(onButtonClick);
        //}
    }

    Button.OnClickListener onButtonClick = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.fab) {
                finish();
            } else {
                String tag = findViewById(v.getId()).getTag().toString();
                checkValue(tag);
            }
        }
    };

    private void checkValue(String tag) {
        int nTag = Integer.parseInt(tag);

        int r = new Random().nextInt(4) + 1;
        String msg = tag + "번 버튼 : ";

        if (nTag == r) {
            msg += "축하합니다! 당첨되셨습니다.";
        } else {
            msg += "안타깝습니다. 다음 기회에 도전하세요.";
        }
        ((TextView) findViewById(R.id.textView2)).setText(msg);
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
}
