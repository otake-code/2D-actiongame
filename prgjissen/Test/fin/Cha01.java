import java.awt.Point;
import java.awt.*;
import java.awt.event.*;
/** 矢印キーで操作可能なキャラクタのクラス
    ・キャラクターの画像読み込み
    ・キャラクターの描画
    ・キャラクターの位置とパラメータの更新
    を行う

    キャラクターのパラメータ： 体の向きと足（左右のどちらを前に出しているか）
 */
class Cha01 extends Frame {
  // ■ フィールド変数
  // s: キャラクタのサイズ （画像の大きさ（ピクセル））
  // cond1: キャラの右足が前 (-1) か左足が前 (1) か
  // cond2: キャラの向き（４種類）
  // cw, cwMax:  足の入れ替え速度
  // cnt: フレーム数
  // onestep: 一歩の幅の長さ
  //jumptime: jump中の時間
  //hp: キャラのHP
  //GRAVITY:重力
  int w=960*2 , h =320*2;//画面の大きさ
  int s= 32, JumpSpeed=24,cond1=1, cond2=2, cw=0, cwMax=5*4,t=0,cnt=60,onestep=4 ,jumptime,hp;
  double GRAVITY =1.1, x=80, y=h-4*s,vx, vy=0 ,newX,newY;//x,yは初期位置 newX,newYは次に動く位置
  int y_start = h-4*s;
  boolean lf, rf, uf, df,onGround=true; // left, right, up, down　//ブロックに衝突したらonGround=true
  boolean ufcnt , onFace=false; //ufcnt: ボタン押しっぱでジャンプするのを防止　onFace: ブロックと面している
  Image cimg; // キャラクタ画像
  Map map;
  Cha01(){
    map = new Map();
  }

  //初期位置　

  // ■ メソッド
  // 画像をファイルから cimg に読み込む
  void cGetImage (GameMaster ac){
    cimg = getToolkit().getImage("img/chara01.gif");
  }

  // ■ メソッド
  // 画像を切り取り、描画する
  void cDraw (Graphics g,int offsetX, int offsetY){
    int imgx, imgy;
    if (cond1 == 1) imgx=0; // 画像の切り取り場所 (x座標) 足踏み
    else            imgx=s; // 画像の切り取り場所 (x座標)
    imgy = cond2*s; // 画像の切り取り場所 (y座標)
    g.drawImage(cimg, (int)x+offsetX,(int) y+ offsetY, (int)x+64+offsetX, (int)y+64 +offsetY, imgx, imgy, imgx+s, imgy+s, null);
    //	System.out.println("(x, y)=" + x + " " + y);
  }

