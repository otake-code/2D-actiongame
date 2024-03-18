import java.awt.Point;
import java.awt.*;
import java.awt.event.*;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.*;
import java.awt.Image;
import java.util.LinkedList;
import java.util.Iterator;



class Map extends Frame {
    // ■ フィールド変数
    int w=32*50,h=320;//マップは２倍に拡大しているため普通の大きさで考える
    Image stageimg;
    int X=0,Y=0,s=32;//20X10
    int  MAPCHIP_WIDTH =32;					// マップチップ１パーツの横幅
    int  MAPCHIP_HEIGHT =32;				// マップチップ１パーツの縦幅
    int  MAP_SIZE_WIDTH = w / MAPCHIP_WIDTH	;	// MAPの横幅(値はチップ数)
    int  MAP_SIZE_HEIGHT = h / MAPCHIP_HEIGHT;		// MAPの縦幅(値はチップ数) 配列の中身のかずと対応させる
    int width_chip_num=8; //横のチップ数
    int height_chip_num=16;//縦のチップ数
    int position_x;//描画先の位置
    int position_y;
    int cnt2,cnt3;//衝突回数
    boolean coinf=false,coinf2=false ,double_coin=false; //サボテンをゲットしたかどうか　同時にゲットした場合double_coinがtrueとなり識別＋2となる
    boolean cof ,cof2;//衝突したかどうか
    int row=10;// 列数
    int col=200;//行数
    private int[][] map;
    int block_x,block_y,blockID;//ブロックの位置とID
    double absPBx,absPBy;//プレイヤーとブロックとの距離

