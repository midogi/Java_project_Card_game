package GameServer;
import java.io.*;
import java.net.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestServer {

    private ServerSocket serverSocket;
    private final int port = 30000;
    private CopyOnWriteArrayList<Socket> clientSockets = new CopyOnWriteArrayList<>();
    private Map<String, Socket> gameMatches = new HashMap<>(); // 게임별로 대기 중인 클라이언트 추적

    public TestServer() throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("서버가 시작되었습니다. 클라이언트 대기 중...");
        while (true) {
            Socket socket = serverSocket.accept();
            clientSockets.add(socket);
            System.out.println("클라이언트 연결됨: " + socket.getInetAddress());

            Thread clientThread = new Thread(new Receiver(socket));
            clientThread.start();
        }
    }

    // 특정 클라이언트로 메시지 전송
    private void sendMsgToClient(String msg, Socket clientSocket) {
        try {
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 클라이언트 메시지 수신 및 매칭 처리
    class Receiver implements Runnable {
        private Socket clientSocket;
        private DataInputStream in;

        public Receiver(Socket socket) throws IOException {
            this.clientSocket = socket;
            this.in = new DataInputStream(socket.getInputStream());
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String msg = in.readUTF(); // 클라이언트로부터 메시지 수신
                    System.out.println("받은 메시지: " + msg);

                    if (msg.startsWith("매칭 요청: ")) {
                        String gameName = msg.substring(6).trim(); // 게임 이름 추출

                        synchronized (gameMatches) {
                            if (gameMatches.containsKey(gameName)) {
                                // 이미 대기 중인 클라이언트가 있는 경우 매칭 성공
                                Socket opponentSocket = gameMatches.remove(gameName); // 대기 중인 클라이언트 가져오기

                                if (opponentSocket != null) {
                                    // 두 클라이언트에게 게임 시작 메시지 전송
                                    sendMsgToClient("게임 시작", clientSocket);
                                    sendMsgToClient("게임 시작", opponentSocket);
                                    System.out.println("현재 대기 중인 클라이언트: " + gameMatches.keySet());
                                    System.out.println("현재 연결된 클라이언트 수: " + clientSockets.size());

 
                                }
                            } else {
                                // 대기열에 현재 클라이언트 추가
                                gameMatches.put(gameName, clientSocket);
                                sendMsgToClient("매칭 중입니다...", clientSocket);
                                System.out.println("대기열에 추가됨: " + gameName + " - " +
                                        clientSocket.getInetAddress());
                            }
                        }


                    } else {
                        // 일반 메시지 처리
                        for (Socket socket : clientSockets) {
                            if (!socket.equals(clientSocket)) {
                                sendMsgToClient(msg, socket);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("클라이언트 연결 해제: " + clientSocket.getInetAddress());
            } finally {
                try {
                    clientSockets.remove(clientSocket);
                    synchronized (gameMatches) {
                        // 대기열에서 제거
                        gameMatches.values().removeIf(socket -> socket.equals(clientSocket));
                    }
                    in.close();
                    clientSocket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            new TestServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}