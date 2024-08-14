package lenin.Client;

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
    private JButton closeButton; // Nuevo botón para cerrar la conexión
    private Client client;

    public ChatFrame(Client client) {
        this.client = client;

        // Configuración de la ventana
        setTitle("Chat");
        setSize(600, 400); // Aumentar el tamaño para acomodar la lista de usuarios
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel principal para el chat
        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new GridLayout(1, 2)); // Dos columnas: mensajes y lista de usuarios

        // Área de texto para mensajes
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        chatPanel.add(new JScrollPane(messageArea)); // Agregar el área de mensajes al panel

        // Área de texto para la lista de usuarios conectados
        userListArea = new JTextArea();
        userListArea.setEditable(false);
        chatPanel.add(new JScrollPane(userListArea)); // Agregar el área de usuarios al panel

        add(chatPanel, BorderLayout.CENTER);

        // Panel para el campo de texto y el botón de envío
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        messageField = new JTextField();
        inputPanel.add(messageField, BorderLayout.CENTER);

        sendButton = new JButton("Send");
        inputPanel.add(sendButton, BorderLayout.EAST);

        // Nuevo panel para los botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        closeButton = new JButton("Close Connection");
        buttonPanel.add(closeButton); // Agregar el botón de cerrar conexión al panel

        add(buttonPanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.SOUTH);

        // Acción del botón de envío
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // Acción de Enter en el campo de texto
        messageField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // Acción del botón de cerrar conexión
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeConnection();
            }
        });

        // Iniciar un hilo para recibir mensajes
        new Thread(this::receiveMessages).start();
    }

    private void sendMessage() {
        String message = messageField.getText();
        if (message != null && !message.trim().isEmpty()) {
            try {
                client.send(message);
                messageField.setText(""); // Limpiar campo de texto
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
                        // Actualizar la lista de usuarios conectados
                        String clientList = msg.substring("CLIENT_LIST_UPDATE:".length());

                        // Dividir la lista de usuarios en líneas separadas
                        String[] users = clientList.replaceAll("[\\[\\]]", "").split(", ");

                        // Construir el string con cada usuario en una línea
                        StringBuilder userListText = new StringBuilder();
                        for (String user : users) {
                            userListText.append(user).append("\n");
                        }

                        SwingUtilities.invokeLater(() -> {
                            userListArea.setText(userListText.toString()); // Actualizar área de usuarios
                        });
                    } else {
                        // Actualizar el área de mensajes
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

    private void closeConnection() {

            if (client != null) {
                client.close(); // Cerrar la conexión del cliente
            }
            System.exit(0); // Salir de la aplicación

    }
}
