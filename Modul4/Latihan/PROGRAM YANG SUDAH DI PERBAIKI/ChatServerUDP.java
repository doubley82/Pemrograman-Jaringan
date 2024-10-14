import java.net.*;

public class ChatServerUDP {
    private static final int PORT = 8000;

    public static void main(String[] args) {
        try (DatagramSocket serverSocket = new DatagramSocket(PORT)) {
            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Pesan diterima: " + message);

                // Mengirim balasan ke klien
                String responseMessage = "Pesan diterima: " + message;
                byte[] responseBuffer = responseMessage.getBytes();
                DatagramPacket responsePacket = new DatagramPacket(
                    responseBuffer, responseBuffer.length,
                    packet.getAddress(), packet.getPort()
                );
                serverSocket.send(responsePacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
