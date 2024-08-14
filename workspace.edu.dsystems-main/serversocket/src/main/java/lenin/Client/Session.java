package lenin.Client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Session {
  private ObjectOutputStream objectOutputStream;
  private ObjectInputStream objectInputStream;
  private Socket socket;

  public Session(Socket socket) {
    this.socket = socket;
    try {
      this.objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
      this.objectOutputStream.flush();
      this.objectInputStream = new ObjectInputStream(this.socket.getInputStream());
    } catch (IOException e) {
      e.printStackTrace();
      close();
    }
  }

  public Object read() throws IOException {
    try {
      return this.objectInputStream.readObject();
    } catch (EOFException e) {
      System.out.println("Connection closed by the client.");
      throw e; // Propaga la excepción para que sea manejada en el cliente
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      throw new IOException("Failed to read object", e);
    }
  }

  public boolean write(Object data) throws IOException {
    try {
      this.objectOutputStream.writeObject(data);
      this.objectOutputStream.flush();
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      throw e;
    }
  }

  public boolean close() {
    boolean success = true;
    try {
      if (this.objectOutputStream != null) {
        this.objectOutputStream.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
      success = false;
    }
    try {
      if (this.objectInputStream != null) {
        this.objectInputStream.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
      success = false;
    }
    try {
      if (this.socket != null && !this.socket.isClosed()) {
        this.socket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
      success = false;
    }
    return success;
  }
}
