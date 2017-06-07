package my.com.breakout;

public class Stage {
    //  Map Data
    static private String[][] map = {
        // Stage 0
        new String[] {
                "   1 1 1 1 1 1  ",
                "  > 1 . . . 1   ",
                "  > 1 >2 2 >1   ",
                "  > 1 >3 3 >1   ",
                "  > 1 >2 2 >1   ",
                "  > 1 . . . 1   ",
                "   1 1 1 1 1 1  " },

        // Stage 1
        new String[] {
                "   1 1 1 1 1 1   ",
                "   >1 2 2 2 1    ",
                "   . 1 2 2 1     ",
                "   .> 1 3 1      ",
                "   . . 3 3      ",
                "   .> 1 3 1      ",
                "   . 1 2 2 1     ",
                "   >1 2 2 2 1    ",
                "   1 1 1 1 1 1   "  },

        // Stage 2
        new String[] {
                "  > . . 1        ",
                "   . . 1 1       ",
                "  > . 1 2 1      ",
                "  .  1 2 2 1     ",
                "  > 1 2 3 2 1    ",
                "   1 2 3 3 2 1   ",
                "  > 1 2 3 2 1    ",
                "  .  1 2 2 1     "  },

        // Stage 3
        new String[] {
                "  > . 1 . 1      ",
                "  .  1 2 2 1     ",
                "  > 1 2 3 2 1    ",
                "   1 2 3 3 2 1   ",
                "  > 1 2 3 2 1    ",
                "  .  1 2 2 1     ",
                "  . > 1 2 2      ",
                "  . .  1 1       ",
                "  . . > 1        ",  }
    };

    //-----------------------------
    // 스테이지 만들기 <-- GameView
    //-----------------------------
    static public void makeStage(int stageNum) {
        // 맵을 반복해서 사용
        stageNum = stageNum % map.length;

        // 블록의 크기
        int w = CommonResources.bw;
        int h = CommonResources.bh;

        // 위쪽 마진 - 블록 높이*2 (블록의 y좌표)
        float y = h * 4;    //

        // 맵 데이터 - 스테이지 번호
        String[] tmp = map[stageNum];
        int n = 0;  // 블록 번호

        // 배열의 행만큼 반복
        for (int i = 0; i < tmp.length; i++) {
            String s = tmp[i].trim();   // 좌우 공백 제거
            float x = 0;                // 각 행의 시작 x좌표

            // 글자 수만큼 반복
            for (int j = 0; j < s.length(); j++) {
                switch ( s.charAt(j) ) {
                case '.' :       // 1칸 공백
                    x += w * 2;
                    break;
                case '>' :        // 반칸 공백
                    x += w;
                    break;
                case '1' :
                case '2' :
                case '3' :        // ArrayList에 추가
                    n = Integer.parseInt( s.substring(j, j + 1) );
                    GameView.mBlock.add( new Block(n, x + w, y + h) );
                    x += w * 2;
                }
            } // for j

            y += h * 2;
        } // for i
    }

} // Stage
