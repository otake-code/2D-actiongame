import java.awt.*;
import java.awt.event.*;

public class GameMaster extends Frame implements Runnable, KeyListener {
  int  w=640*2, h=320*2;//��ʂ̑傫��
  int COL =50; //���̃u���b�N�̐�
  int bw = 64*COL;//�X�e�[�W���̑傫��
  int bh= 64*10;//�X�e�[�W�c�̑傫��
  Thread   th;     // �X���b�h�I�u�W�F�N�g
  Image    buf;    // ���̉�p��
  Graphics buf_gc; // �O���t�B�b�N�X�R���e�L�X�g
  Cha01    ch01;   // ���L�[�ő���\�ȃL�����N�^�̃N���X
  Teki teki ,tekib,tekim ,tekib2;
  Map map;
  Dimension d,D;
  int Y,xr =0;
  int mode =0;//�������
  int s=32;//�l�̃T�C�Y
  int dcnt=0;//���S����
  /*�m�F�p
  int check=0;
  int map_colli=0;
  int map_colli2=0;*/
  int coin=0;
  double X,VX=1.0;//VX�̓X�N���[���̑���
  boolean spacef ,gcnt ,dl , item=false; //space�������ꂽ,�@�Q�[���I������,�@���S����,�@�@�A�C�e���Q�b�g
  int coindouble=0;//�T�{�e���̓����Q�b�g��
  Font f1 =new Font("Serif",Font.PLAIN,10);
  Fonts font1 ,font2,font3,font4,font5;
  
 
  Image imgBack = getToolkit().getImage("img/haikei3.jpeg");//�摜�̓ǂݍ���
  Image imgdead = getToolkit().getImage("img/deadpic.png");//�摜�̓ǂݍ���
  Image imgef1 = getToolkit().getImage("img/effect_red.png");
  Image imgefy = getToolkit().getImage("img/effect_yellow.png");
  Image imggo = getToolkit().getImage("img/go2.jpg");
  Image imgclr = getToolkit().getImage("img/gameclear.png");
  Image imgmi = getToolkit().getImage("img/mission2.png");
  Image imgmain = getToolkit().getImage("img/main.jpg");
  // �� ���C�����\�b�h�i�X�^�[�g�n�_�j
  public static void main(String [] args) {
    GameMaster ac = new GameMaster(); 
  }

  // �� �R���X�g���N�^�i�����ݒ�j
  GameMaster() {
    super("GameMaster");  // �e�N���X�̃R���X�g���N�^�Ăяo��
    this.setSize(w+10, h+10); // �t���[���̏����ݒ�
    this.setVisible(true);  // �t���[��������
    this.addKeyListener(this);   // (KeyListener)���X�i�o�^
    this.requestFocusInWindow(); // (KeyListener)�t�H�[�J�X�𓾂�

    font1  =new Fonts();//�t�H���g�̎��
    font2  =new Fonts();
    font3 =new Fonts();
    font4  =new Fonts();
    font5  =new Fonts();


    ch01 = new Cha01();     // Cha01 �N���X�̃I�u�W�F�N�g���쐬
    ch01.cGetImage(this);   // ch01 �ɂ��ăL�����N�^�̉摜���擾
    ch01.w=bw;
    ch01.h=h;
    //ch01.x =1728;
    //ch01.y=64;
    //ch01.GRAVITY=0.8;
    ch01.y=512;
    ch01.ufcnt=true;
    ch01.hp=3;//�v���C���[��HP��3

    teki = new Teki();//�X���C��
    tekib =new Teki();//����
    tekim=new Teki();//���@�g��
    tekib2 =new Teki();//����
    teki.tGetImage(this,"img/monster/�X���C��/�X���C��A_�ړ�000.png" ,"img/monster/�X���C��/�X���C��A_�ړ�001.png","img/monster/�X���C��/�X���C��A_�ړ�002.png ","img/monster/�X���C��/�X���C��A_�ړ�003.png" );
    tekib.tGetImage(this,"img/monster/��/��A_�ړ�000.png" ,"img/monster/��/��A_�ړ�001.png","img/monster/��/��A_�ړ�002.png","img/monster/��/��A_�ړ�003.png");
    tekim.tGetImage(this,"img/monster/���@�g��/���@�g��A_�ړ�000.png" ,"img/monster/���@�g��/���@�g��A_�ړ�001.png","img/monster/���@�g��/���@�g��A_�ړ�002.png","img/monster/���@�g��/���@�g��A_�ړ�003.png");
    tekib2.tGetImage(this,"img/monster/��/��B_�ړ�000.png" ,"img/monster/��/��B_�ړ�001.png","img/monster/��/��B_�ړ�002.png","img/monster/��/��B_�ړ�003.png");
    //�G�̏����ʒu�Ƒ傫��
    teki.y=512+4;teki.s=64;
    tekib.y=64*2;tekib.s=96;
    tekib2.x=64*48;tekib2.y=64*4;tekib2.s=96;
    tekim.x=64*(50-6);tekim.y=64*2;tekim.s=32*4;
    //tekib.tGetImage()
    d = this.getSize();     // ��ʂ̑傫���擾

    map =new Map();
    map.mGetImage(this);
    map.row= 10;// ��
    map.col =COL;//�s��
    map.w=bw/2;
    map.load();
    //  map.cof=false;

    // �X���b�h�̏����ݒ�
    th = new Thread(this); // �X���b�h�I�u�W�F�N�g�̐V�K�쐬
    th.start();            // �X���b�h�̊J�n
    //=====�E�B���h�E������悤�ɂ���=====
    addWindowListener(new WindowAdapter() {
    @Override
      public void windowClosing(WindowEvent e) {
        System.exit(0);
        }
      });
  }

