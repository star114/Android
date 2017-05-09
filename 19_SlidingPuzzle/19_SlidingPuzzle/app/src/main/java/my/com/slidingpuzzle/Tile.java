package my.com.slidingpuzzle;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.RectF;

public class Tile {

    // 터치 판정 영역, 이동 속도
    RectF rect = new RectF();
    private int speed = 2000;

    // 이동 방향, 목적지
    private Point dir = new Point();
    private float tx, ty;

    // 타일 번호, 화면 좌표, 크기, 비트맵
    public int num;
    public float x, y;
    public int w;
    public Bitmap img;

    //-----------------------------
    // 생성자
    //-----------------------------
    public Tile(int idx) {
        // 타일 번호, 비트맵, 크기
        num = idx;
        img = CommonResources.arTile[num];
        w = CommonResources.tw;
    }

    //-----------------------------
    // 타일의 좌표 설정 <-- Board
    //-----------------------------
    public void setPosition(int idx) {
        x = idx % Settings.size * w;
        y = idx / Settings.size * w;
    }

    //-----------------------------
    // 이동 <-- Board
    //-----------------------------
    public boolean update() {
        // 타일 이동
        x += dir.x * speed * Time.deltaTime;
        y += dir.y * speed * Time.deltaTime;

        // 이동 방향의 한계를 벗어났는가?
        if (dir.x > 0 && x > tx || dir.x < 0 && x < tx || dir.y > 0 && y > ty || dir.y < 0 && y < ty) {
            x = tx;
            y = ty;
            dir.set(0, 0);
            return false;
        }

        return true;
    }

    //-----------------------------
    // 이동 방향 설정 <-- Board
    //-----------------------------
    public void setDir(int moveDir) {
        // 목적지 (12시 기준 CW 1, 2, 3, 4)
        int[] distX = { 0, 0, w, 0, -w};
        int[] distY = { 0, -w, 0, w, -0};

        // 목적지
        tx = x + distX[moveDir];
        ty = y + distY[moveDir];

        // 이동 방향 (-1, 0, 1)
        dir.x = distX[moveDir] / w;
        dir.y = distY[moveDir] / w;
    }

    //-----------------------------
    // 터치 조사 <-- Board
    //-----------------------------
    public int hitTest(float px, float py) {
        // 화면의 터치 영역
        float rx = x + CommonResources.mgnW;
        float ry = y + CommonResources.mgnH;
        rect.set(rx, ry, rx + w - 1, ry + w - 1);

        return rect.contains(px, py) ? num : -1;
    }

} // Tile
