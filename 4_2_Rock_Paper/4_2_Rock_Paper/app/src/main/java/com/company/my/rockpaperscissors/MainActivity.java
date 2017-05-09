package com.company.my.rockpaperscissors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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

    // 가위 바위 보 이미지 배열
    int[] imgRes = { R.drawable.img_1, R.drawable.img_2, R.drawable.img_3 };

    // 가위비위보 값 (0, 1, 2)
    int you = 0;
    int com = 0;

    // 승패 횟수
    int win = 0;
    int lose = 0;

    // 결과 표시용 ImageView
    ImageView imgViewYou;
    ImageView imgViewCom;

    // 승패 횟수 표시용
    TextView txtYou;
    TextView txtCom;

    // 판정 결과 표시용
    TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 버튼의 Listener 설정
        findViewById(R.id.fab).setOnClickListener(onButtonClick);
        for (int i = 0; i < 3; i++) {
            findViewById(R.id.imageButton1 + i).setOnClickListener(onButtonClick);
        }

        // ImageView 읽기
        imgViewYou = (ImageView) findViewById(R.id.imageView1);
        imgViewCom = (ImageView) findViewById(R.id.imageView2);

        // TextView
        txtYou = (TextView) findViewById(R.id.textYou);
        txtCom = (TextView) findViewById(R.id.textCom);
        txtResult = (TextView) findViewById(R.id.textResult);

        // 게임 초기화
        initGame();
    }

    // 옵션 메뉴 만들기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "다시 시작");
        menu.add(0, 2, 1, "종료");
        menu.add(0, 3, 2, "About");
        return true;
    }

    // 옵션 메뉴 Item Select
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 메뉴 id 읽기
        switch( item.getItemId() ) {
            case 1 :    // 다시 시작
                initGame();
                break;
            case 2 :    // 종료
                finishAffinity();
                break;
            case 3 :    // About
                View v = findViewById(R.id.imageButton1);
                Snackbar.make(v, "가위바위보 Ver 1.0", Snackbar.LENGTH_LONG).setAction("OK", null).show();
        }

        // return super.onOptionsItemSelected(item);
        return true;
    }

    // Button Listener
    Button.OnClickListener onButtonClick = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            if ( v.getId() == R.id.fab ) {
                return;
            } else {
                String tag = v.getTag().toString();
                you = Integer.parseInt(tag);
                SetGameResult();
            }
        }
    };

    // 게임 초기화
    private void initGame() {
        win = 0;
        lose = 0;

        // 결과 표시
        txtYou.setText("당신 : 0");
        txtCom.setText("단말기 : 0 ");
        txtResult.setText("");

        // ImageView 초기화
        imgViewYou.setImageResource(R.drawable.question);
        imgViewCom.setImageResource(R.drawable.question);
    }

    // 게임 승패 처리
    private void SetGameResult() {
        com = new Random().nextInt(3);   // 0~2의 난수
        int k = you - com;

        // 승패 판정
        String str = "";
        if ( k == 0 ) {
            str = "비겼습니다.";
        } else if ( k == 1 || k == -2 ) {
            str = "당신이 이겼습니다.";
            win++;
        } else {
            str = "당신이 졌습니다.";
            lose++;
        }

        // Image 표시
        SetImages();

        // 결과 표시
        txtYou.setText("당신 : " + win);
        txtCom.setText("단말기 : " + lose);
        txtResult.setText(str);
    }

    // 이미지 표시
    private void SetImages() {
        imgViewYou.setImageResource( imgRes[you] );
        imgViewCom.setImageResource( imgRes[com] );

        // Bitmap 만들기
        Bitmap orgImg = BitmapFactory.decodeResource(getResources(), imgRes[com]);

        // 이미지를 뒤집을 Matrix
        Matrix matrix = new Matrix();
        matrix.setScale(-1, 1);

        // 이미지 뒤집기
        Bitmap revImg = Bitmap.createBitmap(orgImg, 0, 0, orgImg.getWidth(), orgImg.getHeight(), matrix, false);
        imgViewCom.setImageBitmap(revImg);
    }

} // activity
