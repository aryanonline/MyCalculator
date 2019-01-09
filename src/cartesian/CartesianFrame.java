package cartesian;

import javax.swing.*;

/**
 * Created by aryan on 2019-01-08.
 */
public class CartesianFrame extends JFrame {
    CartesianPanel panel;

    public CartesianFrame() {
        panel = new CartesianPanel();
        add(panel);
    }

    public void showUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Cartesian");
        setSize(700, 700);
        setVisible(true);
    }
}
