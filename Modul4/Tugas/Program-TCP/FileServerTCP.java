import java.io.*;
import java.net.*;

public class FileServerTCP {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server file TCP berjalan pada port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Klien terhubung: " + clientSocket.getInetAddress());

                // Menangani klien dalam thread terpisah
                new Thread(() -> {
                    try (InputStream inputStream = clientSocket.getInputStream();
                         DataInputStream dataInputStream = new DataInputStream(inputStream)) {

                        // Menerima nama file
                        String fileName = dataInputStream.readUTF();
                        System.out.println("Menerima file: " + fileName);

                        // Mengkonfirmasi penerimaan nama file sebelum membaca data
                        clientSocket.getOutputStream().write("Nama file diterima, mulai mengirim data.".getBytes());

                        // Menerima dan menyimpan file
                        try (FileOutputStream fileOutputStream = new FileOutputStream("file_diterima_" + System.currentTimeMillis() + ".txt")) {
                            byte[] buffer = new byte[4096];
                            int bytesRead;
                            while ((bytesRead = dataInputStream.read(buffer)) != -1) {
                                fileOutputStream.write(buffer, 0, bytesRead);
                            }
                            System.out.println("File berhasil diterima: " + fileName);
                        }

                    } catch (IOException e) {
                        System.err.println("Terjadi kesalahan saat menerima file: " + e.getMessage());
                    } finally {
                        try {
                            clientSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
