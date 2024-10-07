import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class NonBlockingClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(SERVER_ADDRESS, SERVER_PORT));
            socketChannel.configureBlocking(false);
            ByteBuffer buffer = ByteBuffer.allocate(256);
            Scanner scanner = new Scanner(System.in);

            System.out.println("Mulai Menulis Pesan dibawah ini (ketikan 'exit' untuk keluar):");
            while (true) {
                String userInput = scanner.nextLine();
                buffer.clear();
                buffer.put(userInput.getBytes(StandardCharsets.UTF_8));
                buffer.flip();
                socketChannel.write(buffer); // Mengirim pesan ke server

                if ("exit".equalsIgnoreCase(userInput)) {
                    break; // Keluar dari loop jika user mengetik 'exit'
                }

                // Membaca balasan dari server
                buffer.clear();
                int bytesRead = socketChannel.read(buffer);
                if (bytesRead > 0) {
                    buffer.flip();
                    System.out.println("Server replied: " + new String(buffer.array(), 0, bytesRead).trim());
                }
            }

            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}