import javax.swing.*;
import java.awt.event.*;

public class Graphical {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLayout(null);
        frame.setVisible(true);

        JButton button1 = new JButton("click me!");
        button1.setBounds(100, 100, 100, 100);
        frame.add(button1);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Hello world");
            }
        });

    }
}