  public void run() {  // (thread) 40 msec ���Ɏ��s����
    try {
      while(true) {
	repaint();        // �ĕ`���v������ repaint() �� update() ���Ăяo��
	Thread.sleep(17); // �E�B���h�E���X�V����O�ɋx�~ 25fps=1000msec/40msec
      }
    }
    catch(Exception e) {
    }
  }

  // �� ���\�b�h
  // paint() �� Frame �N���X�̃��\�b�h
  // ���g���㏑������i�I�[�o�[���C�h�j
  // paint() �� thread �ɂ�� 40msec ���� repaint() ����ČĂ΂��
  public void paint(Graphics gc) { //
    // �ȉ��ŁC����
    // 1. ��ʂ̃T�C�Y���擾
    // 2. buffer �C���[�W�̑��݃`�F�b�N
    // 3. buffer �p gc �̑��݃`�F�b�N
    // ���s���D����� buffer �������^�C�~���O�̖���
    // NullPointerException ��������̂�h���ׁB  

    if (buf == null)
      buf = createImage(bw, d.height); // buffer ����ʂƓ��T�C�Y�ō쐬
    if (buf_gc == null)
      buf_gc = buf.getGraphics();

    // buf �i���̉�p���j��`�悷��
    buf_gc.setColor(Color.black);          // gc �̐F������
    buf_gc.fillRect(0,0,d.width,d.height); // gc ���g���Ďl�p��`��

    switch(mode){//�Q�[����ʃm���[�h
      case 0://�\���@�z�[�����
        buf_gc.setColor(Color.yellow);
        buf_gc.fillRect(0,0 ,w, h);
        buf_gc.drawImage(imgBack, 0, Y,w,h, this); 

        //�g�̂Ȃ�
        buf_gc.setColor(Color.black);
        buf_gc.fillRect(64+32,32+12, w-64*4+32, h/2-32*2);
        Graphics2D g2 = (Graphics2D)buf_gc;
        BasicStroke bs = new BasicStroke(20);
		    g2.setStroke(bs);
        //�g
        buf_gc.setColor(Color.red);
        buf_gc.drawRect(64+32,32+12, w-64*4+32, h/2-32*2);
        //���͂�\��
        map.sabotenn(buf_gc,64+32*6,h/2+64,64,"�~15 ���W�߂ăS�[�����悤");
        buf_gc.setColor(Color.yellow);
        font1.Fon(buf_gc , "���X�N���[���^�A�N�V�����Q�[��" ,  64,64*2,64*2 );
        buf_gc.setColor(Color.white);
        font1.Fon(buf_gc , "�T�{�e������" ,  96,w/2-96*4,64*4  );
        buf_gc.setColor(Color.black);
        font1.Fon(buf_gc , "---PUSH SPACE---" ,  32,w/2-32*4,h-64 );
        buf_gc.setColor(Color.black);
        font4.Fon(buf_gc , "�w�Дԍ�:" , 32,w-10*32,h-64  );
        buf_gc.setColor(Color.black);
        font5.Fon(buf_gc , "�쐬��:���c�s��" ,  32,w-9*32,h-64+32);

        gc.drawImage(buf, 0, 0, this); // �\�̉�p���ɗ��̉�p�� (buf) �̓��e��\��t����
        break;


      case 1://game ��

         //������
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
        
        
        //==offset�@�ړ�������ʂ̈ʒu
        int offsetX = d.width / 2 - (int)ch01.x;
        // �}�b�v�̒[�ł̓X�N���[�����Ȃ��悤�ɂ���
        offsetX = Math.min(offsetX, 0);
        offsetX = Math.max(offsetX, d.width - bw);

        //�w�i
        buf_gc.drawImage(imgBack, 0, Y,w,h, this); 

        //�R�C���Q�b�g
        if (map.coinf ||map.coinf2&&!map.double_coin){
          coin+=1;
        }
        if (map.double_coin){//�����ɃQ�b�g
          coin+=2;
        }
        //�A�C�e���Q�b�g
        if (map.blockID==99){//�����_���Ō��ʂ�ς���
          item=true;
        }
        //�}�b�v��`��
        map.mDraw(buf_gc , (int)offsetX,0);
        map.mUpdate();

        //�L�����N�^�[��`��
        ch01.cDraw(buf_gc,offsetX,0); // ���̉�p���� GC �� Cha01 �̃I�u�W�F�N�g�ɓn��
        //HP
        ch01.cHP(buf_gc ,ch01.hp);
        //�Q�b�g�����T�{�e��
        map.mCoin(buf_gc,coin);
        //�A�C�e������
        if(item)ch01.onestep=8; // 
        ch01.cUpdate(offsetX, map.cof, map.cof2,map.block_x,map.block_y); // �L�����N�^�̃p�����[�^�X�V
        //�Փ˔���
        map.colli(ch01.newX, ch01.y   ,offsetX,0);
        map.colli2(  ch01.x , ch01.newY,offsetX,0);

        //�G
        teki.tDraw(buf_gc,offsetX,0,true);
        teki.tUpdate();
        tekib.tDraw(buf_gc,offsetX,0,true);
        tekib.tUpdate();
        tekib2.tDraw(buf_gc,offsetX,0,true);
        tekib2.tUpdate();
        tekim.tDraw(buf_gc,offsetX,0,false);
        tekim.tUpdate();

        buf_gc.setColor(Color.black);

        //if(ch01.onFace==true) check+=1;//�Փˉ�
        //if(map.cof==true)map_colli +=1;
        //�~�b�V����
        //buf_gc.drawImage(imgmi, 16 ,32-4+16,640/3+64,320/4+32, this);  
        int Mute =teki.Muteki+ tekib.Muteki+tekib2.Muteki+tekim.Muteki; //���G��t�^���Ă��鎞��


        //=============-�󋵂�����ɕ\���i�Q�[���ɂ͊֌W�Ȃ��j=========================
        //�L�����̈ʒu���x�A�ڂ����u���b�N�̈ʒu�Aoffset�Ȃǂ����A���^�C���Ŋm�F����p
        buf_gc.setFont(f1);
        int i=0;
        if(ch01.onGround)i+=1;
        //�R�C���ɓ����������ǂ���
        int coin2=0;
        if(map.coinf2)coin2+=1;
        int coin1=0;
        if(map.coinf)coin1+=1;

        
        if(map.double_coin)coindouble+=1;
        
        double[] num ={(double)coindouble ,(double)map.cnt3 ,ch01.x ,ch01.y,offsetX,(double)Mute,ch01.vy ,i,map.block_x,map.block_y -64 ,map.blockID};
        String[] name =
        { "X���̏Փ�= ",
          "Y���̏Փ�= ",
          "�v���C���[��x���W ch01.x = ",
          "�v���C���[��x���W ch01.y = ",
          "offsetX = ",
          "ALL ���G����= ",
          "vy = ",
          "onGround = ",
          "�u���b�N�̍������WX = ",
          "�u���b�N�̍������WY = ",
          "�u���b�N��ID = "
        };
        String str[] = new String[num.length];

        for(int j=0 ;j< num.length ; j++){
          str[j] = String.valueOf(num[j]);
          //buf_gc.drawString(name[j]+str[j],64*3/2,64*3/2 +10*j);�@//�{�Ԃ͂��̕����R�����g�A�E�g����
        }
        //===============================================        
        
        //==�G����
        if (teki.hp==0||tekib.hp==0||tekim.hp==0||tekib2.hp==0){
          //���X�|�[�������G�̂��⑬�x��ύX�\
          if(Math.random()<0.4/60 &&teki.hp==0){//�����_������ �m��20% 
            teki.hp=1;             
          }
          if(Math.random()<60/60&&tekib.hp==0){//�����_������ �m��100%    
            tekib.hp=1;             
            tekib.x=64*49;
            tekib.vx=2;
          }
          if(Math.random()<60/60&&tekib2.hp==0){//�����_������ �m��100%   
            tekib2.hp=1;             
            tekib2.x=64*49;
            tekib2.vx=3;
          }
          if(Math.random()<60/60&&tekim.hp==00){//�����_������ �m��100%    
            tekim.hp=1;             
          }
        }
        //�L�����Ƃ̏Փ˔���
        teki.tcol(teki , ch01.x,ch01.y ,0 ,teki.Muteki);
        tekib.tcol(tekib , ch01.x,ch01.y ,0,tekib.Muteki);
        tekib2.tcol(tekib2 , ch01.x,ch01.y ,0,tekib2.Muteki);
        //tekim.s=tekim.s/2;
        tekim.tcol(tekim , ch01.x,ch01.y , tekim.s*3/4,tekim.Muteki);
      
        //HP�����肩���G���t�^���ꂽHP����
        if(ch01.hp>0 && Mute==1) {
          ch01.hp-=1;      
          //teki.tekicolli=tekib.tekicolli =tekim.tekicolli=false;
        }
        //�G�Ɠ���������Ԃ��G�t�F�N�g���ł�
        if(ch01.hp>0 && Mute>=1&&Mute <=60)buf_gc.drawImage(imgef1,(int)ch01.x+offsetX,(int)ch01.y,96,96, this); 
        //���S��������������玀�S����
        if(map.blockID==38 || ch01.y==640+64|| map.blockID==91||ch01.hp==0) { 
          dl=true;
        }
        //���S��0.5�b�͉�ʑJ�ڂ��Ȃ�
        if(dl==true){
          dcnt+=1;
          
          if(dcnt<30 &&dcnt >=0){
            //�S�[���ɒ�������
            if(map.blockID==38)buf_gc.drawImage(imgefy,(int)ch01.x+offsetX-dcnt*20,(int)ch01.y -dcnt*20,96+dcnt*40,96+dcnt*40, this); 
              
            if(map.blockID==91)ch01.x=64*23.25;//�}�O�}�ɗ�������
            //���̂ق�
            else if(map.blockID!=38) buf_gc.drawImage(imgef1,(int)ch01.x+offsetX,(int)ch01.y,96,96, this); 
          }
          else {
            if(map.blockID==38)mode=2;//�N���A��ʂ�
            else mode=-1;//�Q�[���I�[�o�[��ʂ�
          }
        }

       gc.drawImage(buf, 0, 0, this); // �\�̉�p���ɗ��̉�p�� (buf) �̓��e��\��t���� 

      break;

      case -1: //gameover
      buf_gc.fillRect(0,0 ,w, h);
      buf_gc.drawImage(imggo, 0,0,w,h, this); //�Q�[���I�[�o�[��ʂ�\��
      gcnt=true;
      gc.drawImage(buf, 0, 0, this); // �\�̉�p���ɗ��̉�p�� (buf) �̓��e��\��t����
      break;

      case 2: //gameclr
      buf_gc.fillRect(0,0 ,w, h);
      buf_gc.drawImage(imgclr, 0 ,0,w,h, this); //�N���A��ʂ�\��
      gcnt=true;
      gc.drawImage(buf, 0, 0, this); // �\�̉�p���ɗ��̉�p�� (buf) �̓��e��\��t����
      break;
    }
    //gc.drawImage(buf, 0, 0, this); // �\�̉�p���ɗ��̉�p�� (buf) �̓��e��\��t����
    
  }   