  // ■ メソッド
  // キャラクタの位置と状態を更新する
  // x, y: 座標
  // vx, vy: 速度
  // cond1: 右足が前か左足が前か
  // cond2: 
  void cUpdate (int offsetX, boolean map_cof ,boolean map_cof2 ,int block_x,int block_y){
    //if(cnt<960-640)cnt=(int)x;
    if(!map_cof) onFace=false;//衝突がないなら面していない 
    else if(map_cof) onFace=true;
    if (lf && !rf){ // 左キーを押し，右キーを押していないなら
      vx=-1;        // x 方向の速度は -1
      cond2=3;      // キャラは左向き
      if(x>48 && !onFace)cnt-=onestep; //人が一度に進む距離　
    }
    else if (rf && !lf){ // 右キーを押し，左キーを押していないなら
      vx=1;         // x 方向の速度は +1
      cond2=1;      // キャラは右向き
      if(x<(w-110)&& !onFace)cnt+=onestep;
    }
    else vx=0;

    if (uf && !df && ufcnt){//jump
      //cond2=0;
      //ufcnt=true;
      if(onGround){
        vy =-JumpSpeed;
        onGround=false;
        ufcnt=false;
      }
    }

    /* 調整用
    //下ボタンで地上に戻る
    else if (df && !uf){
      vy=0;
      y=512;
    }*/
    //ステージ外には出ない
    if(x<48)x=48;
    if(x>(w-110))x=w-110;
  
    //=====当たり判定
    //// x方向
    // 移動先座標を求める
    newX =x +vx*onestep; //次のｘ座標
    if(!map_cof ){
      x=newX;// 衝突するタイルがなければ移動
    }
    if(map_cof &&!map_cof2  ){// 衝突するタイルがある場合

      if (vx > 0) {// 右へ移動中なので右のブロックと衝突
        // ブロックにめりこむ or 隙間がないように位置調整
        x=block_x-(64-16);//
      } else if (vx < 0) {// 左へ移動中なので左のブロックと衝突
        // 位置調整
        x=block_x+(64-16);
      }
      vx = 0;
    }
    if(!ufcnt){//ジャンプ中はjumptimeが増える
      jumptime+=1;
    }if(onGround) jumptime=0;

    //// y方向の当たり判定
    vy+= GRAVITY;//1.1
    // 移動先座標を求める
    newY = y + vy; //vy=1.1
    // 移動先座標で衝突するタイルの位置を取得
    // y方向だけ考えるのでx座標は変化しないと仮定
    if (!map_cof2 ) {// 衝突するタイルがなければ移動
      y = newY;
      onGround = false;// 衝突してないということは空中
    } if(map_cof2 ){// 衝突するタイルがある場合

      if (vy > 0 ) {// 下へ移動中なので下のブロックと衝突（着地）
        // 位置調整
        y=block_y-64;
        vy = 0;// 着地したのでy方向速度を0に
        onGround = true; // 着地

      } else if (vy < 0 &&jumptime>1) {// 上へ移動中なので上のブロックと衝突
        // 位置調整
        y=block_y+64;
        vy = -1.101;// 天井にぶつかったのでy方向速度を0に
        onGround = false;

      }else if(vy<0 && jumptime <=1){//Jumpしようとしているとき
        y=newY;
        onGround = false;
      }
    }
    if(map_cof&&map_cof2){//斜めから
      if(vx>0&&block_x-(64-20)==newX){//左から
        x=block_x-(64-16);//押し戻す
        vx = 0;
        if(vy>0){//上からなら
          y=block_y-64;
          vy = 0;
          // 着地
          onGround = true;
        }
        if(vy<0){//下からなら
          y=newY;
          onGround = false;
        }
      }if(vx<0&&block_x+(64-20)==newX){//右から
        x=block_x+(64-16);
        vx = 0;
        if(vy>0){//上からなら
          y=block_y-64;
          vy = 0;
          // 着地
          onGround = true;
        }
        if(vy<0){
          y=newY;//下からなら
          onGround = false;
        } 
      }
    }


    //落下したら地下で停止
    if(y>320*2){
      y=640+64;
      vy=0;
    }
 
    if(lf || rf ||uf){//動作中
      if (cw < cwMax*3/4) // 時間が経っていないなら 1/3sec cmax=20
        cw++;   // 足の入替に関するカウントを１増やす
      else {
        cond1 = cond1 * (-1); // 時間が経てば足を左右入替えて
        cw = 0; // カウントをリセット
      }
    }
    if(!lf &&!rf&&!uf){//制止中
      if (cw < 25) // 時間が経っていないなら
        cw++;   // 足の入替に関するカウントを１増やす
      else {
        cond1 = cond1 * (-1); // 時間が経てば足を左右入替えて
        cw = 0; // カウントをリセット
      }
    }
    //	System.out.print("flags: " + lf + " " + rf + " " + uf + " " + df + "   ");
  }

  // ■ メソッド
  //キャラのHPを表示する
  void cHP(Graphics buf_gc, int h ){
    //背景の白
    buf_gc.setColor(Color.BLACK);
    buf_gc.fillRect(( 640*2 -32*11/2)-64-64 , (64+32 -40), 64+64-12,64-6 );

    int imgx=0, imgy=2*s;
    //キャラ
    buf_gc.drawImage(cimg,( 640*2 -32*11/2 -64*2), (64+32 -40) +6, (640*2 -32*11/2+40 -64*2) ,(64+40) +6, imgx, imgy, imgx+s, imgy+s, null);
  
    String msg ="× ";
    Font f2 =new Font("Serif",Font.BOLD,40);
    buf_gc.setColor(Color.red);
    buf_gc.setFont(f2);
    //HPのかず
    buf_gc.drawString(msg+h, 640*2 -64*2 -64*2 , 64+32);
}

}

    
    