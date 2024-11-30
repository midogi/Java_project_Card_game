import java.io.*;
import java.net.*;

public class BlackjackClient {
    private String serverAddress;
    private int serverPort;

    // 생성자: 서버 주소와 포트를 초기화
    public BlackjackClient(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    // BlackjackServer에 연결하고 통신하는 메서드
    public void connectAndPlay() {
        try (Socket socket = new Socket(serverAddress, serverPort);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // BlackjackServer로 메시지 전송
            out.println("Hello, BlackjackServer!");
            String response = in.readLine();
            System.out.println("Response from BlackjackServer: " + response);

        } catch (IOException e) {
            System.err.println("Unable to connect to BlackjackServer: " + e.getMessage());
        }
    }
}
