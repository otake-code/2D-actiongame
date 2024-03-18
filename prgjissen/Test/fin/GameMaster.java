import java.awt.*;
import java.awt.event.*;

public class GameMaster extends Frame implements Runnable, KeyListener {
  int  w=640*2, h=320*2;//画面の大きさ
  int COL =50; //横のブロックの数
  int bw = 64*COL;//ステージ横の大きさ
  int bh= 64*10;//ステージ縦の大きさ
  Thread   th;     // スレッドオブジェクト
  Image    buf;    // 仮の画用紙
  Graphics buf_gc; // グラフィックスコンテキスト
  Cha01    ch01;   // 矢印キーで操作可能なキャラクタのクラス
  Teki teki ,tekib,tekim ,tekib2;
  Map map;
  Dimension d,D;
  int Y,xr =0;
  int mode =0;//初期画面
  int s=32;//人のサイズ
  int dcnt=0;//死亡時間
  /*確認用
  int check=0;
  int map_colli=0;
  int map_colli2=0;*/
  int coin=0;
  double X,VX=1.0;//VXはスクロールの速さ
  boolean spacef ,gcnt ,dl , item=false; //spaceがおされた,　ゲーム終了判定,　死亡判定,　　アイテムゲット
  int coindouble=0;//サボテンの同時ゲット回数
  Font f1 =new Font("Serif",Font.PLAIN,10);
  Fonts font1 ,font2,font3,font4,font5;
  
 
  Image imgBack = getToolkit().getImage("img/haikei3.jpeg");//画像の読み込み
  Image imgdead = getToolkit().getImage("img/deadpic.png");//画像の読み込み
  Image imgef1 = getToolkit().getImage("img/effect_red.png");
  Image imgefy = getToolkit().getImage("img/effect_yellow.png");
  Image imggo = getToolkit().getImage("img/go2.jpg");
  Image imgclr = getToolkit().getImage("img/gameclear.png");
  Image imgmi = getToolkit().getImage("img/mission2.png");
  Image imgmain = getToolkit().getImage("img/main.jpg");
  // ■ メインメソッド（スタート地点）
  public static void main(String [] args) {
    GameMaster ac = new GameMaster(); 
  }

  // ■ コンストラクタ（初期設定）
  GameMaster() {
    super("GameMaster");  // 親クラスのコンストラクタ呼び出し
    this.setSize(w+10, h+10); // フレームの初期設定
    this.setVisible(true);  // フレームを可視化
    this.addKeyListener(this);   // (KeyListener)リスナ登録
    this.requestFocusInWindow(); // (KeyListener)フォーカスを得る

    font1  =new Fonts();//フォントの種類
    font2  =new Fonts();
    font3 =new Fonts();
    font4  =new Fonts();
    font5  =new Fonts();


    ch01 = new Cha01();     // Cha01 クラスのオブジェクトを作成
    ch01.cGetImage(this);   // ch01 についてキャラクタの画像を取得
    ch01.w=bw;
    ch01.h=h;
    //ch01.x =1728;
    //ch01.y=64;
    //ch01.GRAVITY=0.8;
    ch01.y=512;
    ch01.ufcnt=true;
    ch01.hp=3;//プレイヤーのHPは3

    teki = new Teki();//スライム
    tekib =new Teki();//鳥黒
    tekim=new Teki();//魔法使い
    tekib2 =new Teki();//鳥赤
    teki.tGetImage(this,"img/monster/スライム/スライムA_移動000.png" ,"img/monster/スライム/スライムA_移動001.png","img/monster/スライム/スライムA_移動002.png ","img/monster/スライム/スライムA_移動003.png" );
    tekib.tGetImage(this,"img/monster/鳥/鳥A_移動000.png" ,"img/monster/鳥/鳥A_移動001.png","img/monster/鳥/鳥A_移動002.png","img/monster/鳥/鳥A_移動003.png");
    tekim.tGetImage(this,"img/monster/魔法使い/魔法使いA_移動000.png" ,"img/monster/魔法使い/魔法使いA_移動001.png","img/monster/魔法使い/魔法使いA_移動002.png","img/monster/魔法使い/魔法使いA_移動003.png");
    tekib2.tGetImage(this,"img/monster/鳥/鳥B_移動000.png" ,"img/monster/鳥/鳥B_移動001.png","img/monster/鳥/鳥B_移動002.png","img/monster/鳥/鳥B_移動003.png");
    //敵の初期位置と大きさ
    teki.y=512+4;teki.s=64;
    tekib.y=64*2;tekib.s=96;
    tekib2.x=64*48;tekib2.y=64*4;tekib2.s=96;
    tekim.x=64*(50-6);tekim.y=64*2;tekim.s=32*4;
    //tekib.tGetImage()
    d = this.getSize();     // 画面の大きさ取得

    map =new Map();
    map.mGetImage(this);
    map.row= 10;// 列数
    map.col =COL;//行数
    map.w=bw/2;
    map.load();
    //  map.cof=false;

    // スレッドの初期設定
    th = new Thread(this); // スレッドオブジェクトの新規作成
    th.start();            // スレッドの開始
    //=====ウィンドウを閉じれるようにする=====
    addWindowListener(new WindowAdapter() {
    @Override
      public void windowClosing(WindowEvent e) {
        System.exit(0);
        }
      });
  }

