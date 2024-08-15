package JavaClient;

import JavaClient.UI.ChatFrame;
import JavaClient.Client.Client;
import JavaClient.UI.NameInputFrame;

import javax.swing.*;

public class Comunicaciones {

    private Client client;

    public Comunicaciones(String host, int port) {
        this.client = new Client(host, port);
    }

    public void startChat() {
        try {

            SwingUtilities.invokeLater(() -> {
                NameInputFrame nameInputFrame = new NameInputFrame(client);
                nameInputFrame.setVisible(true);
            });


            while (client.getClientName() == null) {
                Thread.sleep(100);
            }

            if (client.connect()) {
                System.out.println("Connected to server");


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
