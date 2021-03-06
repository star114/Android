package android.my.com.xwing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

public class CommonResources {
    // X-Wing
    static public Bitmap imgXwing;
    static public int xw, xh;

    // Laser
    static public Bitmap imgLaser;
    static public int lw, lh;

    // 사운드, 사운드 id
    static private SoundPool  mSound;
    static private int sndLaser;

    //--------------------------
    // Set Resource  <-- GameView
    //--------------------------
    static public void set(Context context) {
        setXwing(context);
        setLaser(context);
        setSound(context);
    }

    //--------------------------
    // X-Wing
    //--------------------------
    static private void setXwing(Context context) {
        imgXwing = BitmapFactory.decodeResource(context.getResources(), R.drawable.xwing);
        xw = imgXwing.getWidth() / 2;
        xh = imgXwing.getHeight() / 2;
    }

    //--------------------------
    // Laser
    //--------------------------
    static private void setLaser(Context context) {
        imgLaser = BitmapFactory.decodeResource(context.getResources(), R.drawable.laser);
        lw = imgLaser.getWidth() / 2;
        lh = imgLaser.getHeight() / 2;
    }

    //--------------------------
    // Sound
    //--------------------------
    static private void setSound(Context context) {
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

        sndLaser = mSound.load(context, R.raw.laser, 1);
    }

    //--------------------------
    // Sound 재생 <-- X-Wing
    //--------------------------
    static public void sndPlay() {
        mSound.play(sndLaser, 1, 1, 1, 0, 1);
    }

} // CommonResources
