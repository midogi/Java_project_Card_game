import java.io.*;
import java.net.*;

public class BlackjackServer implements Runnable {
    private int port;

    public BlackjackServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("BlackjackServer started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("BlackjackClient connected!");

                // 클라이언트와 간단한 통신
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                String message = in.readLine();
                System.out.println("Received from BlackjackClient: " + message);
                out.println("Welcome to BlackjackServer!");
            }
        } catch (IOException e) {
            System.err.println("BlackjackServer error: " + e.getMessage());
        }
    }
}
