package JavaClient;

public class Main {
    public static void main(String[] args) {
        // Inicializar Comunicaciones y comenzar el chat
        //192.168.1.66
        Comunicaciones comunicaciones = new Comunicaciones("localhost", 1802);
        comunicaciones.startChat();
    }
}
