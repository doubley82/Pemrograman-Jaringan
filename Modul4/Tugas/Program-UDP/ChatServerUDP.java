import java.net.*;
import java.io.*; // Untuk menangani IOException
import java.util.*;

public class ChatServerUDP {
    private static final int SERVER_PORT = 8000;
    private static Map<String, InetAddress> clientAddresses = new HashMap<>();  // Menyimpan alamat klien
    private static Map<String, Integer> clientPorts = new HashMap<>();  // Menyimpan port klien
    private static List<String> clientNames = new ArrayList<>();  // Menyimpan nama klien

    public static void main(String[] args) throws IOException {
        DatagramSocket serverSocket = new DatagramSocket(SERVER_PORT);
        byte[] buffer = new byte[1024];

        System.out.println("Server UDP siap menerima pesan...");

        while (true) {
            DatagramPacket paketMasuk = new DatagramPacket(buffer, buffer.length);
            serverSocket.receive(paketMasuk);

            InetAddress alamatKlien = paketMasuk.getAddress();
            int portKlien = paketMasuk.getPort();
            String pesan = new String(paketMasuk.getData(), 0, paketMasuk.getLength());

            // Jika pesan adalah nama klien yang baru terhubung
            if (pesan.startsWith("NAMA:")) {
                String namaKlien = pesan.substring(5).trim();
                
                if (!clientNames.contains(namaKlien)) {
                    clientNames.add(namaKlien);
                    clientAddresses.put(namaKlien, alamatKlien);
                    clientPorts.put(namaKlien, portKlien);
                    System.out.println(namaKlien + " telah terhubung.");
                }
            } else {
                // Temukan nama klien berdasarkan alamat dan port
                String clientName = getClientName(alamatKlien, portKlien);

                if (clientName != null) {
                    System.out.println(clientName + ": " + pesan);

                    // Sebarkan pesan ke semua klien yang terhubung
                    for (String name : clientNames) {
                        InetAddress alamatPenerima = clientAddresses.get(name);
                        int portPenerima = clientPorts.get(name);

                        if (!(alamatPenerima.equals(alamatKlien) && portPenerima == portKlien)) {
                            String pesanUntukDikirim = clientName + ": " + pesan;
                            byte[] bufferKeluar = pesanUntukDikirim.getBytes();
                            DatagramPacket paketKeluar = new DatagramPacket(bufferKeluar, bufferKeluar.length, alamatPenerima, portPenerima);
                            serverSocket.send(paketKeluar);
                        }
                    }
                }
            }
        }
    }

    // Metode untuk mendapatkan nama klien berdasarkan alamat dan port
    private static String getClientName(InetAddress address, int port) {
        for (String name : clientNames) {
            if (clientAddresses.get(name).equals(address) && clientPorts.get(name) == port) {
                return name;
            }
        }
        return null;
    }
}
