import java.net.*;
import java.io.*;
// BlockingSocketClient.java
// Program Client
public class BlockingSocketClient {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 8888;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT)) {
            System.out.println("Connected to server");

            // Mengirim pesan ke server
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println("Hello from Client!");

            // Menerima pesan balasan dari server
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String serverMessage = reader.readLine();
            System.out.println("Received from server: " + serverMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
