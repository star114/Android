package my.com.slidingpuzzle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class GameView extends View {
    // Context, Thread
    private Context context;
    private GameThread mThread;

    // 화면 크기
    private int w, h;

    // Board, Stage Clear?
    private Board mBoard;
    private boolean isClear = false;

    // TimeSpan, Span String
    private  float timeSpan = 0;
    private  String strTime = "";

    // Media Player, Paint
    private MediaPlayer mPlayer;
    private Paint paint = new Paint();
    private Paint paintBack = new Paint();
    private Paint paintClear = new Paint();

    //-----------------------------
    // 생성자
    //-----------------------------
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    //-----------------------------
    // View의 크기 구하기
    //-----------------------------
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        this.w = w;     // 화면의 폭과 높이
        this.h = h;

        // CommonResources, Board 만들기
        CommonResources.set(context, w, h);
        mBoard = new Board();

        initGame();     // 게임 초기화

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
    // 게임 초기화
    //-----------------------------
    private void initGame() {
        // 점수 표시용 Paint
        paint.setTextSize(60);
        paint.setColor(0xff000080);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setTextAlign(Paint.Align.RIGHT);

        // 배경 표시용 Paint
        LinearGradient grad = new LinearGradient(0, 0, 0, h, 0xFFa8dda0, 0x40a8dda0, Shader.TileMode.CLAMP);
        paintBack.setStyle(Paint.Style.FILL);
        paintBack.setShader(grad);

        // Stage Clear 표시용
        paintClear.setTextSize(120);
        paintClear.setColor(0xff000080);
        paintClear.setTypeface(Typeface.DEFAULT_BOLD);
        paintClear.setTextAlign(Paint.Align.CENTER);

        // Media Player
        mPlayer = MediaPlayer.create(context, R.raw.rondo);
        mPlayer.setLooping(true);
        if (Settings.isMusic) mPlayer.start();

        mBoard.makeBoard();
    }

    //-----------------------------
    // 화면 그리기
    //-----------------------------
    @Override
    protected void onDraw(Canvas canvas) {
        // 배경
        canvas.drawRect(0, 0, w, h, paintBack);

        // Board
        mBoard.drawTile(canvas);

        // Score
        canvas.drawText("Move Tile :", w - 50 , h * 0.2f, paint);
        canvas.drawText(mBoard.moveCnt + "", w - 50 , h * 0.3f, paint);

        canvas.drawText("Time Span :", w - 50 , h * 0.4f, paint);
        canvas.drawText(strTime, w - 50 , h * 0.5f, paint);

        if (isClear) {
            canvas.drawText("Stage Clear!", w / 2, h / 2, paintClear);
        }
    }

    //-----------------------------
    // 타일 이동
    //-----------------------------
    private void moveObject() {
        mBoard.moveTiles();
    }

    //-----------------------------
    // Stage Clear
    //-----------------------------
    private void checkClear() {
        isClear = mBoard.isClear();
    }

    //-----------------------------
    // 게임 경과 시간
    //-----------------------------
    private void setTimeSpan() {
        if (!isClear) {
            timeSpan += Time.deltaTime;
        }

        int hour = (int)timeSpan / 3600;
        int min = (int)timeSpan % 3600 / 60;
        float sec = (float)Math.round(timeSpan % 60 * 10) / 10;

        strTime = String.format("%02d:%02d:%04.1f", hour, min, sec);
    }

    //-----------------------------
    // Touch Event
    //-----------------------------
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isClear && event.getAction() == MotionEvent.ACTION_DOWN) {
            mBoard.hitTest(event.getX(), event.getY());

            // Sound
            CommonResources.sndPlay();
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
                    checkClear();
                    setTimeSpan();
                    postInvalidate();   // 화면 그리기
                    sleep(5);
                } catch (Exception e) {
                    // nothing
                }
            }
        }
    } // Thread

} // GameView
