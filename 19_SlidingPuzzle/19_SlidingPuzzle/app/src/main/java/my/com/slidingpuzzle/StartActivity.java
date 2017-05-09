package my.com.slidingpuzzle;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class StartActivity extends AppCompatActivity {

    MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_start);

        // Statusbar 감추기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        // 배경음악
        mPlayer = MediaPlayer.create(this, R.raw.greensleeves);
        mPlayer.setLooping(true);
        mPlayer.start();

        // 버튼의 Click Listener
        findViewById(R.id.BtnStart).setOnClickListener(onButtonClick);
        findViewById(R.id.BtnQuit).setOnClickListener(onButtonClick);

        // 라디오 버튼
        findViewById(R.id.RadioMusic1).setOnClickListener(onButtonClick);
        findViewById(R.id.RadioMusic2).setOnClickListener(onButtonClick);
    }

    //-----------------------------
    // On Resume
    //-----------------------------
    @Override
    protected void onResume() {
        super.onResume();

        getSettings();
        if (Settings.isMusic) {
            mPlayer.seekTo(0);
            mPlayer.start();
        }
    }

    //-----------------------------
    // On ButtonClick
    //-----------------------------
    View.OnClickListener onButtonClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch ( v.getId() ) {
                case R.id.BtnQuit :     // 종료
                    android.os.Process.killProcess( android.os.Process.myPid() );
                    break;
                case R.id.BtnStart :    // 게임 시작
                    setSettings();
                    mPlayer.pause();
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.RadioMusic1:  // Music On
                    mPlayer.start();
                    break;
                case R.id.RadioMusic2:  // Music On
                    mPlayer.pause();
                    break;
            }
        }
    };

    //-----------------------------
    // 환경 설정 저장
    //-----------------------------
    private void setSettings() {
        // Board Size
        RadioGroup group = (RadioGroup) findViewById(R.id.GroupSize);
        int id = group.getCheckedRadioButtonId();
        View radio = group.findViewById(id);
        Settings.size = group.indexOfChild(radio) + 3;

        // 음악
        id = ( (RadioGroup) findViewById(R.id.GroupMusic) ).getCheckedRadioButtonId();
        Settings.isMusic = (id == R.id.RadioMusic1);

        // 사운드
        id = ( (RadioGroup) findViewById(R.id.GroupSound) ).getCheckedRadioButtonId();
        Settings.isSound = (id == R.id.RadioSound1);

        // 진동
        id = ( (RadioGroup) findViewById(R.id.GroupVib) ).getCheckedRadioButtonId();
        Settings.isVib = (id == R.id.RadioVib1);
    }

    //-----------------------------
    // 환경 설정 읽기
    //-----------------------------
    private void getSettings() {
        // Board Size
        RadioGroup group = (RadioGroup) findViewById(R.id.GroupSize);
        ((RadioButton) group.getChildAt(Settings.size - 3)).setChecked(true);

        // 음악, Sound, 진동
        ((RadioButton) findViewById(R.id.RadioMusic1)).setChecked(Settings.isMusic == true);
        ((RadioButton) findViewById(R.id.RadioSound1)).setChecked(Settings.isSound == true);
        ((RadioButton) findViewById(R.id.RadioVib1)).setChecked(Settings.isVib == true);
    }

    //-----------------------------
    // Back Key
    //-----------------------------
    @Override
    public void onBackPressed() {
        android.os.Process.killProcess( android.os.Process.myPid() );
    }

} // StartActivity
