package my.com.breakout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Vibrator;

public class CommonResources {
    // Paddle
    static public Bitmap[] arPaddle = new Bitmap[3];

    // Ball, Ball Icon
    static public Bitmap ball;
    static public Bitmap ballIcon;
    static public int br;

    // Block
    static public Bitmap[] arBlock = new Bitmap[3];
    static public int bw, bh;

    // 배경
    static public Bitmap[] arBack = new Bitmap[4];

    // 사운드, 사운드 id
    static private SoundPool mSound;
    static private int[] sndId = new int[5];

    // 진동
    static private Vibrator vib;

    //-----------------------------
    // Set <-- GameView
    //-----------------------------
    static public void set(Context context, int scrW, int scrH) {
        makePaddle(context);
        makeBall(context);
        makeBlock(context, scrW, scrH);
        makeBackground(context, scrW, scrH);
        makeSound(context);
    }

    //-----------------------------
    // 패들
    //-----------------------------
    static private void makePaddle(Context context) {
        for (int i = 0; i < 3; i++) {
            arPaddle[i] = BitmapFactory.decodeResource(context.getResources(), R.drawable.paddle1 + i);
        }
    }

    //-----------------------------
    // 볼
    //-----------------------------
    static private void makeBall(Context context) {
        ball = BitmapFactory.decodeResource(context.getResources(), R.drawable.ball);
        br = ball.getWidth() / 2;

        // Ball Icon
        ballIcon = Bitmap.createScaledBitmap(ball, br, br, true);
    }

    //-----------------------------
    // 블록
    //-----------------------------
    private static void makeBlock(Context context, int scrW, int scrH) {
        int w = scrW / 6;
        int h = scrH / 20;

        for (int i = 0; i < 3; i++) {
            Bitmap tmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.block1 + i);
            arBlock[i] = Bitmap.createScaledBitmap(tmp, w, h, true);
        }

        bw = w / 2;
        bh = h / 2;
    }

    //-----------------------------
    // 배경
    //-----------------------------
    private static void makeBackground(Context context, int scrW, int scrH) {
        for (int i = 0; i < 4; i++) {
            Bitmap tmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.back1 + i);
            arBack[i] = Bitmap.createScaledBitmap(tmp, scrW, scrH, true);
        }
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

        // 사운드 종류 - 0: 벽, 123: 블록, 4: 패들
        for (int i = 0; i < 5; i++) {
            sndId[i] = mSound.load(context, R.raw.sound1 + i, 1);
        }

        // 진동
        vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    //--------------------------
    // Sound, 진동
    //--------------------------
    static public void sndPlay(int kind) {
        if (Settings.isSound) {
            mSound.play(sndId[kind], 0.9f, 0.9f, 1, 0, 1);
        }

        // 30/1000초 진동
        if (Settings.isVib) {
            vib.vibrate(30);
        }
    }

} // CommonResources
