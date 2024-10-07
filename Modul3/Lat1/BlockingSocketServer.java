import java.net.*;
import java.io.*;
// BlockingSocketServer.java
// Program Server
public class BlockingSocketServer {
    private static final int PORT = 8888;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);
            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

                    String clientMessage;
                    while ((clientMessage = reader.readLine()) != null) {
                        System.out.println("Received from client: " + clientMessage);
                        // Kirim pesan balasan ke client
                        writer.println("Hello from Server!");
                    }
                } catch (IOException e) {
                    System.out.println("Error handling client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