  public void run() {  // (thread) 40 msec 毎に実行する
    try {
      while(true) {
	repaint();        // 再描画を要求する repaint() は update() を呼び出す
	Thread.sleep(17); // ウィンドウを更新する前に休止 25fps=1000msec/40msec
      }
    }
    catch(Exception e) {
    }
  }

  // ■ メソッド
  // paint() は Frame クラスのメソッド
  // 中身を上書きする（オーバーライド）
  // paint() は thread により 40msec 毎に repaint() を介して呼ばれる
  public void paint(Graphics gc) { //
    // 以下で，毎回
    // 1. 画面のサイズを取得
    // 2. buffer イメージの存在チェック
    // 3. buffer 用 gc の存在チェック
    // を行う．これは buffer が作られるタイミングの問題で
    // NullPointerException が生じるのを防ぐ為。  

    if (buf == null)
      buf = createImage(bw, d.height); // buffer を画面と同サイズで作成
    if (buf_gc == null)
      buf_gc = buf.getGraphics();

    // buf （仮の画用紙）を描画する
    buf_gc.setColor(Color.black);          // gc の色を黒に
    buf_gc.fillRect(0,0,d.width,d.height); // gc を使って四角を描く

    switch(mode){//ゲーム画面ノモード
      case 0://表紙　ホーム画面
        buf_gc.setColor(Color.yellow);
        buf_gc.fillRect(0,0 ,w, h);
        buf_gc.drawImage(imgBack, 0, Y,w,h, this); 

        //枠のなか
        buf_gc.setColor(Color.black);
        buf_gc.fillRect(64+32,32+12, w-64*4+32, h/2-32*2);
        Graphics2D g2 = (Graphics2D)buf_gc;
        BasicStroke bs = new BasicStroke(20);
		    g2.setStroke(bs);
        //枠
        buf_gc.setColor(Color.red);
        buf_gc.drawRect(64+32,32+12, w-64*4+32, h/2-32*2);
        //文章を表示
        map.sabotenn(buf_gc,64+32*6,h/2+64,64,"×15 を集めてゴールしよう");
        buf_gc.setColor(Color.yellow);
        font1.Fon(buf_gc , "横スクロール型アクションゲーム" ,  64,64*2,64*2 );
        buf_gc.setColor(Color.white);
        font1.Fon(buf_gc , "サボテンあつめ" ,  96,w/2-96*4,64*4  );
        buf_gc.setColor(Color.black);
        font1.Fon(buf_gc , "---PUSH SPACE---" ,  32,w/2-32*4,h-64 );
        buf_gc.setColor(Color.black);
        font4.Fon(buf_gc , "学籍番号:" , 32,w-10*32,h-64  );
        buf_gc.setColor(Color.black);
        font5.Fon(buf_gc , "作成者:岡田壮央" ,  32,w-9*32,h-64+32);

        gc.drawImage(buf, 0, 0, this); // 表の画用紙に裏の画用紙 (buf) の内容を貼り付ける
        break;


      case 1://game 中

         //初期化
         if(gcnt==true){
          ch01.x=60; ch01.y=512;//x=ch01.cnt
          ch01.vy=0;
          ch01.hp=3;
          ch01.cond2=2;
          ch01.onestep=4;
          ch01.GRAVITY=1.1;
          ch01.ufcnt=true;

          teki.x = tekib.x=640*2; tekim.x=64*(COL-6);tekib2.x=640*(COL-2);
          teki.hp=tekib.hp=tekib2.hp=tekim.hp=0;
          teki.tekicolli=tekib.tekicolli=tekib2.tekicolli=tekim.tekicolli=false;
          
          dcnt=0;
          dl=false;gcnt=false;item=false;
          coin=0;
          map.load();
        }
        
        
        //==offset　移動した画面の位置
        int offsetX = d.width / 2 - (int)ch01.x;
        // マップの端ではスクロールしないようにする
        offsetX = Math.min(offsetX, 0);
        offsetX = Math.max(offsetX, d.width - bw);

        //背景
        buf_gc.drawImage(imgBack, 0, Y,w,h, this); 

        //コインゲット
        if (map.coinf ||map.coinf2&&!map.double_coin){
          coin+=1;
        }
        if (map.double_coin){//同時にゲット
          coin+=2;
        }
        //アイテムゲット
        if (map.blockID==99){//ランダムで効果を変える
          item=true;
        }
        //マップを描画
        map.mDraw(buf_gc , (int)offsetX,0);
        map.mUpdate();

        //キャラクターを描画
        ch01.cDraw(buf_gc,offsetX,0); // 仮の画用紙の GC を Cha01 のオブジェクトに渡す
        //HP
        ch01.cHP(buf_gc ,ch01.hp);
        //ゲットしたサボテン
        map.mCoin(buf_gc,coin);
        //アイテム効果
        if(item)ch01.onestep=8; // 
        ch01.cUpdate(offsetX, map.cof, map.cof2,map.block_x,map.block_y); // キャラクタのパラメータ更新
        //衝突判定
        map.colli(ch01.newX, ch01.y   ,offsetX,0);
        map.colli2(  ch01.x , ch01.newY,offsetX,0);

        //敵
        teki.tDraw(buf_gc,offsetX,0,true);
        teki.tUpdate();
        tekib.tDraw(buf_gc,offsetX,0,true);
        tekib.tUpdate();
        tekib2.tDraw(buf_gc,offsetX,0,true);
        tekib2.tUpdate();
        tekim.tDraw(buf_gc,offsetX,0,false);
        tekim.tUpdate();

        buf_gc.setColor(Color.black);

        //if(ch01.onFace==true) check+=1;//衝突回数
        //if(map.cof==true)map_colli +=1;
        //ミッション
        //buf_gc.drawImage(imgmi, 16 ,32-4+16,640/3+64,320/4+32, this);  
        int Mute =teki.Muteki+ tekib.Muteki+tekib2.Muteki+tekim.Muteki; //無敵を付与している時間


        //=============-状況を左上に表示（ゲームには関係ない）=========================
        //キャラの位置速度、接したブロックの位置、offsetなどをリアルタイムで確認する用
        buf_gc.setFont(f1);
        int i=0;
        if(ch01.onGround)i+=1;
        //コインに当たったかどうか
        int coin2=0;
        if(map.coinf2)coin2+=1;
        int coin1=0;
        if(map.coinf)coin1+=1;

        
        if(map.double_coin)coindouble+=1;
        
        double[] num ={(double)coindouble ,(double)map.cnt3 ,ch01.x ,ch01.y,offsetX,(double)Mute,ch01.vy ,i,map.block_x,map.block_y -64 ,map.blockID};
        String[] name =
        { "X軸の衝突= ",
          "Y軸の衝突= ",
          "プレイヤーのx座標 ch01.x = ",
          "プレイヤーのx座標 ch01.y = ",
          "offsetX = ",
          "ALL 無敵時間= ",
          "vy = ",
          "onGround = ",
          "ブロックの左下座標X = ",
          "ブロックの左下座標Y = ",
          "ブロックのID = "
        };
        String str[] = new String[num.length];

        for(int j=0 ;j< num.length ; j++){
          str[j] = String.valueOf(num[j]);
          //buf_gc.drawString(name[j]+str[j],64*3/2,64*3/2 +10*j);　//本番はこの文をコメントアウトする
        }
        //===============================================        
        
        //==敵生成
        if (teki.hp==0||tekib.hp==0||tekim.hp==0||tekib2.hp==0){
          //リスポーンした敵のｘや速度を変更可能
          if(Math.random()<0.4/60 &&teki.hp==0){//ランダム生成 確率20% 
            teki.hp=1;             
          }
          if(Math.random()<60/60&&tekib.hp==0){//ランダム生成 確率100%    
            tekib.hp=1;             
            tekib.x=64*49;
            tekib.vx=2;
          }
          if(Math.random()<60/60&&tekib2.hp==0){//ランダム生成 確率100%   
            tekib2.hp=1;             
            tekib2.x=64*49;
            tekib2.vx=3;
          }
          if(Math.random()<60/60&&tekim.hp==00){//ランダム生成 確率100%    
            tekim.hp=1;             
          }
        }
        //キャラとの衝突判定
        teki.tcol(teki , ch01.x,ch01.y ,0 ,teki.Muteki);
        tekib.tcol(tekib , ch01.x,ch01.y ,0,tekib.Muteki);
        tekib2.tcol(tekib2 , ch01.x,ch01.y ,0,tekib2.Muteki);
        //tekim.s=tekim.s/2;
        tekim.tcol(tekim , ch01.x,ch01.y , tekim.s*3/4,tekim.Muteki);
      
        //HPがありかつ無敵が付与されたHP減少
        if(ch01.hp>0 && Mute==1) {
          ch01.hp-=1;      
          //teki.tekicolli=tekib.tekicolli =tekim.tekicolli=false;
        }
        //敵と当たったら赤いエフェクトがでる
        if(ch01.hp>0 && Mute>=1&&Mute <=60)buf_gc.drawImage(imgef1,(int)ch01.x+offsetX,(int)ch01.y,96,96, this); 
        //死亡条件がそろったら死亡する
        if(map.blockID==38 || ch01.y==640+64|| map.blockID==91||ch01.hp==0) { 
          dl=true;
        }
        //死亡後0.5秒は画面遷移しない
        if(dl==true){
          dcnt+=1;
          
          if(dcnt<30 &&dcnt >=0){
            //ゴールに着いたら
            if(map.blockID==38)buf_gc.drawImage(imgefy,(int)ch01.x+offsetX-dcnt*20,(int)ch01.y -dcnt*20,96+dcnt*40,96+dcnt*40, this); 
              
            if(map.blockID==91)ch01.x=64*23.25;//マグマに落ちたら
            //そのほか
            else if(map.blockID!=38) buf_gc.drawImage(imgef1,(int)ch01.x+offsetX,(int)ch01.y,96,96, this); 
          }
          else {
            if(map.blockID==38)mode=2;//クリア画面へ
            else mode=-1;//ゲームオーバー画面へ
          }
        }

       gc.drawImage(buf, 0, 0, this); // 表の画用紙に裏の画用紙 (buf) の内容を貼り付ける 

      break;

      case -1: //gameover
      buf_gc.fillRect(0,0 ,w, h);
      buf_gc.drawImage(imggo, 0,0,w,h, this); //ゲームオーバー画面を表示
      gcnt=true;
      gc.drawImage(buf, 0, 0, this); // 表の画用紙に裏の画用紙 (buf) の内容を貼り付ける
      break;

      case 2: //gameclr
      buf_gc.fillRect(0,0 ,w, h);
      buf_gc.drawImage(imgclr, 0 ,0,w,h, this); //クリア画面を表示
      gcnt=true;
      gc.drawImage(buf, 0, 0, this); // 表の画用紙に裏の画用紙 (buf) の内容を貼り付ける
      break;
    }
    //gc.drawImage(buf, 0, 0, this); // 表の画用紙に裏の画用紙 (buf) の内容を貼り付ける
    
  }   

