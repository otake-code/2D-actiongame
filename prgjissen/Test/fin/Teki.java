import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageWriteParam;

class Teki extends Frame {
    // �� �t�B�[���h�ϐ�
    // s: �L�����N�^�̃T�C�Y �i�摜�̑傫���i�s�N�Z���j�j
    // cond1: �L�����̉E�����O (-1) ���������O (1) ��
    // cond2: �L�����̌����i�S��ށj
    // cw, cwMax:  ���̓���ւ����x
    //�@hp:�G��HP
    //�@Muteki:���G����
    int s=64,  y=320*2-128, vy=1, w=640*2,cond1=1, cond2=2, cw=0, cwMax=5,hp=0,Muteki=0;
    int speed =2;
    double  x=640*2,vx=1,tmp =0,cnt=0,time=60;//�G�̈ʒu�@, �����@, 0����60�����[�v�@, �������ԁ@,1�b�Ԃ�60�t���[��
    boolean lf, rf, uf, df , tekicolli; // left, right, up, down ,�Փ˔���
    Image t1img,t2img ,t3img, t4img; // �G�̓����̉摜
    Image Images[]=new Image[4];
    //�����ʒu�@
    // �� ���\�b�h
    // �摜���t�@�C������ cimg �ɓǂݍ���
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
  
    // �� ���\�b�h
    // �摜��؂���A�`�悷��
    void tDraw (Graphics g,int offsetX,int offsetY ,boolean f){
      //int imgx, imgy;
      if(hp==1){//HP������Ȃ�
        cnt +=1;//�@���Ԃ��o�� 1/60sec
        tmp =cnt%time;//cnt % 60 tmp�͂O����U�O
        //4��ނ̉摜�����Ԃɕ\��
        if( (0<=tmp && tmp<=(15) ) )//1/3sec �Ō��
          g.drawImage(t1img, (int)x+offsetX, y,s, s, this);
        if(((15)<tmp&&tmp<=(30)))//1/3sec �Ō��
          g.drawImage(t2img, (int)x+offsetX, y,s, s, this);  
        if((30)<tmp&&tmp<=(45))//1/3sec �Ō��
          g.drawImage(t3img,(int)x+offsetX, y,s, s, this);
        if((tmp> (45) &&tmp<=(60)))//1/3sec �Ō��
          g.drawImage(t4img,(int)x+offsetX, y,s, s, this);  
        if(f)x=x-vx;//�G�̈ړ�
        
        if(!f){//�S�[���O�̓G�͏㉺�Ɉړ�
          if(vy>0)y+=1*speed;
          else y-=1*speed;
          if(y>64*8) { //��ʂ�����������Ȃ�ŋt�����Ɉړ�
            y=64*8;
            vy=-1;
          }
          if(y<64*2) {
            y=64*2;
             vy=1;
          }
        }  
        if(x<48){ //�X�e�[�W�̍��[�ɒ������玀�S
          x=w;
          cnt =0;
          hp=0;
        }  
      }
    }
    
    // �� ���\�b�h
    //�G�Ƃ̏Փ�
    void tcol(Teki teki , double plx ,double ply,double w,int Mute ){
      //60�b���G�@�L�����N�^�ƓG�̉摜�����Ԃ�����Փ�
      if(Muteki <=60 &&Muteki >=0 &&hp==1 && Math.abs(plx-x) <= (s-w) && Math.abs(ply-y) <= (s-w) ){
        tekicolli=true;//�Ԃ������疳�G��t�^
        Muteki+=1;//���G���Ԃ̌v��
      }
      else  Muteki=0;//60�b�𒴂����疳�G���Ȃ��Ȃ�
    }
    
     // �� ���\�b�h
     //����͎g�p���Ă��Ȃ�
    void tUpdate (){
    }
  }
  