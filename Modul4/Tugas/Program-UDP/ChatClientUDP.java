import java.net.*;
import java.io.*; // Untuk menangani IOException
import java.util.Scanner;

public class ChatClientUDP {
    private static final int SERVER_PORT = 8000;

    public static void main(String[] args) throws IOException {
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress alamatServer = InetAddress.getByName("localhost");
        Scanner scanner = new Scanner(System.in);

        // Memasukkan nama klien
        System.out.print("Masukkan nama Anda: ");
        String nama = scanner.nextLine();

        // Mengirim nama klien ke server
        String pesanNama = "NAMA:" + nama;
        byte[] bufferNama = pesanNama.getBytes();
        DatagramPacket paketNama = new DatagramPacket(bufferNama, bufferNama.length, alamatServer, SERVER_PORT);
        clientSocket.send(paketNama);

        // Thread untuk menerima pesan dari server
        Thread terimaPesan = new Thread(() -> {
            byte[] buffer = new byte[1024];
            while (true) {
                DatagramPacket paketMasuk = new DatagramPacket(buffer, buffer.length);
                try {
                    clientSocket.receive(paketMasuk);
                    String pesan = new String(paketMasuk.getData(), 0, paketMasuk.getLength());
                    System.out.println(pesan);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        terimaPesan.start();

        // Loop untuk mengirim pesan ke server
        while (true) {
            System.out.print("Masukkan pesan: ");
            String pesan = scanner.nextLine();
            byte[] buffer = pesan.getBytes();
            DatagramPacket paketKeluar = new DatagramPacket(buffer, buffer.length, alamatServer, SERVER_PORT);
            clientSocket.send(paketKeluar);
        }
    }
}
