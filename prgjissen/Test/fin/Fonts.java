import java.awt.*;
import java.awt.event.*;
import java.util.function.ToDoubleBiFunction;

class Fonts extends Frame {
    // ■ メソッド
    //文字の大きさを変更し、文章を表示
    void Fon(Graphics  buf, String text , int size  ,int X,int Y ){
        Font f2 =new Font("Serif",Font.BOLD,size);
        String msg = text;
        //buf.setColor(Color.col);
        buf.setFont(f2);
        buf.drawString(msg, X, Y);
    }
    
}