  // ■ メソッド
  // これを入れないとダブルバッファリングしてもちらつく
  // （repaintメソッドが呼ばれると，update が呼ばれ、いったん背景色で
  //  塗りつぶす作業が行われるため，updata メソッドをオーバーライドし，
  // そのままpaint を呼ぶようにする必要がある）
  public void update(Graphics gc){
    paint(gc);
    // 確認用　space押したら画面遷移
    if(spacef==true){
      mode+=1; // フラグを立てる
      if(mode>2)mode=0;
      spacef=false;
    }
  }

  public void keyTyped(KeyEvent ke) {}

  public void keyPressed(KeyEvent ke) {
    int cd = ke.getKeyCode();
    switch (cd) {
    case KeyEvent.VK_SPACE: // [ー]キーが押されたら、
      
      break;  
    case KeyEvent.VK_LEFT:  // [←]キーが押されたら
      ch01.lf = true;   // フラグを立てる
      //	    System.out.println(" [←] pressed");
      break;
    case KeyEvent.VK_RIGHT: // [→]キーが押されたら
      ch01.rf = true;   // フラグを立てる
      //	    System.out.println(" [→] pressed");
      break;
    case KeyEvent.VK_UP: // [↑]キーが押されたら
      ch01.uf = true;   // フラグを立てる
      //ch01.ufcnt =true;//おしっぱを防ぐ
      //	    System.out.println(" [↑] pressed");
      break;
    case KeyEvent.VK_DOWN: // [↓]キーが押されたら
      ch01.df = true;   // フラグを立てる
      //	    System.out.println(" [↓] pressed");
      break;
    }
  }
  public void keyReleased(KeyEvent ke) {
    int cd = ke.getKeyCode();
    switch (cd) {
    case KeyEvent.VK_SPACE: // スペースキーが離されたら  
      spacef = true; // フラグを降ろす 
      break;
      
    case KeyEvent.VK_LEFT:  // [←]キーが離されたら
      ch01.lf = false;  // フラグを降ろす
      //	    System.out.println(" [←] released");
      break;
    case KeyEvent.VK_RIGHT: // [→]キーが離されたら
      ch01.rf = false;  // フラグを降ろす
      //	    System.out.println(" [→] released");
      break;
    case KeyEvent.VK_UP: // [↑]キーが離されたら
      ch01.uf = false;  // フラグを降ろす
      ch01.ufcnt = true; //おしっぱ防止　離されたら再度ジャンプの入力を受け付ける
      //	    System.out.println(" [↑] released");
      break;
    case KeyEvent.VK_DOWN: // [↓]キーが離されたら
      ch01.df = false;  // フラグを降ろす
      //	    System.out.println(" [↓] released");
      break;
    }
  }
}
