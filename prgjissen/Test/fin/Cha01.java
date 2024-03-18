import java.awt.Point;
import java.awt.*;
import java.awt.event.*;
/** ���L�[�ő���\�ȃL�����N�^�̃N���X
    �E�L�����N�^�[�̉摜�ǂݍ���
    �E�L�����N�^�[�̕`��
    �E�L�����N�^�[�̈ʒu�ƃp�����[�^�̍X�V
    ���s��

    �L�����N�^�[�̃p�����[�^�F �̂̌����Ƒ��i���E�̂ǂ����O�ɏo���Ă��邩�j
 */
class Cha01 extends Frame {
  // �� �t�B�[���h�ϐ�
  // s: �L�����N�^�̃T�C�Y �i�摜�̑傫���i�s�N�Z���j�j
  // cond1: �L�����̉E�����O (-1) ���������O (1) ��
  // cond2: �L�����̌����i�S��ށj
  // cw, cwMax:  ���̓���ւ����x
  // cnt: �t���[����
  // onestep: ����̕��̒���
  //jumptime: jump���̎���
  //hp: �L������HP
  //GRAVITY:�d��
  int w=960*2 , h =320*2;//��ʂ̑傫��
  int s= 32, JumpSpeed=24,cond1=1, cond2=2, cw=0, cwMax=5*4,t=0,cnt=60,onestep=4 ,jumptime,hp;
  double GRAVITY =1.1, x=80, y=h-4*s,vx, vy=0 ,newX,newY;//x,y�͏����ʒu newX,newY�͎��ɓ����ʒu
  int y_start = h-4*s;
  boolean lf, rf, uf, df,onGround=true; // left, right, up, down�@//�u���b�N�ɏՓ˂�����onGround=true
  boolean ufcnt , onFace=false; //ufcnt: �{�^���������ςŃW�����v����̂�h�~�@onFace: �u���b�N�Ɩʂ��Ă���
  Image cimg; // �L�����N�^�摜
  Map map;
  Cha01(){
    map = new Map();
  }

  //�����ʒu�@

  // �� ���\�b�h
  // �摜���t�@�C������ cimg �ɓǂݍ���
  void cGetImage (GameMaster ac){
    cimg = getToolkit().getImage("img/chara01.gif");
  }

  // �� ���\�b�h
  // �摜��؂���A�`�悷��
  void cDraw (Graphics g,int offsetX, int offsetY){
    int imgx, imgy;
    if (cond1 == 1) imgx=0; // �摜�̐؂���ꏊ (x���W) ������
    else            imgx=s; // �摜�̐؂���ꏊ (x���W)
    imgy = cond2*s; // �摜�̐؂���ꏊ (y���W)
    g.drawImage(cimg, (int)x+offsetX,(int) y+ offsetY, (int)x+64+offsetX, (int)y+64 +offsetY, imgx, imgy, imgx+s, imgy+s, null);
    //	System.out.println("(x, y)=" + x + " " + y);
  }

