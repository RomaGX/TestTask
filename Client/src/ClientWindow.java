import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;

/**
 * Класс, реализующий интерфейс пользователя
 * @author Roman Gorbunov
 */
class ClientWindow extends JFrame {
    private JTextField inField;
    private JTextArea outField;
    private String lastCommand;
    private Connector connector;

    /**
     * @param title - заголовок окна
     */
    ClientWindow(String title){
        connector = new Connector(9856, "127.0.0.1");

        this.setTitle(title);
        this.setSize(1000, 450);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}

            @Override
            public void windowClosing(WindowEvent e) {
                try{
                    connector.disconnect();
                    setVisible(false);
                    System.exit(0);
                } catch (Exception x){
                    x.printStackTrace();
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {}

            @Override
            public void windowIconified(WindowEvent e) {}

            @Override
            public void windowDeiconified(WindowEvent e) {}

            @Override
            public void windowActivated(WindowEvent e) {}

            @Override
            public void windowDeactivated(WindowEvent e) {}
        });

        Border border = BorderFactory.createEmptyBorder(10, 10, 10, 10);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBounds(0, 0, 900, 450);
        panel.setBorder(border);

        JPanel inPanel = new JPanel();
        inPanel.setLayout(new BoxLayout(inPanel, BoxLayout.X_AXIS));
        inPanel.setSize(450, 100);
        inPanel.setBorder(border);

        JLabel labelForInField = new JLabel("Введите ваш запрос:");
        labelForInField.setBorder(border);
        inPanel.add(labelForInField);

        inField = new JTextField();
        inField.setBorder(border);
        inField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lastCommand = inField.getText();
                inField.setText("");
                if(outField.getText().equals(""))
                    outField.setText(connector.sendRequest(lastCommand));
                else
                    outField.setText(outField.getText()+"\n\n"+connector.sendRequest(lastCommand));
            }
        });
        inField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == e.VK_UP && lastCommand != null)
                    inField.setText(lastCommand);
            }
        });
        inPanel.add(inField);

        panel.add(inPanel, BorderLayout.PAGE_END);

        outField = new JTextArea();
        outField.setBorder(border);
        outField.setEditable(false);
        JScrollPane outPane = new JScrollPane(outField);
        outPane.setBorder(border);
        panel.add(outPane, BorderLayout.CENTER);

        this.add(panel);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        ClientWindow bankDirectory = new ClientWindow("Справочник по банковским вкладам");
    }
}