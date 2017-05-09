package com.company.my.rabbit;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    GameView gameView;    // GameView 저장

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // getSupportActionBar().hide();

        setTitle("엽기 토끼");
        gameView = (GameView) findViewById(R.id.gameView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameView.isRun = !gameView.isRun;
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "Handler 시작");
        menu.add(0, 2, 1, "Handler 중지");
        menu.add(0, 3, 2, "프로그램 종료");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ( item.getItemId() ) {
            case 1 :
                gameView.isRun = true;
                // gameView.mHandler.sendEmptyMessageDelayed(0, 10);
                break;
            case 2 :
                gameView.isRun = false;
                // gameView.mHandler.removeMessages(0);
                break;
            case 3 :
                finish();
        }

        return true;
    }

}
