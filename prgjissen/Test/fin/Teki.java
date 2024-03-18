import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageWriteParam;

class Teki extends Frame {
    // ■ フィールド変数
    // s: キャラクタのサイズ （画像の大きさ（ピクセル））
    // cond1: キャラの右足が前 (-1) か左足が前 (1) か
    // cond2: キャラの向き（４種類）
    // cw, cwMax:  足の入れ替え速度
    //　hp:敵のHP
    //　Muteki:無敵時間
    int s=64,  y=320*2-128, vy=1, w=640*2,cond1=1, cond2=2, cw=0, cwMax=5,hp=0,Muteki=0;
    int speed =2;
    double  x=640*2,vx=1,tmp =0,cnt=0,time=60;//敵の位置　, 速さ　, 0から60をループ　, 内部時間　,1秒間に60フレーム
    boolean lf, rf, uf, df , tekicolli; // left, right, up, down ,衝突判定
    Image t1img,t2img ,t3img, t4img; // 敵の動きの画像
    Image Images[]=new Image[4];
    //初期位置　
    // ■ メソッド
    // 画像をファイルから cimg に読み込む
    void tGetImage (GameMaster ac ,String img ,String img2 ,String img3,String img4){
      t1img = getToolkit().getImage(img);
      t2img= getToolkit().getImage(img2);
      t3img= getToolkit().getImage(img3);
      t4img=getToolkit().getImage(img4);
      Images[0]=t1img;
      Images[1]=t2img;
      Images[2]=t3img;
      Images[3]=t4img;
    }
  
    // ■ メソッド
    // 画像を切り取り、描画する
    void tDraw (Graphics g,int offsetX,int offsetY ,boolean f){
      //int imgx, imgy;
      if(hp==1){//HPがあるなら
        cnt +=1;//　時間が経過 1/60sec
        tmp =cnt%time;//cnt % 60 tmpは０から６０
        //4種類の画像を順番に表示
        if( (0<=tmp && tmp<=(15) ) )//1/3sec で交代
          g.drawImage(t1img, (int)x+offsetX, y,s, s, this);
        if(((15)<tmp&&tmp<=(30)))//1/3sec で交代
          g.drawImage(t2img, (int)x+offsetX, y,s, s, this);  
        if((30)<tmp&&tmp<=(45))//1/3sec で交代
          g.drawImage(t3img,(int)x+offsetX, y,s, s, this);
        if((tmp> (45) &&tmp<=(60)))//1/3sec で交代
          g.drawImage(t4img,(int)x+offsetX, y,s, s, this);  
        if(f)x=x-vx;//敵の移動
        
        if(!f){//ゴール前の敵は上下に移動
          if(vy>0)y+=1*speed;
          else y-=1*speed;
          if(y>64*8) { //画面から消えそうならで逆向きに移動
            y=64*8;
            vy=-1;
          }
          if(y<64*2) {
            y=64*2;
             vy=1;
          }
        }  
        if(x<48){ //ステージの左端に着いたら死亡
          x=w;
          cnt =0;
          hp=0;
        }  
      }
    }
    
    // ■ メソッド
    //敵との衝突
    void tcol(Teki teki , double plx ,double ply,double w,int Mute ){
      //60秒無敵　キャラクタと敵の画像がかぶったら衝突
      if(Muteki <=60 &&Muteki >=0 &&hp==1 && Math.abs(plx-x) <= (s-w) && Math.abs(ply-y) <= (s-w) ){
        tekicolli=true;//ぶつかったら無敵を付与
        Muteki+=1;//無敵時間の計測
      }
      else  Muteki=0;//60秒を超えたら無敵がなくなる
    }
    
     // ■ メソッド
     //今回は使用していない
    void tUpdate (){
    }
  }
  