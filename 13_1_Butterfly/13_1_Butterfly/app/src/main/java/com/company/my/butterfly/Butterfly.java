package com.company.my.butterfly;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PointF;

import java.util.Random;

//--------------------------
// Butterfly
//--------------------------
public class Butterfly {
    // 화면 크기, 랜덤
    private int scrW, scrH;
    private Random rnd = new Random();

    // 속도, 이동 방향
    private int speed;
    private PointF dir = new PointF();

    // 애니메이션 속도, 경과 시간
    private float animSpan;
    private float animTime = 0;

    // 비트맵, 애니메이션 번호
    private Bitmap[] arrFly = new Bitmap[10];
    private int animNum = 0;

    // 목적지 최대 근접 거리, 목적지
    private int dist;
    private boolean isTarget;

    // 목적지 도착 여부, 목적지에 머무를 시간
    private boolean reached;
    private float stay;

    // 현재 위치, 목적지 위치
    public float x, y;
    public float tx, ty;

    // 이미지, 크기, 회전각도
    public Bitmap fly;
    public int w, h;
    public float ang;

    //--------------------------
    // 생성자
    //--------------------------
    public Butterfly(Context context, int width, int height) {
        scrW = width;
        scrH = height;

        // 나비 초기 위치 - 화면 전체
        x = rnd.nextInt(scrW);
        y = rnd.nextInt(scrH);

        // 비트맵 만들기와 초기화
        makeButterfly(context);
        init();
    }

    //--------------------------
    // Move
    //--------------------------
    public void update() {
        // 애니메이션
        animationFly();

        // 이동
        x += dir.x * Time.deltaTime;
        y += dir.y * Time.deltaTime;

        // 목적지가 있는가?
        if (isTarget) {
            checkTarget();
        }

        // 화면을 벗어나면 화면 반대쪽에서 등장
        if (x < -w) x = scrW + w;   // 왼쪽
        if (x > scrW + w) x = -w;   // 오른쪽

        if (y < -h) y = scrH + h;   // 위
        if (y > scrH + h) y = -h;   // 아래
    }

    //--------------------------
    // Target 설정 <-- GameView
    //--------------------------
    public void setTarget(float px, float py) {
        tx = px;      // 목적지 좌표
        ty = py;

        double rad = -Math.atan2(ty - y, tx - x);

        // 이동 방향 및 속도
        dir.x = (float) Math.cos(rad) * speed;
        dir.y = (float) -Math.sin(rad) * speed;

        // 나비의 회전 방향
        ang = 90 - (float)Math.toDegrees(rad);
        isTarget = true;
    }

    //--------------------------
    // 목적지 근처인가?
    //--------------------------
    private void checkTarget() {
        // 원의 공식
        float r = (x - tx) * (x - tx) + (y - ty) * (y - ty);

        // 목적지에서 50~150 픽셀 이내인가?
        reached = (r <= dist * dist);

        // 목적지에 도착하면
        if (reached) {
            dir.x = dir.y = 0;       // 정지
            stay -= Time.deltaTime;  // 머무르는 시간 감소

            // 시간이 경과되면 초기화 - 랜덤하게 날아가가
            if (stay <= 0) {
                init();
            }
        }
    }

    //--------------------------
    // 랜덤한 컬러의 나비 만들기
    //--------------------------
    private void makeButterfly(Context context) {
        //  원본 이미지
        Bitmap org = BitmapFactory.decodeResource(context.getResources(), R.drawable.butterfly);
        w = org.getWidth();
        h = org.getHeight();

        // 컬러를 입힐 작업용 빈 이미지 만들기
        Bitmap tmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();

        // 랜덤한 컬러와 컬러 필터 만들기
        int color = rnd.nextInt(0x808080) + 0x808080;
        ColorFilter filter = new LightingColorFilter(color, 0x404040);
        paint.setColorFilter(filter);

        // 빈 이미지에 필터를 적용해서 출력
        Canvas canvas = new Canvas(tmp);
        canvas.drawBitmap(org, 0, 0, paint);

        // 이미지 분리
        w /= 10;
        for (int i = 0; i < 10; i++) {
            arrFly[i] = Bitmap.createBitmap(tmp, w * i, 0, w, h);
        }

        w = w / 2;
        h = h / 2;

        fly = arrFly[0];
    }

    //--------------------------
    // 속도, 방향 설정
    //--------------------------
    private void init() {
        // 속도
        speed = rnd.nextInt(101) + 200;     // 200 ~ 300 픽셀

        // 이동 방향
        double rad = Math.toRadians( rnd.nextInt(360) );

        dir.x = (float) Math.cos(rad) * speed;
        dir.y = (float) -Math.sin(rad) * speed;

        // 회전 방향
        ang = 90 - (float)Math.toDegrees(rad);
        animSpan = (rnd.nextInt(8) + 6) / 100f;    // 0.06 ~ 0.13초

        // 목적지에 머무를 시간
        stay = (rnd.nextInt(6) + 10) / 10f;        // 1.0 ~ 1.5초

        // 목적지와의 최대 근접 거리
        dist = rnd.nextInt(101) + 50;              // 50 ~ 150 픽셀
        isTarget = reached = false;
    }

    //-----------------------------
    // 애니메이션
    //-----------------------------
    private void animationFly() {
        // 다음 애니메이션 번호
        animTime += Time.deltaTime;

        if (animTime > animSpan) {
            animNum++;
            animTime = 0;

            // 마지막 이미지인가?
            if (animNum >= 10) {
                animNum = 0;
            }
        }

        fly = arrFly[animNum];
    }

} // Butterfly
