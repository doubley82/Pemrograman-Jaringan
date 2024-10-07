import java.io.*;
import java.net.*;

public class BlockingClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            System.out.println("Connected to the server on port " + SERVER_PORT);

            // Create input and output streams
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            String userInput;
            while (true) {
                System.out.print("Enter message: ");
                userInput = consoleReader.readLine();

                // Send message to server
                writer.println(userInput);

                // Read and print the response from server
                String response = reader.readLine();
                System.out.println("Server: " + response);

                // Break the loop if "exit" command is sent
                if ("exit".equalsIgnoreCase(userInput)) {
                    break;
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }
}
