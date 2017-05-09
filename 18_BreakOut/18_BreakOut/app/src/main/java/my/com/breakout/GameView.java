package my.com.breakout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameView extends View {
    // Context, Thread
    private Context context;
    private GameThread mThread;

    // 화면 크기, 배경 이미지
    private int w, h;
    static private Bitmap imgBack;

    // 남은 공 수, 스테이지
    static private int ballCnt;
    static public int stageNum;

    // 데모 모드, Delay
    static public boolean isDemo;
    static private float delaySpan;

    // 점수
    static public int score;
    static public int hitCnt;

    // 점수 출력용 문자열
    static private String sStage, sScore, sHit, msg;
    static private DecimalFormat decFormat = new DecimalFormat("#,##0");

    // Paint, Media Player
    private Paint paint = new Paint();
    private Paint stroke = new Paint();
    private MediaPlayer mPlayer;

    // Paddle, Ball
    static public Paddle paddle;
    static public Ball ball;

    static public List<Block> mBlock = Collections.synchronizedList( new ArrayList<Block>() );

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

        initGame();     // 게임 초기화
        makeStage();    // 스테이지 만들기

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
        mPlayer.stop();

        mThread.canRun = false;
        super.onDetachedFromWindow();
    }

    //-----------------------------
    // Delay - Touch Event 무시용
    //-----------------------------
    static private void delay(float span) {
        delaySpan = span;
    }

    //-----------------------------
    // 게임 초기화
    //-----------------------------
    private void initGame() {
        stageNum = 0;
        ballCnt = 2;
        isDemo = false;

        // 점수
        score = 0;
        hitCnt = 0;
        msg = "계속하시겠습니까?\n[Touch] 다시 시작\n[Back Key] 종료";

        // Paint
        paint.setTextSize(60);
        paint.setColor(0xff000080);
        paint.setTypeface(Typeface.DEFAULT_BOLD);

        // 윤곽선 문자
        stroke.setTextSize(60);
        stroke.setColor(Color.WHITE);
        stroke.setTypeface(Typeface.DEFAULT_BOLD);

        stroke.setTextAlign(Paint.Align.CENTER);
        stroke.setStyle(Paint.Style.STROKE);
        stroke.setStrokeWidth(10);

        // Media Player
        mPlayer = MediaPlayer.create(context, R.raw.rondo);
        mPlayer.setLooping(true);
        if (Settings.isMusic) mPlayer.start();

        setScore();     // 점수 만들기

        // Game Object
        CommonResources.set(context, w, h);
        paddle = new Paddle(w, h);
        ball = new Ball(w, h);
    }

    //-----------------------------
    // 점수 만들기 <-- Ball, Block
    //-----------------------------
    static public void setScore() {
        sScore = String.format("Score : %s", decFormat.format(score) );
        sHit = String.format("Hit : %d", hitCnt);
        sStage = String.format("Stage : %d", stageNum + 1);
    }

    //-----------------------------
    // Stage 만들기 <-- Ball
    //-----------------------------
    static public void makeStage() {
        imgBack = CommonResources.arBack[stageNum % 4];
        paddle.setPaddle(stageNum);

        mBlock.clear();
        Stage.makeStage(stageNum);

        ball.reset();
        delay(0.5f);
    }

    //-----------------------------
    // 게임 오버? <-- Ball
    //-----------------------------
    static public void checkOver() {
        if (--ballCnt >= 0) return;

        // Demo Mode
        isDemo = true;
        ball.reset();
        ball.start(ball.x, ball.y);

        delay(0.5f);
    }

    //-----------------------------
    // 화면 그리기
    //-----------------------------
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(imgBack, 0, 0, null);

        // 스코어
        paint.setTextAlign(Paint.Align.LEFT);
        stroke.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(sScore, 20, 80, stroke);
        canvas.drawText(sScore, 20, 80, paint);

        paint.setTextAlign(Paint.Align.CENTER);
        stroke.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(sHit, w / 2, 80, stroke);
        canvas.drawText(sHit, w / 2, 80, paint);

        paint.setTextAlign(Paint.Align.RIGHT);
        stroke.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(sStage, w - 20, 80, stroke);
        canvas.drawText(sStage, w - 20, 80, paint);

        // 공 아이콘(남은 공 수) - 화면 왼쪽 아래
        int r = CommonResources.br + 10;
        for (int i = 1; i <= ballCnt; i++) {
            canvas.drawBitmap(CommonResources.ballIcon, i * r, h - r * 1.5f, null);
        }

        // Paddle, Ball
        canvas.drawBitmap(paddle.img, paddle.x - paddle.w, paddle.y - paddle.h, null);
        canvas.drawBitmap(ball.img, ball.x - ball.r, ball.y - ball.r, null);

        // Block
        synchronized (mBlock) {
            for (Block tmp : mBlock) {
                canvas.drawBitmap(tmp.img, tmp.x - tmp.w, tmp.y - tmp.h, null);
            }
        }

        // 게임 오버 메시지
        if (isDemo) {
            paint.setTextSize(90);
            stroke.setTextSize(90);

            paint.setTextAlign(Paint.Align.CENTER);
            stroke.setTextAlign(Paint.Align.CENTER);

            float y = h * 0.4f;
            for (String tmp : msg.split("\n")) {
                canvas.drawText(tmp, w / 2, y, stroke);
                canvas.drawText(tmp, w / 2, y, paint);
                y += h * 0.1f;
            }

            paint.setTextSize(60);
            stroke.setTextSize(60);
        }
    }

    //-----------------------------
    // 이동
    //-----------------------------
    private void moveObject() {
        paddle.update();
        ball.update();
    }

    //-----------------------------
    // 파괴된 블록 삭제
    //-----------------------------
    private void removeDead() {
        synchronized (mBlock) {
            for (int i = mBlock.size() - 1; i >= 0; i--) {
                if (mBlock.get(i).isDead) {
                    mBlock.remove(i);
                }
            }
        }
    }

    //-----------------------------
    // Touch Event
    //-----------------------------
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (delaySpan > 0) return true;

        if (isDemo) {
            isDemo = false;
            initGame();
            makeStage();
            return true;
        }

        float x = event.getX();
        float y = event.getY();

        // 공 발사
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if ( ball.start(x, y) ) return true;
        }

        // 패들 이동
        boolean isPress = (!isDemo && event.getAction() != MotionEvent.ACTION_UP);
        paddle.action( isPress, x );

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
                    delaySpan -= Time.deltaTime;

                    moveObject();
                    removeDead();
                    postInvalidate();   // 화면 그리기
                    sleep(5);
                } catch (Exception e) {
                    // nothing
                }
            }
        }
    } // Thread

} // GameView
