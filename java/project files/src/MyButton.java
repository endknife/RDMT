import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyButton extends JButton {

    private boolean isPressed;
    Tema col = new Tema();


    /**
     * <h2>Custom button object</h2>
     * This class extends JButton and it adds the methods "isPressed()" and "setPressed()" handle better the state of the button.
     * @param str text of the button
     */
    MyButton(String str){
        this.isPressed = false;
        this.setForeground(col.buttonTxtColor);
        super.setText(str);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {

                if(isPressed) {
                    setBg(col.buttonPressedBgHover);
                }else{
                    setBg(col.buttonUnPressedBgHover);
                }
                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if(isPressed){
                    setBg(col.buttonPressedBg);
                }else{
                    setBg(col.buttonUnPressedBg);
                }
                super.mouseExited(e);
            }
        });
    }

    //wasted memory
    MyButton(){
        this.isPressed = false;
    }

    public boolean isPressed() {
        return isPressed;
    }

    public void setPressed(boolean pressed) {
        isPressed = pressed;
    }

    public void setBg(Color color){
        this.setBackground(color);
    }
}
