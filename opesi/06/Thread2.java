class Thread2 {
 
    // main �X���b�h
    public static void main(String args[]) {
 
        // ���L�ϐ����Ǘ����� Monitor �I�u�W�F�N�g�𐶐�
        Monitor monitor = new Monitor();
 
        // �X���b�h�I�u�W�F�N�g���Q����
        MyThread a = new MyThread("(A)", monitor);
        MyThread b = new MyThread("(B)", monitor);
 
        // 2�̃X���b�h�̎��s���J�n����
        a.start();
        b.start();
 
        // 2�̃X���b�h�̏I����҂�
        try {
            a.join();
            b.join();
        } catch (InterruptedException e) {
            System.out.println(e);
        }
 
        // �X���b�h�Ԃŋ��L���ꂽ�ϐ��̒l��\������
        monitor.show();
    }
}
 
 
// �X���b�h�Ԃŋ��L�����ϐ��Ƃ���𑀍삷�郁�\�b�h�����N���X
class Monitor {
    int myglobal = 0;       // �X���b�h�Ԃ̋��L�ϐ�
 
    // myglobal �̒l��1���₷���\�b�h
    public synchronized void increment() {
        int temp = myglobal;  // myglobal �̒l���擾
        temp = temp + 1;
     
        try {
            // �����_���� 0�`50ms �X���[�v����
            Thread.sleep((int)(Math.random() * 50));
        } catch (InterruptedException e) {
            System.out.println(e);
        }
 
        myglobal = temp;      // myglobal �̒l��1���₷
    }
 
    // myglobal �̒l��\�����郁�\�b�h
    public void show() {
        System.out.println("\n myglobal = " + myglobal);
    }
}
 
 
// ���s���Ď��s�����X���b�h�̃N���X
class MyThread extends Thread {
    String id;         // �X���b�h��ID�ƂȂ镶����
    Monitor monitor;   // ���j�^�ƂȂ�I�u�W�F�N�g�ւ̎Q��
 
    // �R���X�g���N�^�iID������ƃ��j�^��ݒ�j
    public MyThread(String str, Monitor mon) {
        monitor = mon;
        id = str;
    }
 
    // �X���b�h�J�n���ɍŏ��Ɏ��s����郁�\�b�h
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.print(id);  // ID�������\��
            monitor.increment();   // ���j�^���̕ϐ� myglobal ��1���₷
 
            try {
                // �����_���� 0�`100ms �X���[�v����
                Thread.sleep((int)(Math.random() * 100));
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }
}