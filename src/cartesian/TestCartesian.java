package cartesian;

import javax.swing.*;

/**
 * Created by aryan on 2019-01-08.
 */
public class TestCartesian {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                CartesianFrame frame = new CartesianFrame();
                frame.showUI();
            }
        });
    }
}
