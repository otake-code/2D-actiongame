class Thread2 {
 
    // main スレッド
    public static void main(String args[]) {
 
        // 共有変数を管理する Monitor オブジェクトを生成
        Monitor monitor = new Monitor();
 
        // スレッドオブジェクトを２つ生成
        MyThread a = new MyThread("(A)", monitor);
        MyThread b = new MyThread("(B)", monitor);
 
        // 2つのスレッドの実行を開始する
        a.start();
        b.start();
 
        // 2つのスレッドの終了を待つ
        try {
            a.join();
            b.join();
        } catch (InterruptedException e) {
            System.out.println(e);
        }
 
        // スレッド間で共有された変数の値を表示する
        monitor.show();
    }
}
 
 
// スレッド間で共有される変数とそれを操作するメソッドを持つクラス
class Monitor {
    int myglobal = 0;       // スレッド間の共有変数
 
    // myglobal の値を1つ増やすメソッド
    public synchronized void increment() {
        int temp = myglobal;  // myglobal の値を取得
        temp = temp + 1;
     
        try {
            // ランダムに 0～50ms スリープする
            Thread.sleep((int)(Math.random() * 50));
        } catch (InterruptedException e) {
            System.out.println(e);
        }
 
        myglobal = temp;      // myglobal の値を1つ増やす
    }
 
    // myglobal の値を表示するメソッド
    public void show() {
        System.out.println("\n myglobal = " + myglobal);
    }
}
 
 
// 並行して実行されるスレッドのクラス
class MyThread extends Thread {
    String id;         // スレッドのIDとなる文字列
    Monitor monitor;   // モニタとなるオブジェクトへの参照
 
    // コンストラクタ（ID文字列とモニタを設定）
    public MyThread(String str, Monitor mon) {
        monitor = mon;
        id = str;
    }
 
    // スレッド開始時に最初に実行されるメソッド
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.print(id);  // ID文字列を表示
            monitor.increment();   // モニタ内の変数 myglobal を1つ増やす
 
            try {
                // ランダムに 0～100ms スリープする
                Thread.sleep((int)(Math.random() * 100));
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }
}