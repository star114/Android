package com.company.my.randomnumver;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Random rnd = new Random();  // 난수 class
    int count = 0;              // 사용자 입력 횟수
    int num;                    // 난수

    EditText edNum;             // 입력 위젯
    TextView txtCount;          // 사용자 입력 횟수 출력
    TextView txtResult;         // 판정 결과 출력

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("숫자 맞추기");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 버튼의 Listener 설정
        findViewById(R.id.fab).setOnClickListener(onButtonClick);
        findViewById(R.id.button).setOnClickListener(onButtonClick);

        // 위젯 읽기
        edNum = (EditText) findViewById(R.id.editText);
        txtCount = (TextView) findViewById(R.id.textCount);
        txtResult = (TextView) findViewById(R.id.textResult);

        // 난수 만들기
        num = rnd.nextInt(501) + 500;

        // 위젯 초기화
        clearFields();
    } // onCreate

    // Button Event
    Button.OnClickListener onButtonClick = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch ( v.getId() ) {
                case R.id.fab :     // Floating Action Button
                    num = rnd.nextInt(501) + 500;
                    count = 0;
                    clearFields();
                    break;
                case R.id.button :  // 확인 버튼
                    checkValue();
            }
        }
    };

    // Widget 초기화
    private void clearFields() {
        txtCount.setText("입력횟수 : " + count);
        txtResult.setText("");
        edNum.setText("");
    }

    // 결과 판정
    private void checkValue() {
        // 입력받은 값 읽기
        String str = edNum.getText().toString();

        // 빈문자인지 판정
        if ( str.equals("") ) {
            txtResult.setText("500~1000 사이의 숫자를 입력하세요.");
            return;
        }

        // 문자열을 정수로 변환
        int n = Integer.parseInt(str);

        // 정답 여부 판정
        if (n == num) {
            str = "정답입니다.";
        } else if (n > num) {
            str = n + "보다는 적습니다.";
        } else {
            str = n + "보다는 큽니다.";
        }

        // 입력 횟수 증가
        count++;

        // 판정 결과 표시
        txtCount.setText("입력횟수 : " + count);
        txtResult.setText(str);

        // 다음 입력을 위해 입력한 숫자를 지우고 포커스 이동
        if (n != num) {
            edNum.setText("");
            // edNum.selectAll();
            edNum.requestFocus();
        }
    }

} // activity

