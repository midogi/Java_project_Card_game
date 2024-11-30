import java.io.*;
import java.net.*;

public class TestClient {
    public static void main(String[] args) {
        String testServerAddress = "localhost"; // TestServer 주소
        int testServerPort = 30000; // TestServer 포트

        String blackjackServerAddress = "localhost"; // BlackjackServer 주소
        int blackjackServerPort = 30001; // BlackjackServer 포트

        try (Socket testServerSocket = new Socket(testServerAddress, testServerPort);
             BufferedReader testServerIn = new BufferedReader(new InputStreamReader(testServerSocket.getInputStream()));
             PrintWriter testServerOut = new PrintWriter(testServerSocket.getOutputStream(), true)) {

            // TestServer로 메시지 전송
            testServerOut.println("Hello, TestServer!");
            String testServerResponse = testServerIn.readLine();
            System.out.println("Response from TestServer: " + testServerResponse);

            // TestServer와의 통신 후 BlackjackClient 생성
            System.out.println("Connecting to BlackjackServer...");
            connectToBlackjackServer(blackjackServerAddress, blackjackServerPort);

        } catch (IOException e) {
            System.err.println("Unable to connect to TestServer: " + e.getMessage());
        }
    }

    // BlackjackServer에 연결하는 메서드
    private static void connectToBlackjackServer(String serverAddress, int serverPort) {
        try (Socket blackjackSocket = new Socket(serverAddress, serverPort);
             BufferedReader blackjackIn = new BufferedReader(new InputStreamReader(blackjackSocket.getInputStream()));
             PrintWriter blackjackOut = new PrintWriter(blackjackSocket.getOutputStream(), true)) {

            // BlackjackServer로 메시지 전송
            blackjackOut.println("Hello, BlackjackServer!");
            String blackjackResponse = blackjackIn.readLine();
            System.out.println("Response from BlackjackServer: " + blackjackResponse);

        } catch (IOException e) {
            System.err.println("Unable to connect to BlackjackServer: " + e.getMessage());
        }
    }
}
