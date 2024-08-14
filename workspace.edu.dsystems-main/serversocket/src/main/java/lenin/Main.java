package lenin;

public class Main {
    public static void main(String[] args) {
        // Inicializar Comunicaciones y comenzar el chat
        Comunicaciones comunicaciones = new Comunicaciones("localhost", 1802);
        comunicaciones.startChat();
    }
}
