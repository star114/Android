package android.my.com.xwing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//-----------------------------
// GameView
//-----------------------------
public class GameView extends View {
    // Context, Thread
    private Context context;
    private GameThread mThread;

    // 배경과 화면 크기
    private Bitmap imgBack;
    private int w, h;

    // Xwing, Laser
    private Xwing xwing;
    static public List<Laser> mLaser;

    //-----------------------------
    // 생성자
    //-----------------------------
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Context, 비트맵 이미지 처리
        this.context = context;
        CommonResources.set(context);
    }

    //-----------------------------
    // View의 크기 구하기
    //-----------------------------
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        this.w = w;  // 화면의 폭과 높이
        this.h = h;

        // 배경 이미지
        imgBack = BitmapFactory.decodeResource(getResources(), R.drawable.sky);
        imgBack = Bitmap.createScaledBitmap(imgBack, w, h, true);

        // X-Wing, 레이저
        xwing = new Xwing(w, h);
        mLaser = Collections.synchronizedList( new ArrayList<Laser>() );

        // 스레드 기동
        if (mThread == null) {
            mThread = new GameThread();
            mThread.start();
        }
    }

    //-----------------------------
    // View의 종료
    //-----------------------------
    @Override
    protected void onDetachedFromWindow() {
        mThread.canRun = false;
        super.onDetachedFromWindow();
    }

    //-----------------------------
    // 화면 그리기 - 동기화
    //-----------------------------
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(imgBack, 0, 0, null);

        // Laser
        synchronized (mLaser) {
            for (Laser tmp : mLaser) {
                canvas.drawBitmap(tmp.img, tmp.x - tmp.w, tmp.y - tmp.h, null);
            }
        }

        canvas.drawBitmap(xwing.img, xwing.pos.x - xwing.w, xwing.pos.y - xwing.h, null);
    }

    //-----------------------------
    // 이동
    //-----------------------------
    private void moveObject() {
        xwing.update();

        synchronized (mLaser) {
            for (Laser tmp : mLaser) {
                tmp.update();
            }
        }
    }

    //-----------------------------
    // 화면을 벗어난 레이저 삭제  - 동기화
    //-----------------------------
    private void removeDead() {
        synchronized (mLaser) {
            for (int i = mLaser.size() - 1; i >= 0; i--) {
                if (mLaser.get(i).isDead) {
                    mLaser.remove(i);
                }
            }
        }
    }

    //-----------------------------
    // Touch Event
    //-----------------------------
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            xwing.setAction( event.getX(),  event.getY() );
        }

        return true;
    }

    //-----------------------------
    // Thread
    //-----------------------------
    class GameThread extends Thread {
        public boolean canRun = true;

        @Override
        public void run() {
            while (canRun) {
                try {
                    Time.update();      // deltaTime 계산

                    moveObject();
                    removeDead();
                    postInvalidate();   // 화면 그리기
                    sleep(10);
                } catch (Exception e) {
                    //
                }
            }
        }
    } // Thread

} // GameView
