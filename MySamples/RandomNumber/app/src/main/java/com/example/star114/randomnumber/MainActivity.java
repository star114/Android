package com.example.star114.randomnumber;

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
    Random rnd = new Random();
    int count = 0;
    int num;

    EditText edNum;
    TextView txtCount;
    TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("숫자 맞추기");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.fab).setOnClickListener(onButtonClick);
        findViewById(R.id.button).setOnClickListener(onButtonClick);

        edNum = (EditText) findViewById(R.id.editText);
        txtCount = (TextView) findViewById(R.id.textCount);
        txtResult = (TextView) findViewById(R.id.textResult);

        num = rnd.nextInt(501) + 500;

        clearFields();
    }

    private void clearFields() {
        txtCount.setText("입력횟수 : " + count);
        txtResult.setText("");
        edNum.setText("");
    }

    // Button Event
    Button.OnClickListener onButtonClick = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fab:
                    num = rnd.nextInt(501) + 500;
                    count = 0;
                    clearFields();
                    break;
                case R.id.button:
                    checkValue();
            }
        }
    };

    private void checkValue() {
        String str = edNum.getText().toString();

        if (str.equals("")) {
            txtResult.setText("500~1000 사이의 숫자를 입력하세요.");
            return;
        }

        int n = Integer.parseInt(str);

        if (n < 500 || n > 1000) {
            txtResult.setText("500~1000 사이의 숫자를 입력하세요.");
            return;
        }

        if (n == num) {
            str = "정답입니다.";
        } else if (n > num) {
            str = n + "보다는 적습니다.";
        } else {
            str = n + "보다는 큽니다.";
        }
        count++;

        txtCount.setText("입력횟수 : " + count);
        txtResult.setText(str);

        if (n != num) {
            edNum.setText("");
            edNum.requestFocus();
        }
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
