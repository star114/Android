package my.com.breakout;

import android.graphics.Bitmap;

public class Block {
    // 블록번호, 충돌 횟수
    private int kind;
    private int hitCnt;

    // 비트맵, 위치, 크기
    public Bitmap img;
    public float x, y;
    public int w, h;

    // 점수, 소멸?
    public int score;
    public boolean isDead;

    //-----------------------------
    // 생성자 - 번호, 좌표
    //-----------------------------
    public Block(int kind, float bx, float by) {
        this.kind = kind;
        x = bx;
        y = by;

        img = CommonResources.arBlock[kind - 1];
        w = CommonResources.bw;
        h = CommonResources.bh;

        // 충돌 횟수, 점수
        hitCnt = kind;
        score = kind * 10;
    }

    //-----------------------------
    // 충돌 판정 <-- Ball
    //-----------------------------
    public int getHit(float bx, float by, int r) {
        // 원:사각형 충돌 판정
        if ( !MathF.checkCollision(bx, by, r, x, y, w, h) ) {
            return 0;
        }

        // 충돌 방향 - 모서리
        int result = 3;
        float d = r * 0.6f;

        // 좌우 면인가?
        if ( (by - d > y - h && by + d < y + h) && (bx - d < x - w || bx + d > x + w ) ) {
            result = 2;
        } else if (bx - d >= x - w && bx + d <= x + w) {
            result = 1;
        }

        // Sound
        CommonResources.sndPlay(kind);

        // Demo Mode가 아니면 득점
        if (!GameView.isDemo) {
            GameView.score += kind;
            GameView.setScore();
        }

        // Demo Mode가 아니면 블록 소멸
        if (!GameView.isDemo && --hitCnt <= 0) {
            GameView.score += score;
            GameView.hitCnt++;
            GameView.setScore();
            isDead = true;
        }

        return result;
    }

} // Block
