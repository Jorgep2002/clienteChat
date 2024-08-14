package lenin;

import lenin.Client.Client;
import lenin.Client.NameInputFrame;
import lenin.Client.ChatFrame;

import javax.swing.*;
import java.io.IOException;

public class Comunicaciones {

    private Client client;

    public Comunicaciones(String host, int port) {
        this.client = new Client(host, port);
    }

    public void startChat() {
        try {
            // Mostrar la ventana para ingresar el nombre
            SwingUtilities.invokeLater(() -> {
                NameInputFrame nameInputFrame = new NameInputFrame(client);
                nameInputFrame.setVisible(true);
            });

            // Esperar a que se envíe el nombre antes de continuar
            while (client.getClientName() == null) {
                Thread.sleep(100); // Breve pausa para evitar un bucle intenso
            }

            if (client.connect()) {
                System.out.println("Connected to server");

                // Mostrar la ventana de chat
                SwingUtilities.invokeLater(() -> {
                    ChatFrame chatFrame = new ChatFrame(client);
                    chatFrame.setVisible(true);
                });

            } else {
                System.out.println("Failed to connect to server");
            }
        } catch (Exception e) {
            e.printStackTrace();
            closeConnection();
        }
    }

    private void closeConnection() {
        try {
            if (client != null) {
                client.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
