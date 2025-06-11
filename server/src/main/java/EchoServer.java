import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer {
    // 서버의 포트 번호를 지정합니다.
    private final static int PORT = 44928;
    // 미리 생성할 스레드 개수를 지정합니다.
    private final static int THREAD_POOL_SIZE = 10;

    private ServerSocket welcomeSocket;
    // 클라이언트 종료 후 서버가 종료되지 않도록 스레드를 만들어 관리합니다. `THREAD_POOL_SIZE`만큼 스레드를 생성하여 클라이언트에게 할당합니다.
    private final ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    public void start() {
        try {
            // 클라이언트의 TCP 연결 요청을 받아들이는 환영 소켓을 생성합니다.
            welcomeSocket = new ServerSocket(PORT);
            System.out.println("This server is ready to receive.");

            // 클라이언트의 요청을 대기합니다.
            while (true) {
                // 클라이언트 요청이 오면 연결을 승인하며 연결 소켓을 생성합니다.
                Socket connectionSocket = welcomeSocket.accept();

                // 연결 소켓 생성 이후 스레드 풀에서 스레드 하나를 가져와 클라이언트에게 할당합니다.
                // 스레드는 EchoApplication을 생성 후 메서드 run을 실행하고 종료합니다.
                threadPool.submit(() -> {
                    EchoApplication application = new EchoApplication();
                    application.run(connectionSocket);
                });
            }
        } catch (IOException e) {
            System.out.println("Failed to create a socket.");
        }

        System.out.println("This server has shut down.");
    }
}
