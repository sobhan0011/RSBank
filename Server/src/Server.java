package sample;
import java.net.Socket;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.*;

public class Server
{
    private ServerSocket serverSocket;
    private int serverPort = 8080;
    public static void main(String[] args) {
        new Server();
    }

    public Server()
    {
        try {
            serverSocket = new ServerSocket(serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(new AccountsManager(), 0, 30, TimeUnit.DAYS);

        while (true)
        {
            try {
                Socket client = serverSocket.accept();
                Thread thread = new Thread(new ClientManger(client));
                thread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
