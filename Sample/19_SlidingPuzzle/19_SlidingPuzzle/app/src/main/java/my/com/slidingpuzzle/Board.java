package my.com.slidingpuzzle;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.Random;

public class Board {
    private Random rnd = new Random();

    // 타일 수, 타일 번호 배열
    private int cnt;
    private int[] tiles;

    // 터치한 타일 번호, 배열 인덱스, 공백의 인덱스
    private int num, idx, blk;

    // 타일 객체, 이동한 타일 수
    public ArrayList<Tile> mTile = new ArrayList<Tile>();
    public int moveCnt = 0;

    // 타일의 이동 방향, 이동할 타일 번호 목록
    private int dir = 0;
    private ArrayList<Integer> buffer = new ArrayList<Integer>();

    //-----------------------------
    // Make Board <-- GameView
    //-----------------------------
    public void makeBoard() {
        // 타일 수, 배열의 크기
        cnt = Settings.size * Settings.size - 1;
        tiles = new int[cnt + 1];

        // 배열에 일련번호 넣기
        for (int i = 0; i < cnt; i++) {
            tiles[i] = i;
        }

        // 마지막 번호는 -1 (빈 타일)
        tiles[cnt] = -1;

        // 타일 번호 섞기
        SuffleTile();

        // 일련의 타일 블록 만들기
        mTile.clear();
        for (int i = 0; i < cnt; i++) {
            mTile.add( new Tile(i) );
        }

        // 각 타일 블록의 좌표 설정
        for (int i = 0; i <= cnt; i++) {
            if (tiles[i] >= 0) {
                mTile.get(tiles[i]).setPosition(i);
            }
        }
    }

    //-----------------------------
    // 타일 번호 섞기
    //-----------------------------
    private void SuffleTile() {
        for (int i = 0; i < cnt; i++) {
            int n1 = rnd.nextInt(cnt + 1);
            int n2 = rnd.nextInt(cnt + 1);

            // 교환
            int t = tiles[n1];
            tiles[n1] = tiles[n2];
            tiles[n2] = t;
        }

        // 무결성 조사
        if ( !intergrity() ) {
            SuffleTile();
        }
    }

    //-----------------------------
    // 무결성 조사 - 짝수 치환인가?
    //-----------------------------
    private boolean intergrity() {
        int repCnt = 0;

        for (int i = 0; i < cnt; i++) {
            if (tiles[i] == -1) continue;
            for (int j = i + 1; j <= cnt; j++) {
                if (tiles[j] != -1 && tiles[i] > tiles[j]) {
                    repCnt++;
                }
            }
        }

        return repCnt % 2 == 0;
    }

    //-----------------------------
    // 화면 츌력
    //-----------------------------
    public void drawTile(Canvas canvas) {
        // 액자
        canvas.drawBitmap(CommonResources.frame, CommonResources.fx, CommonResources.fy, null);

        // 타일 위치
        canvas.translate(CommonResources.mgnW, CommonResources.mgnH);
        // 타일
        for (Tile tmp : mTile) {
            canvas.drawBitmap(tmp.img, tmp.x, tmp.y, null);
        }
        canvas.translate(-CommonResources.mgnW, -CommonResources.mgnH);
    }

    //-----------------------------
    // 터치 처리 <-- Touch Event (Main Loop)
    //-----------------------------
    public void hitTest(float x, float y) {
        // 타일이 이동중이거나 터치한 타일 없음
        if ( dir != 0  || !getTileNum(x, y) ) return;

        getTileIndex();     // 터치한 타일과 공백 위치 찾기
        getDir();           // 타일의 이동 방향 구하기

        // 이동 이동가능하면
        if (dir > 0) {
            getMoveTiles();    // 이동 가능한 모든 타일 구하기
        }
    }

    //-----------------------------
    // 터치한 Tile 객체 찾기
    //-----------------------------
    private boolean getTileNum(float x, float y) {
        for (Tile tmp : mTile) {
            num = tmp.hitTest(x, y);
            if (num >= 0) break;
        }

        return num >= 0;
    }

    //-----------------------------
    // 배열에서 타일 index 찾기
    //-----------------------------
    private void getTileIndex() {
        // 타일 위치 찾기
        for (int i = 0; i <= cnt; i++) {
            if (tiles[i] == num) {
                idx = i;
                break;
            }
        }

        // 공백 위치 찾기
        for (int i = 0; i <= cnt; i++) {
            if (tiles[i] == -1) {
                blk = i;
                break;
            }
        }
    }

    //-----------------------------
    // 타일 이동 방향 찾기
    //-----------------------------
    private void getDir() {
        // 타일의 배열 index를 직교 좌표로 변환
        int x = idx % Settings.size;
        int y = idx / Settings.size;

        // 공백의 index를 직교 좌표로 변환
        int bx = blk % Settings.size;
        int by = blk / Settings.size;

        dir = 0;
        // 타일과 같은 행 또는 열에 공백 없음
        if (x != bx && y != by) return;

        // 공백이 같은 열에 있음 (이동 방향 : 1 or 3 --> 12시 or 6시)
        if (x == bx) {
            dir = (y > by) ? 1 : 3;
        }

        // 공백이 같은 행에 있음 (이동 방향 : 2 or 4 --> 9시 or 3시)
        if (y == by) {
            dir = (x < bx) ? 2 : 4;
        }
    }

    //-----------------------------
    // 이동할 타일 목록 만들기
    //-----------------------------
    private void getMoveTiles() {
        buffer.clear();
        int tlCnt = Settings.size;

        // 이동할 타일 목록과 타일 index 이동
        switch (dir) {
        case 4:     // 왼쪽
            for (int i = blk + 1; i <= idx; i++) {
                buffer.add(tiles[i]);
                tiles[i - 1] = tiles[i];
            }
            break;
        case 2:     // 오른쪽
            for (int i = blk - 1; i >= idx; i-- ) {
                buffer.add(tiles[i]);
                tiles[i + 1] = tiles[i];
            }
            break;
        case 1:     // 위쪽
            for (int i = blk + tlCnt; i <= idx; i += tlCnt) {
                buffer.add(tiles[i]);
                tiles[i - tlCnt] = tiles[i];
            }
            break;
        case 3:     // 아래
            for (int i = blk - tlCnt; i >= idx; i -= tlCnt) {
                buffer.add(tiles[i]);
                tiles[i + tlCnt] = tiles[i];
            }
            break;
        }

        // 이동 후에는 터치한 타일 위치를 공백으로 설정
        tiles[idx] = -1;

        // Tile 객체에 이동 방향 설정
        for (Integer n : buffer) {
            mTile.get(n).setDir(dir);

            // 타일 이동 회수
            moveCnt++;
        }
    }

    //-----------------------------
    // 타일 이동 <-- Thread
    //-----------------------------
    public void moveTiles() {
        if (dir == 0) return;
        boolean isMove = false;

        // 타일 이동
        for (Integer n : buffer) {
            isMove = mTile.get(n).update();
        }

        // 모든 타일의 이동이 끝났는가?
        if (!isMove) {
            dir = 0;
        }
    }

    //-----------------------------
    // Stage Clear <-- GameView
    //-----------------------------
    public boolean isClear() {
        // 타일을 이동중인가?
        if (dir != 0) return false;

        // 타일이 순서대로 배치되었는가?
        for (int i = 0; i < cnt; i++) {
            if (tiles[i] != i) return false;
        }

        return true;
    }

} // Board
