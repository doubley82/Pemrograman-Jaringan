import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class NonBlockingServer {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try {
            // Membuka selector
            Selector selector = Selector.open();
            // Membuka ServerSocketChannel
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(PORT));
            serverSocketChannel.configureBlocking(false); // Set ke non-blocking
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("Non-blocking server listening on port " + PORT);

            while (true) {
                // Menunggu event
                selector.select();
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();

                    if (key.isAcceptable()) {
                        // Menerima koneksi klien
                        SocketChannel clientChannel = serverSocketChannel.accept();
                        clientChannel.configureBlocking(false);
                        clientChannel.register(selector, SelectionKey.OP_READ);
                        System.out.println("Client connected: " + clientChannel.getRemoteAddress());
                    } else if (key.isReadable()) {
                        // Membaca data dari klien
                        SocketChannel clientChannel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(256);
                        int bytesRead = clientChannel.read(buffer);

                        if (bytesRead == -1) {
                            System.out.println("Client disconnected: " + clientChannel.getRemoteAddress());
                            clientChannel.close();
                        } else {
                            String message = new String(buffer.array()).trim();
                            System.out.println("Received: " + message);
                            buffer.flip();
                            clientChannel.write(buffer); // Mengirim kembali data ke klien
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
