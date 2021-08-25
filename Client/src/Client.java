package sample;
import java.io.*;
import java.net.Socket;

public class Client
{
    private Socket socket;
    private int port = 8080;
    private String serverAddress = "localhost";
    private PrintWriter toServer;
    private BufferedReader fromServer;


    public Client()
    {
        try {
            socket = new Socket(serverAddress, port);
            fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            toServer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String reader() throws IOException
    {
        return fromServer.readLine();
    }

    public void writer(String information)
    {
        toServer.println(information);
    }

}