    // ■ メソッド
    //txtからステージ読み込み
    void load() {
           // マップ
        try {
            // txtファイルを開く
            BufferedReader br = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream("img/map.txt")));  
            // txtの数字と位置に対応したマップを作成
            map = new int[row][col];
            for (int i = 0; i < row; i++) {
                String[] line= br.readLine().split("\t");
                for (int j = 0; j < col; j++) {
                    map[i][j] = Integer.parseInt(line[j]+ "");      
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // ■ メソッド
    //画像の読み込み　背景
    void mGetImage (GameMaster ac){
        stageimg = getToolkit().getImage("img/map2.png");//画像の読み込み
    }

    // ■ メソッド
    //マップを描画
    void mDraw (Graphics buf_gc,int offsetX,int offsetY){
        for(int i=0;i<MAP_SIZE_HEIGHT;i++){
            for(int j=0;j<MAP_SIZE_WIDTH;j++ ){
                int chip_id = map[i][j]; //ブロックの種類ごとにIDを設定　
                if (chip_id != 0){// 無効な値かをチェック　0はなにもないため
                
                position_x = 2*MAPCHIP_WIDTH * j;//描画先の位置
                position_y = 2*MAPCHIP_HEIGHT * i;

                int chip_pos_x = (chip_id % width_chip_num)* MAPCHIP_WIDTH;//マップチップの切り取る位置
                int chip_pos_y = (chip_id / width_chip_num) * MAPCHIP_HEIGHT;
                //描画
                buf_gc.drawImage(
                    stageimg, position_x + offsetX, position_y + offsetY, position_x+64+ offsetX, position_y+64+ offsetY,
                    chip_pos_x, chip_pos_y, chip_pos_x+s, chip_pos_y+s, null);
                }
            }
        }  
    }

    // ■ メソッド
    //ｘ方向のブロック衝突
    void colli(double newX ,double Y ,int offsetX,int offsetY){
        cnt2=0;
        coinf2=false;

        for(int i=0;i<MAP_SIZE_HEIGHT;i++){
            for(int j=0;j<MAP_SIZE_WIDTH;j++ ){
                int chip_id = map[i][j];
                
                position_x = 2*MAPCHIP_WIDTH * j  ;//描画先の位置
                position_y = 2*MAPCHIP_HEIGHT * i;// 

                if (chip_id == 0||chip_id==99)continue;//ブロックがなければ無視
                //ブロックにぶつかっているか
                absPBx=newX -(double)position_x;
                absPBy=Y -(double)position_y;
                if(absPBx==0&&absPBy==0)continue; //接しているのは無視
            
                //ｘ方向
                if( Math.abs(absPBx) < (64 -16)  && Math.abs(absPBy) <= (64-1)){//衝突しているなら　//プレイヤーの幅48　画像に対応して横幅の当たり判定を狭くする
                    if(chip_id == 19 && Y == (double)position_y ){//サボテンに触れたら
                        coinf2=true;//サボテンゲット
                        map[i][j]=0;//サボテン消滅
                    }if(chip_id==19 && Y != (double)position_y ){
                    }
                    else {
                        block_x=position_x;//左から右に向かってぶつかったので押し返す
                        cnt2+=1;//ぶつかったらカウント
                    }
                    blockID =chip_id;//上から踏んだら反応
                    if(chip_id==19&& Y == (double)position_y)coinf=true;else coinf =false;
                }   
            }
        }  
        if(cnt2>0)cof=true;//ぶつかっていたら衝突ON
        else {
            cof=false;
            cnt2=0;//ぶつかった回数をリセット
        }
    } 
    // ■ メソッド
    //ｙ方向のブロック衝突
    void colli2(double x ,double newY ,int offsetX,int offsetY){
        //床と壁は考えなくて良いからforの一部を減らす
        cnt3=0;
        coinf=false;
        double_coin=false;

        for(int i=0;i<MAP_SIZE_HEIGHT;i++){//地面
            for(int j=0;j<MAP_SIZE_WIDTH;j++ ){//壁
                int chip_id = map[i][j];//要素の数字
                
                position_x = 2*MAPCHIP_WIDTH * j  ;//描画先の位置//MAPCHIPは32
                position_y = 2*MAPCHIP_HEIGHT * i;// offsetY =0

                if (chip_id == 0)continue;//ブロックがなければ無視
                //ブロックにぶつかっているか
                absPBx=x -(double)position_x;
                absPBy=newY -(double)position_y;
                if(absPBx==0&&absPBy==0)continue;

                //y方向
                if( Math.abs(absPBx) <(64 -16) && Math.abs(absPBy) <= (64-1)){//衝突しているなら　・間隔は４３
                    //if(absPBx<0)block_x=position_x;//左から右
                    if(chip_id==19||chip_id==99){//サボテンに触れると消える 
                        if(chip_id==19&& newY == (double)position_y)coinf=true;
                        map[i][j]=0;
                    }
                    else{
                        if(absPBy<0)block_y=position_y;//上向き  OK
                        //if(absPBx>0)block_x=position_x;//右から左
                        if(absPBy>0)block_y=position_y;//下向き
                        cnt3+=1;//衝突回数
                    }
                    blockID =chip_id;//上から踏んだら反応
                    if(chip_id==19)coinf=true;else coinf =false;
                }   
            }
        }  
        if(cnt3>0){
            //if(coinf) double_coin=true;
            //else
            cof2=true;//衝突ON
        }    
        else {
            cof2=false;
            cnt3=0;
        }
    } 

    // ■ メソッド
    //ゲットしたサボテンと数を表示
    void mCoin(Graphics buf_gc ,int coin){
        //背景の白
        buf_gc.setColor(Color.BLACK);
        buf_gc.fillRect(( 640*2 -32*11/2), (64+32 -40), 64+64,64-6 );

        int chip_id=19;//サボテンはIDが19
        int chip_pos_x = (chip_id % width_chip_num)* MAPCHIP_WIDTH;//マップチップの切り取る位置
        int chip_pos_y = (chip_id / width_chip_num) * MAPCHIP_HEIGHT;
        //サボテン
        buf_gc.drawImage(stageimg,( 640*2 -32*11/2), (64+32 -40) +6, (640*2 -32*11/2+40) ,(64+40) +6, chip_pos_x, chip_pos_y, chip_pos_x+s, chip_pos_y+s, null);
        String msg ="× ";
        Font f2 =new Font("Serif",Font.BOLD,40);
        buf_gc.setColor(Color.green);
        buf_gc.setFont(f2);
        //サボテンのゲットした個数
        buf_gc.drawString(msg+coin, 640*2 -64*2 , 64+32);
      }

    // ■ メソッド
    //サボテンの絵と文を表示
     void sabotenn(Graphics buf_gc,int X ,int Y , int size ,String text){
        int chip_id=19;
        int chip_pos_x = (chip_id % width_chip_num)* MAPCHIP_WIDTH;//マップチップの切り取る位置
        int chip_pos_y = (chip_id / width_chip_num) * MAPCHIP_HEIGHT;
        //サボテン
        buf_gc.drawImage(stageimg,X, Y, X+size ,Y+size, chip_pos_x, chip_pos_y, chip_pos_x+s, chip_pos_y+s, null);
        String msg =text;
        Font f2 =new Font("Serif",Font.BOLD,size);
        buf_gc.setColor(Color.black);
        buf_gc.setFont(f2);
        //サボテンの横につく文章
        buf_gc.drawString(msg, X+size , Y+size-5);
    }     
    // ■ メソッド
    //マップは変わらないため今回は使用しない
    void mUpdate (){
    }
}

