package ui;

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.ychstudio.PacMan;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Antonio Zaitoun on 08/12/2018.
 */
public class EditorApp extends JFrame {

    public EditorApp() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final Container container = getContentPane();
        container.setLayout(new BorderLayout());

        LwjglAWTCanvas canvas = new LwjglAWTCanvas(new PacMan());
        container.add(canvas.getCanvas(), BorderLayout.CENTER);

        pack();
        setVisible(true);
        setSize(800, 600);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new EditorApp();
            }
        });
    }
}