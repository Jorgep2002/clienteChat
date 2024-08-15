package lenin.Client;

import java.io.IOException;

public interface SocketProcess {
  boolean connect() throws IOException;
  boolean send(String data) throws IOException;
  Object receive() throws IOException;  // Modificado para lanzar IOException
  boolean close() throws IOException;   // Asegúrate de que también pueda lanzar IOException
}
