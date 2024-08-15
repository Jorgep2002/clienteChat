package JavaClient.Client;
import JavaClient.network.Session;

import java.io.IOException;
import java.io.EOFException;

import java.net.Socket;

public class Client implements SocketProcess {
    private Socket socket;
    private Session session;
    private String serverAddress;
    private int serverPort;
    private boolean running;
    private String clientName;

    public Client(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.session = null;
        this.running = false;
        this.clientName = null;
    }

    @Override
    public boolean connect() {
        try {
            this.socket = new Socket(this.serverAddress, this.serverPort);
            this.session = new Session(this.socket);
            this.running = true;


            while (clientName == null) {
                Thread.sleep(100);
            }

            session.write(clientName);

            return true;
        } catch (IOException | InterruptedException e) {
            System.out.println("Failed to connect");
            e.printStackTrace();
            return false;
        }
    }

    public void setClientName(String name) {
        this.clientName = name;
    }

    public String getClientName() {
        return clientName;
    }

    public boolean send(String data) throws IOException {
        if (session != null) {
            return session.write(data);
        }
        return false;
    }

    public Object receive() throws IOException {
        if (session != null) {
            try {
                return session.read();
            } catch (EOFException e) {

                System.out.println("Connection closed by the server.");
                close();
            }
        }
        return null;
    }

    @Override
    public boolean close()  throws IOException {
        try{

        boolean successful = this.session.close();
        this.session = null;
        this.running = false;
        return successful;
        }catch (IOException e){
            e.printStackTrace();
            throw e;
        }
    }
}
