import java.io.*;
import java.net.*;

public class FileServerTCP {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server file TCP berjalan pada port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Klien terhubung");

                // Menangani klien dalam thread terpisah
                new Thread(() -> {
                    try (InputStream inputStream = clientSocket.getInputStream();
                         DataInputStream dataInputStream = new DataInputStream(inputStream)) {

                        // Menerima nama file
                        String fileName = dataInputStream.readUTF();
                        if (fileName.isEmpty()) {
                            System.err.println("Nama file tidak valid.");
                            return; // Menghentikan eksekusi jika nama file tidak valid
                        }

                        System.out.println("Menerima file: " + fileName);

                        // Menerima dan menyimpan file
                        try (FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
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
