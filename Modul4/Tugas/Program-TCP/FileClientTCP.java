import java.io.*;
import java.net.*;
import java.util.Scanner;

public class FileClientTCP {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
             Scanner scanner = new Scanner(System.in)) {

            System.out.print("Masukkan path file yang ingin dikirim: ");
            String filePath = scanner.nextLine();

            File file = new File(filePath);
            if (!file.exists() || file.isDirectory()) {
                System.out.println("File tidak ditemukan. Silakan coba lagi.");
                return;
            }

            // Mengirim nama file
            dataOutputStream.writeUTF(file.getName());

            // Menerima konfirmasi dari server sebelum mengirim data
            InputStream inputStream = socket.getInputStream();
            byte[] responseBuffer = new byte[256];
            int bytesRead = inputStream.read(responseBuffer);
            String response = new String(responseBuffer, 0, bytesRead);
            System.out.println("Server: " + response);

            // Mengirim file
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                byte[] buffer = new byte[4096];
                int fileBytesRead;
                while ((fileBytesRead = fileInputStream.read(buffer)) != -1) {
                    dataOutputStream.write(buffer, 0, fileBytesRead);
                }
            }
            System.out.println("File berhasil dikirim: " + file.getName());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
