package JavaClient.Client;

import java.io.IOException;

public interface SocketProcess {
  boolean connect() throws IOException;
  boolean send(String data) throws IOException;
  Object receive() throws IOException;
  boolean close() throws IOException;
}
