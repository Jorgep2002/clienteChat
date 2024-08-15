package JavaClient.UI;

import JavaClient.Client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NameInputFrame extends JFrame {
    private JTextField nameField;
    private JButton submitButton;
    private Client client;

    public NameInputFrame(Client client) {
        this.client = client;

        // Configuración de la ventana
        setTitle("Enter Your Name");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());


        nameField = new JTextField(20);
        add(new JLabel("Enter your name:"));
        add(nameField);


        submitButton = new JButton("Submit");
        add(submitButton);


        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String clientName = nameField.getText();
                if (clientName != null && !clientName.trim().isEmpty()) {
                    client.setClientName(clientName);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(NameInputFrame.this, "Name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
