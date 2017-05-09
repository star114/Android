package com.company.my.fortune;

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

    TextView txtResult;     // 당첨 결과 표시

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.fab).setOnClickListener(onButtonClick);

        // Button에 Listener 설정
        for (int i = 0; i <= 3; i++) {
            findViewById(R.id.button1 + i).setOnClickListener(onButtonClick);
        }

        // TextView 위젯 읽기
        txtResult = (TextView)findViewById(R.id.textResult);
    }

    Button.OnClickListener onButtonClick = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.fab) {
               // finish();
               //finishAffinity();
               //finishAndRemoveTask();
               // moveTaskToBack(true);
               android.os.Process.killProcess(android.os.Process.myPid());
               //System.exit(0);

            } else {
                String tag = findViewById(v.getId()).getTag().toString();
                checkValue(tag);
            }
        }
    };

    // 당첨 확인
    private void checkValue(String tag) {
        int ntag = Integer.parseInt(tag);

        // 1~4의 난수 만들기
        int r = new Random().nextInt(4) + 1;
        String msg = tag + "번 버튼 : ";

        if (ntag == r) {
            msg += "축하합니다! 당첨되셨습니다.";
        } else {
            msg += "안타깝습니다. 다음 기회에 도전하세요.";
        }

        txtResult.setText(msg);
    }
}

