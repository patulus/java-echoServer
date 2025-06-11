public class ClientMain {
    public static void main(String[] args) {
        EchoClient client = new EchoClient("localhost", 44928);
        client.start();
    }
}