  // �� ���\�b�h
  // ��������Ȃ��ƃ_�u���o�b�t�@�����O���Ă������
  // �irepaint���\�b�h���Ă΂��ƁCupdate ���Ă΂�A��������w�i�F��
  //  �h��Ԃ���Ƃ��s���邽�߁Cupdata ���\�b�h���I�[�o�[���C�h���C
  // ���̂܂�paint ���ĂԂ悤�ɂ���K�v������j
  public void update(Graphics gc){
    paint(gc);
    // �m�F�p�@space���������ʑJ��
    if(spacef==true){
      mode+=1; // �t���O�𗧂Ă�
      if(mode>2)mode=0;
      spacef=false;
    }
  }

  public void keyTyped(KeyEvent ke) {}

  public void keyPressed(KeyEvent ke) {
    int cd = ke.getKeyCode();
    switch (cd) {
    case KeyEvent.VK_SPACE: // [�[]�L�[�������ꂽ��A
      
      break;  
    case KeyEvent.VK_LEFT:  // [��]�L�[�������ꂽ��
      ch01.lf = true;   // �t���O�𗧂Ă�
      //	    System.out.println(" [��] pressed");
      break;
    case KeyEvent.VK_RIGHT: // [��]�L�[�������ꂽ��
      ch01.rf = true;   // �t���O�𗧂Ă�
      //	    System.out.println(" [��] pressed");
      break;
    case KeyEvent.VK_UP: // [��]�L�[�������ꂽ��
      ch01.uf = true;   // �t���O�𗧂Ă�
      //ch01.ufcnt =true;//�������ς�h��
      //	    System.out.println(" [��] pressed");
      break;
    case KeyEvent.VK_DOWN: // [��]�L�[�������ꂽ��
      ch01.df = true;   // �t���O�𗧂Ă�
      //	    System.out.println(" [��] pressed");
      break;
    }
  }
  public void keyReleased(KeyEvent ke) {
    int cd = ke.getKeyCode();
    switch (cd) {
    case KeyEvent.VK_SPACE: // �X�y�[�X�L�[�������ꂽ��  
      spacef = true; // �t���O���~�낷 
      break;
      
    case KeyEvent.VK_LEFT:  // [��]�L�[�������ꂽ��
      ch01.lf = false;  // �t���O���~�낷
      //	    System.out.println(" [��] released");
      break;
    case KeyEvent.VK_RIGHT: // [��]�L�[�������ꂽ��
      ch01.rf = false;  // �t���O���~�낷
      //	    System.out.println(" [��] released");
      break;
    case KeyEvent.VK_UP: // [��]�L�[�������ꂽ��
      ch01.uf = false;  // �t���O���~�낷
      ch01.ufcnt = true; //�������ϖh�~�@�����ꂽ��ēx�W�����v�̓��͂��󂯕t����
      //	    System.out.println(" [��] released");
      break;
    case KeyEvent.VK_DOWN: // [��]�L�[�������ꂽ��
      ch01.df = false;  // �t���O���~�낷
      //	    System.out.println(" [��] released");
      break;
    }
  }
}
