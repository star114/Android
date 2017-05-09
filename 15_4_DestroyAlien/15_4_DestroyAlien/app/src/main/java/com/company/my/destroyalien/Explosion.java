package com.company.my.destroyalien;

import android.graphics.Bitmap;

public class Explosion {
    // 지연 시간, 경과시간, 이미지 번호
    private float animDelay = 0.04f;
    private float animSpan;
    private int imgNum;

    // 위치, 크기
    public float x, y;
    public int w, h;

    // 현재 이미지, 소멸?
    public Bitmap img;
    public boolean isDead;

    //--------------------------
    // 생성자
    //--------------------------
    public Explosion(float tx, float ty, String sKind) {
        x = tx;
        y = ty;

        // 작은 불꽃의 처리
        if (sKind == "Small") {
            imgNum = 20;
            animDelay = 0.1f;
        }

        // 초기 이미지
        img = CommonResources.arExp[imgNum];
        w = CommonResources.ew;
        h = CommonResources.eh;
    }

    //--------------------------
    // 애니메이션
    //--------------------------
    public void update() {
        animSpan += Time.deltaTime;
        if (animSpan > animDelay) {
            animSpan = 0;
            imgNum++;

            // 표시할 이미지, 마지막 이미지이면 소멸
            img = CommonResources.arExp[imgNum];
            isDead = (imgNum == 24);
        }
    }

} // Explosion
