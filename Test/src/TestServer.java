import java.io.*;
import java.net.*;

public class TestServer {
    public static void main(String[] args) {
        int testServerPort = 30000; // TestServer 포트
        int blackjackServerPort = 30001; // BlackjackServer 포트

        // BlackjackServer 생성 및 시작
        BlackjackServer blackjackServer = new BlackjackServer(blackjackServerPort);
        new Thread(blackjackServer).start(); // 별도의 스레드에서 실행

        try (ServerSocket serverSocket = new ServerSocket(testServerPort)) {
            System.out.println("TestServer started on port " + testServerPort);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("TestClient connected!");

                // 클라이언트와 간단한 통신
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                String message = in.readLine();
                System.out.println("Received from TestClient: " + message);
                out.println("Hello from TestServer!");
            }
        } catch (IOException e) {
            System.err.println("TestServer error: " + e.getMessage());
        }
    }
}
