package my.com.breakout;

import android.graphics.Bitmap;
import android.graphics.PointF;

import java.util.Random;

public class Ball {
    // 화면 크기
    private int scrW, scrH;
    Random rnd = new Random();

    // 이동 방향, 속도
    PointF dir = new PointF();
    public float speed = 800;

    // 위치, 이미지, 반지름
    public float x, y;
    public Bitmap img;
    public int r;

    // 공이 화면 아래를 벗어났나?
    public boolean isOut = true;

    //-----------------------------
    // 생성자
    //-----------------------------
    public Ball(int scrWidth, int scrHeight) {
        scrW = scrWidth;
        scrH = scrHeight;

        // 비트맵, 반지름
        img = CommonResources.ball;
        r = CommonResources.br;

        reset();    // 초기 위치는 패들 위
    }

    //-----------------------------
    // 이동
    //-----------------------------
    public void update() {
        if (isOut) return;

        // 이동
        x += dir.x * Time.deltaTime;
        y += dir.y * Time.deltaTime;

        // 패들/블록과 충돌?
        if ( checkPaddle() || checkBlock() ) return;

        // 천정?
        if (y < r) {
            y -= dir.y * Time.deltaTime;
            dir.y = -dir.y;
            CommonResources.sndPlay(0);
        }

        // 좌우의 벽?
        if (x < r || x > scrW - r) {
            x -= dir.x * Time.deltaTime;
            dir.x = -dir.x;
            CommonResources.sndPlay(0);
        }

        // 화면 아래를 벗어남
        if (y > scrH + r * 4) {
            GameView.paddle.reset();
            isOut = true;

            // 게임오버인가?
            GameView.checkOver();
        }
    }

    //-----------------------------
    // 패들과 충돌 판정
    //-----------------------------
    private boolean checkPaddle() {
        if ( !GameView.paddle.hitTest(x, y, r) ) return false;

        // Sound, Stage Clear?
        CommonResources.sndPlay(4);
        if ( isClear() ) return true;

        setDir();       // 반사 방향 설정
        if (!GameView.isDemo) speed += 4;
        return true;
    }

    //-----------------------------
    // 스테이지 클리어?
    //-----------------------------
    private boolean isClear() {
        int cnt = GameView.mBlock.size();

        if (cnt == 0) {
            GameView.stageNum++;
            GameView.makeStage();
        }

        return cnt == 0;
    }

    //-----------------------------
    // 블록과 충돌 판정
    //-----------------------------
    private boolean checkBlock() {
        int n = 0;
        for (Block tmp : GameView.mBlock) {
            n = tmp.getHit(x, y, r);
            if (n > 0) break;
        }

        // 충돌시 방향 전환
        switch (n) {
        case 1:
            dir.y = -dir.y;
            break;
        case 2:
            dir.x = -dir.x;
            break;
        case 3:
            dir.x = -dir.x;
            dir.y = -dir.y;
        }

        // Demo Mode가 아니면 충돌시 속도 증가
        if (n > 0 && !GameView.isDemo) {
            speed += 2;
        }

        return n > 0;
    }

    //-----------------------------
    // 패들에서 반사 <-- 패들, Touch
    //-----------------------------
    private void setDir() {
        // 이동 방향 (30 ~ 150)
        float rad = (float) Math.toRadians(rnd.nextInt(120) + 30);
        dir.x = (float) Math.cos(rad) * speed;
        dir.y = -(float) Math.sin(rad) * speed;
    }

    //-----------------------------
    // 초기화 <-- Paddle
    //-----------------------------
    public void reset() {
        // 패들 위에 공 놓기
        isOut = true;
        x = GameView.paddle.x;
        y = GameView.paddle.y - GameView.paddle.h - r;
        dir.set(0, 0);
    }

    //-----------------------------
    // 발사 <-- Touch Event
    //-----------------------------
    public boolean start(float tx, float ty) {
        // 공이 이동중이면 이벤트 무시
        if (!isOut) return false;

        // 공 주변을 터치했는가?
        if ( MathF.hitTest(x, y, r * 10, tx, ty) ) {
            setDir();
            isOut = false;
        }

        return !isOut;
    }

} // Ball
