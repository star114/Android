package my.com.slidingpuzzle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Vibrator;

public class CommonResources {
    // 위쪽, 왼쪽 여백
    static int mgnH, mgnW;

    // Tile 배열, Tile 크기
    static public Bitmap[] arTile;
    static public int tw;

    // 액자, 출력 위치
    static public Bitmap frame;
    static public int fx, fy;

    // 사운드, 사운드 id
    static private SoundPool mSound;
    static private int sndId;

    // 진동
    static private Vibrator vib;

    //-----------------------------
    // Set <-- GameView
    //-----------------------------
    static public void set(Context context, int scrW, int scrH) {
        makeTile(context, scrW, scrH);
        makeFrame(context, scrW, scrH);
        makeSound(context);
    }

    //-----------------------------
    // Tile
    //-----------------------------
    private static void makeTile(Context context, int scrW, int scrH) {
        // 보드의 종류와 타일의 수
        int n = Settings.size;
        arTile = new Bitmap[n * n];

        // 세로 및 가로 여백
        mgnH = scrH / 10;
        mgnW = (scrW - scrH) / 2;

        // 타일 크기
        tw = (scrH - mgnH * 2) / n;

        for (int i = 0; i < n * n - 1; i++) {
            Bitmap tmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.tile01 + i);
            arTile[i] = Bitmap.createScaledBitmap(tmp, tw, tw, true);
        }
    }

    //-----------------------------
    // 액자
    //-----------------------------
    private static void makeFrame(Context context, int scrW, int scrH) {
        Bitmap tmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.frame);
        frame = Bitmap.createScaledBitmap(tmp, scrH - mgnH, scrH - mgnH, true);

        // 액자 출력 위치
        fx = mgnW - mgnH / 2;
        fy = mgnH / 2;
    }

    //--------------------------
    // Sound
    //--------------------------
    static private void makeSound(Context context) {
        // 롤리팝 이전 버전인가?
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mSound = new SoundPool(5, AudioManager.STREAM_MUSIC, 1);
        } else {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();
            mSound = new SoundPool.Builder().setAudioAttributes(attributes).setMaxStreams(5).build();
        }

        // 사운드
        sndId = mSound.load(context, R.raw.sound1, 1);

        // 진동
        vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    //--------------------------
    // Sound, 진동
    //--------------------------
    static public void sndPlay() {
        if (Settings.isSound) {
            mSound.play(sndId, 0.9f, 0.9f, 1, 0, 1);
        }

        // 30/1000초 진동
        if (Settings.isVib) {
            vib.vibrate(30);
        }
    }

} // CommonResources
