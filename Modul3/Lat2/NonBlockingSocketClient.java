import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;

public class NonBlockingSocketClient {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 8888;

    public static void main(String[] args) {
        try {
            SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(SERVER_IP, SERVER_PORT));
            socketChannel.configureBlocking(false);

            // Mengirim pesan ke server
            String message = "Hello from Client!";
            socketChannel.write(ByteBuffer.wrap(message.getBytes()));

            // Menerima pesan balasan dari server
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            Selector selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_READ);

            // Menunggu hingga data tersedia
            selector.select();
            if (selector.selectedKeys().size() > 0) {
                int bytesRead = socketChannel.read(buffer);
                if (bytesRead > 0) {
                    buffer.flip();
                    byte[] data = new byte[buffer.remaining()];
                    buffer.get(data);
                    String serverMessage = new String(data);
                    System.out.println("Received from server: " + serverMessage);
                }
            }

            // Menutup socket
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
