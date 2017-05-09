package com.company.my.yutgame;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // 윷의 이름
    String[] yutName = { "모", "도", "개", "걸", "윷" };

    // 난수
    Random rnd = new Random();
    int[] yut = new int[4];

    // Image Resource & ImageView
    int[] yutImg = { R.drawable.yut_0, R.drawable.yut_1 };
    ImageView[] imgView = new ImageView[4];

    // 게임 결과 표시용
    TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 버튼의 Listener
        findViewById(R.id.fab).setOnClickListener(onButtonClick);
        findViewById(R.id.button).setOnClickListener(onButtonClick);

        // ImageView 읽기
        for (int i = 0; i < 4; i++) {
            imgView[i] = (ImageView) findViewById(R.id.imageView0 + i);
        }

        txtResult = (TextView) findViewById(R.id.textResult);
    }

    // Button의 Listener
    Button.OnClickListener onButtonClick = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            if ( v.getId() == R.id.button ) {
                setGameResult();
            }
        }
    };

    // 게임 결과 표시
    private void setGameResult() {
        int s = 0;

        for (int i = 0; i < 4; i++) {
            int n = rnd.nextInt(2); // 0 or 1
            s += n; // 난수 합계

            imgView[i].setImageResource( yutImg[n] );
        }

        txtResult.setText( yutName[s] );
    }

} // activity
