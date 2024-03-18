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
    // �� �t�B�[���h�ϐ�
    int w=32*50,h=320;//�}�b�v�͂Q�{�Ɋg�債�Ă��邽�ߕ��ʂ̑傫���ōl����
    Image stageimg;
    int X=0,Y=0,s=32;//20X10
    int  MAPCHIP_WIDTH =32;					// �}�b�v�`�b�v�P�p�[�c�̉���
    int  MAPCHIP_HEIGHT =32;				// �}�b�v�`�b�v�P�p�[�c�̏c��
    int  MAP_SIZE_WIDTH = w / MAPCHIP_WIDTH	;	// MAP�̉���(�l�̓`�b�v��)
    int  MAP_SIZE_HEIGHT = h / MAPCHIP_HEIGHT;		// MAP�̏c��(�l�̓`�b�v��) �z��̒��g�̂����ƑΉ�������
    int width_chip_num=8; //���̃`�b�v��
    int height_chip_num=16;//�c�̃`�b�v��
    int position_x;//�`���̈ʒu
    int position_y;
    int cnt2,cnt3;//�Փˉ�
    boolean coinf=false,coinf2=false ,double_coin=false; //�T�{�e�����Q�b�g�������ǂ����@�����ɃQ�b�g�����ꍇdouble_coin��true�ƂȂ莯�ʁ{2�ƂȂ�
    boolean cof ,cof2;//�Փ˂������ǂ���
    int row=10;// ��
    int col=200;//�s��
    private int[][] map;
    int block_x,block_y,blockID;//�u���b�N�̈ʒu��ID
    double absPBx,absPBy;//�v���C���[�ƃu���b�N�Ƃ̋���

    // �� ���\�b�h
    //txt����X�e�[�W�ǂݍ���
    void load() {
           // �}�b�v
        try {
            // txt�t�@�C�����J��
            BufferedReader br = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream("img/map.txt")));  
            // txt�̐����ƈʒu�ɑΉ������}�b�v���쐬
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
    // �� ���\�b�h
    //�摜�̓ǂݍ��݁@�w�i
    void mGetImage (GameMaster ac){
        stageimg = getToolkit().getImage("img/map2.png");//�摜�̓ǂݍ���
    }

    // �� ���\�b�h
    //�}�b�v��`��
    void mDraw (Graphics buf_gc,int offsetX,int offsetY){
        for(int i=0;i<MAP_SIZE_HEIGHT;i++){
            for(int j=0;j<MAP_SIZE_WIDTH;j++ ){
                int chip_id = map[i][j]; //�u���b�N�̎�ނ��Ƃ�ID��ݒ�@
                if (chip_id != 0){// �����Ȓl�����`�F�b�N�@0�͂Ȃɂ��Ȃ�����
                
                position_x = 2*MAPCHIP_WIDTH * j;//�`���̈ʒu
                position_y = 2*MAPCHIP_HEIGHT * i;

                int chip_pos_x = (chip_id % width_chip_num)* MAPCHIP_WIDTH;//�}�b�v�`�b�v�̐؂���ʒu
                int chip_pos_y = (chip_id / width_chip_num) * MAPCHIP_HEIGHT;
                //�`��
                buf_gc.drawImage(
                    stageimg, position_x + offsetX, position_y + offsetY, position_x+64+ offsetX, position_y+64+ offsetY,
                    chip_pos_x, chip_pos_y, chip_pos_x+s, chip_pos_y+s, null);
                }
            }
        }  
    }

    // �� ���\�b�h
    //�������̃u���b�N�Փ�
    void colli(double newX ,double Y ,int offsetX,int offsetY){
        cnt2=0;
        coinf2=false;

        for(int i=0;i<MAP_SIZE_HEIGHT;i++){
            for(int j=0;j<MAP_SIZE_WIDTH;j++ ){
                int chip_id = map[i][j];
                
                position_x = 2*MAPCHIP_WIDTH * j  ;//�`���̈ʒu
                position_y = 2*MAPCHIP_HEIGHT * i;// 

                if (chip_id == 0||chip_id==99)continue;//�u���b�N���Ȃ���Ζ���
                //�u���b�N�ɂԂ����Ă��邩
                absPBx=newX -(double)position_x;
                absPBy=Y -(double)position_y;
                if(absPBx==0&&absPBy==0)continue; //�ڂ��Ă���͖̂���
            
                //������
                if( Math.abs(absPBx) < (64 -16)  && Math.abs(absPBy) <= (64-1)){//�Փ˂��Ă���Ȃ�@//�v���C���[�̕�48�@�摜�ɑΉ����ĉ����̓����蔻�����������
                    if(chip_id == 19 && Y == (double)position_y ){//�T�{�e���ɐG�ꂽ��
                        coinf2=true;//�T�{�e���Q�b�g
                        map[i][j]=0;//�T�{�e������
                    }if(chip_id==19 && Y != (double)position_y ){
                    }
                    else {
                        block_x=position_x;//������E�Ɍ������ĂԂ������̂ŉ����Ԃ�
                        cnt2+=1;//�Ԃ�������J�E���g
                    }
                    blockID =chip_id;//�ォ�瓥�񂾂甽��
                    if(chip_id==19&& Y == (double)position_y)coinf=true;else coinf =false;
                }   
            }
        }  
        if(cnt2>0)cof=true;//�Ԃ����Ă�����Փ�ON
        else {
            cof=false;
            cnt2=0;//�Ԃ������񐔂����Z�b�g
        }
    } 
    // �� ���\�b�h
    //�������̃u���b�N�Փ�
    void colli2(double x ,double newY ,int offsetX,int offsetY){
        //���ƕǂ͍l���Ȃ��ėǂ�����for�̈ꕔ�����炷
        cnt3=0;
        coinf=false;
        double_coin=false;

        for(int i=0;i<MAP_SIZE_HEIGHT;i++){//�n��
            for(int j=0;j<MAP_SIZE_WIDTH;j++ ){//��
                int chip_id = map[i][j];//�v�f�̐���
                
                position_x = 2*MAPCHIP_WIDTH * j  ;//�`���̈ʒu//MAPCHIP��32
                position_y = 2*MAPCHIP_HEIGHT * i;// offsetY =0

                if (chip_id == 0)continue;//�u���b�N���Ȃ���Ζ���
                //�u���b�N�ɂԂ����Ă��邩
                absPBx=x -(double)position_x;
                absPBy=newY -(double)position_y;
                if(absPBx==0&&absPBy==0)continue;

                //y����
                if( Math.abs(absPBx) <(64 -16) && Math.abs(absPBy) <= (64-1)){//�Փ˂��Ă���Ȃ�@�E�Ԋu�͂S�R
                    //if(absPBx<0)block_x=position_x;//������E
                    if(chip_id==19||chip_id==99){//�T�{�e���ɐG���Ə����� 
                        if(chip_id==19&& newY == (double)position_y)coinf=true;
                        map[i][j]=0;
                    }
                    else{
                        if(absPBy<0)block_y=position_y;//�����  OK
                        //if(absPBx>0)block_x=position_x;//�E���獶
                        if(absPBy>0)block_y=position_y;//������
                        cnt3+=1;//�Փˉ�
                    }
                    blockID =chip_id;//�ォ�瓥�񂾂甽��
                    if(chip_id==19)coinf=true;else coinf =false;
                }   
            }
        }  
        if(cnt3>0){
            //if(coinf) double_coin=true;
            //else
            cof2=true;//�Փ�ON
        }    
        else {
            cof2=false;
            cnt3=0;
        }
    } 

    // �� ���\�b�h
    //�Q�b�g�����T�{�e���Ɛ���\��
    void mCoin(Graphics buf_gc ,int coin){
        //�w�i�̔�
        buf_gc.setColor(Color.BLACK);
        buf_gc.fillRect(( 640*2 -32*11/2), (64+32 -40), 64+64,64-6 );

        int chip_id=19;//�T�{�e����ID��19
        int chip_pos_x = (chip_id % width_chip_num)* MAPCHIP_WIDTH;//�}�b�v�`�b�v�̐؂���ʒu
        int chip_pos_y = (chip_id / width_chip_num) * MAPCHIP_HEIGHT;
        //�T�{�e��
        buf_gc.drawImage(stageimg,( 640*2 -32*11/2), (64+32 -40) +6, (640*2 -32*11/2+40) ,(64+40) +6, chip_pos_x, chip_pos_y, chip_pos_x+s, chip_pos_y+s, null);
        String msg ="�~ ";
        Font f2 =new Font("Serif",Font.BOLD,40);
        buf_gc.setColor(Color.green);
        buf_gc.setFont(f2);
        //�T�{�e���̃Q�b�g������
        buf_gc.drawString(msg+coin, 640*2 -64*2 , 64+32);
      }

    // �� ���\�b�h
    //�T�{�e���̊G�ƕ���\��
     void sabotenn(Graphics buf_gc,int X ,int Y , int size ,String text){
        int chip_id=19;
        int chip_pos_x = (chip_id % width_chip_num)* MAPCHIP_WIDTH;//�}�b�v�`�b�v�̐؂���ʒu
        int chip_pos_y = (chip_id / width_chip_num) * MAPCHIP_HEIGHT;
        //�T�{�e��
        buf_gc.drawImage(stageimg,X, Y, X+size ,Y+size, chip_pos_x, chip_pos_y, chip_pos_x+s, chip_pos_y+s, null);
        String msg =text;
        Font f2 =new Font("Serif",Font.BOLD,size);
        buf_gc.setColor(Color.black);
        buf_gc.setFont(f2);
        //�T�{�e���̉��ɂ�����
        buf_gc.drawString(msg, X+size , Y+size-5);
    }     
    // �� ���\�b�h
    //�}�b�v�͕ς��Ȃ����ߍ���͎g�p���Ȃ�
    void mUpdate (){
    }
}

