package JavaClient.UI;

import JavaClient.Client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ChatFrame extends JFrame {
    private JTextArea messageArea;
    private JTextArea userListArea;
    private JTextField messageField;
    private JButton sendButton;
    private JButton closeButton;
    private Client client;

    public ChatFrame(Client client) {
        this.client = client;


        setTitle("Chat");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());


        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new GridLayout(1, 2));


        messageArea = new JTextArea();
        messageArea.setEditable(false);
        chatPanel.add(new JScrollPane(messageArea));


        userListArea = new JTextArea();
        userListArea.setEditable(false);
        chatPanel.add(new JScrollPane(userListArea));

        add(chatPanel, BorderLayout.CENTER);


        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        messageField = new JTextField();
        inputPanel.add(messageField, BorderLayout.CENTER);

        sendButton = new JButton("Send");
        inputPanel.add(sendButton, BorderLayout.EAST);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        closeButton = new JButton("Close Connection");
        buttonPanel.add(closeButton);

        add(buttonPanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });


        messageField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });


        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    closeConnection();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });


        new Thread(this::receiveMessages).start();
    }

    private void sendMessage() {
        String message = messageField.getText();
        if (message != null && !message.trim().isEmpty()) {
            try {
                client.send(message);
                messageField.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void receiveMessages() {
        try {
            while (true) {
                Object message = client.receive();
                if (message != null) {
                    String msg = message.toString();
                    if (msg.startsWith("CLIENT_LIST_UPDATE:")) {

                        String clientList = msg.substring("CLIENT_LIST_UPDATE:".length());


                        String[] users = clientList.replaceAll("[\\[\\]]", "").split(", ");


                        StringBuilder userListText = new StringBuilder();
                        for (String user : users) {
                            userListText.append(user).append("\n");
                        }

                        SwingUtilities.invokeLater(() -> {
                            userListArea.setText(userListText.toString());
                        });
                    } else {

                        SwingUtilities.invokeLater(() -> {
                            messageArea.append(msg + "\n");
                        });
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() throws IOException {

            if (client != null) {
                client.close();
            }
            System.exit(0);

    }
}