  // �� ���\�b�h
  // �L�����N�^�̈ʒu�Ə�Ԃ��X�V����
  // x, y: ���W
  // vx, vy: ���x
  // cond1: �E�����O���������O��
  // cond2: 
  void cUpdate (int offsetX, boolean map_cof ,boolean map_cof2 ,int block_x,int block_y){
    //if(cnt<960-640)cnt=(int)x;
    if(!map_cof) onFace=false;//�Փ˂��Ȃ��Ȃ�ʂ��Ă��Ȃ� 
    else if(map_cof) onFace=true;
    if (lf && !rf){ // ���L�[�������C�E�L�[�������Ă��Ȃ��Ȃ�
      vx=-1;        // x �����̑��x�� -1
      cond2=3;      // �L�����͍�����
      if(x>48 && !onFace)cnt-=onestep; //�l����x�ɐi�ދ����@
    }
    else if (rf && !lf){ // �E�L�[�������C���L�[�������Ă��Ȃ��Ȃ�
      vx=1;         // x �����̑��x�� +1
      cond2=1;      // �L�����͉E����
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

    /* �����p
    //���{�^���Œn��ɖ߂�
    else if (df && !uf){
      vy=0;
      y=512;
    }*/
    //�X�e�[�W�O�ɂ͏o�Ȃ�
    if(x<48)x=48;
    if(x>(w-110))x=w-110;
  
    //=====�����蔻��
    //// x����
    // �ړ�����W�����߂�
    newX =x +vx*onestep; //���̂����W
    if(!map_cof ){
      x=newX;// �Փ˂���^�C�����Ȃ���Έړ�
    }
    if(map_cof &&!map_cof2  ){// �Փ˂���^�C��������ꍇ

      if (vx > 0) {// �E�ֈړ����Ȃ̂ŉE�̃u���b�N�ƏՓ�
        // �u���b�N�ɂ߂肱�� or ���Ԃ��Ȃ��悤�Ɉʒu����
        x=block_x-(64-16);//
      } else if (vx < 0) {// ���ֈړ����Ȃ̂ō��̃u���b�N�ƏՓ�
        // �ʒu����
        x=block_x+(64-16);
      }
      vx = 0;
    }
    if(!ufcnt){//�W�����v����jumptime��������
      jumptime+=1;
    }if(onGround) jumptime=0;

    //// y�����̓����蔻��
    vy+= GRAVITY;//1.1
    // �ړ�����W�����߂�
    newY = y + vy; //vy=1.1
    // �ړ�����W�ŏՓ˂���^�C���̈ʒu���擾
    // y���������l����̂�x���W�͕ω����Ȃ��Ɖ���
    if (!map_cof2 ) {// �Փ˂���^�C�����Ȃ���Έړ�
      y = newY;
      onGround = false;// �Փ˂��ĂȂ��Ƃ������Ƃ͋�
    } if(map_cof2 ){// �Փ˂���^�C��������ꍇ

      if (vy > 0 ) {// ���ֈړ����Ȃ̂ŉ��̃u���b�N�ƏՓˁi���n�j
        // �ʒu����
        y=block_y-64;
        vy = 0;// ���n�����̂�y�������x��0��
        onGround = true; // ���n

      } else if (vy < 0 &&jumptime>1) {// ��ֈړ����Ȃ̂ŏ�̃u���b�N�ƏՓ�
        // �ʒu����
        y=block_y+64;
        vy = -1.101;// �V��ɂԂ������̂�y�������x��0��
        onGround = false;

      }else if(vy<0 && jumptime <=1){//Jump���悤�Ƃ��Ă���Ƃ�
        y=newY;
        onGround = false;
      }
    }
    if(map_cof&&map_cof2){//�΂߂���
      if(vx>0&&block_x-(64-20)==newX){//������
        x=block_x-(64-16);//�����߂�
        vx = 0;
        if(vy>0){//�ォ��Ȃ�
          y=block_y-64;
          vy = 0;
          // ���n
          onGround = true;
        }
        if(vy<0){//������Ȃ�
          y=newY;
          onGround = false;
        }
      }if(vx<0&&block_x+(64-20)==newX){//�E����
        x=block_x+(64-16);
        vx = 0;
        if(vy>0){//�ォ��Ȃ�
          y=block_y-64;
          vy = 0;
          // ���n
          onGround = true;
        }
        if(vy<0){
          y=newY;//������Ȃ�
          onGround = false;
        } 
      }
    }


    //����������n���Œ�~
    if(y>320*2){
      y=640+64;
      vy=0;
    }
 
    if(lf || rf ||uf){//���쒆
      if (cw < cwMax*3/4) // ���Ԃ��o���Ă��Ȃ��Ȃ� 1/3sec cmax=20
        cw++;   // ���̓��ւɊւ���J�E���g���P���₷
      else {
        cond1 = cond1 * (-1); // ���Ԃ��o�ĂΑ������E���ւ���
        cw = 0; // �J�E���g�����Z�b�g
      }
    }
    if(!lf &&!rf&&!uf){//���~��
      if (cw < 25) // ���Ԃ��o���Ă��Ȃ��Ȃ�
        cw++;   // ���̓��ւɊւ���J�E���g���P���₷
      else {
        cond1 = cond1 * (-1); // ���Ԃ��o�ĂΑ������E���ւ���
        cw = 0; // �J�E���g�����Z�b�g
      }
    }
    //	System.out.print("flags: " + lf + " " + rf + " " + uf + " " + df + "   ");
  }

  // �� ���\�b�h
  //�L������HP��\������
  void cHP(Graphics buf_gc, int h ){
    //�w�i�̔�
    buf_gc.setColor(Color.BLACK);
    buf_gc.fillRect(( 640*2 -32*11/2)-64-64 , (64+32 -40), 64+64-12,64-6 );

    int imgx=0, imgy=2*s;
    //�L����
    buf_gc.drawImage(cimg,( 640*2 -32*11/2 -64*2), (64+32 -40) +6, (640*2 -32*11/2+40 -64*2) ,(64+40) +6, imgx, imgy, imgx+s, imgy+s, null);
  
    String msg ="�~ ";
    Font f2 =new Font("Serif",Font.BOLD,40);
    buf_gc.setColor(Color.red);
    buf_gc.setFont(f2);
    //HP�̂���
    buf_gc.drawString(msg+h, 640*2 -64*2 -64*2 , 64+32);
}

}

    
    