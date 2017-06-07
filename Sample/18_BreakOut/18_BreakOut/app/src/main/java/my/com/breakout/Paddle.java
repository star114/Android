package my.com.breakout;

import android.graphics.Bitmap;

public class Paddle {
    // 화면의 크기
    private int scrW, scrH;

    // 이동 방향(-1, 0, 1), 속도
    private int dirX = 0;
    private final int MIN_SPEED = 300;
    private final int MAX_SPEED = 1500;
    private float speed = MIN_SPEED;

    // 위치, 비트맵, 크기
    public float x, y;
    public Bitmap img;
    public int w, h;

    //-----------------------------
    // 생성자
    //-----------------------------
    public Paddle(int scrWidth, int scrHeight) {
        scrW = scrWidth;
        scrH = scrHeight;

        setPaddle(0);   // 패들 종류
    }

    //-----------------------------
    // 이동
    //-----------------------------
    public void update() {
        // Demo Mode인가?
        if (GameView.isDemo) {
            x = GameView.ball.x;
            return;
        }

        // 패들 이동
        speed = MathF.lerp(speed, MAX_SPEED, 2 * Time.deltaTime);
        x += dirX * speed * Time.deltaTime;

        if (GameView.ball.isOut) {
            GameView.ball.reset();
        }

        // 화면을 벗어나면 정지
        if (x < w || x > scrW - w) {
            x -= dirX * speed * Time.deltaTime;
            dirX = 0;
        }
    }

    //-----------------------------
    // 초기 위치 <-- Ball
    //-----------------------------
    public void reset() {
        x = scrW / 2;
        y = scrH * 0.94f;
    }

    //-----------------------------
    // 충돌 판정 <-- Ball
    //-----------------------------
    public boolean hitTest(float bx, float by, int r) {
        // 패들의 윗면이 아니면 충돌 무시
        if (by > y - h) return false;

        // 원:사각형 충돌
        return MathF.checkCollision(bx, by, r,  x, y, w, h);
    }

    //-----------------------------
    // Action <-- Touch Event
    //-----------------------------
    public void action(boolean isPress, float tx) {
        if (isPress) {      // ACTION_DOWN
            dirX = (tx < scrW / 2) ? -1 : 1;
        } else {            // ACTION_UP
            dirX = 0;
            speed = MIN_SPEED;
        }
    }

    //-----------------------------
    // SetPaddle <-- GameView
    //-----------------------------
    public void setPaddle(int stageNum) {
        int n = Math.min(2, stageNum);
        img = CommonResources.arPaddle[n];

        w = img.getWidth() / 2;
        h = img.getHeight() / 2;

        reset();    // 초기 위치
    }

} // Paddle
