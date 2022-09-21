import java.net.*;
import tcpclient.TCPClient;
import java.io.*;
import java.nio.charset.StandardCharsets;


public class ConcHTTPAsk {

    public static void main(String[] args) throws IOException {
        int portport = Integer.parseInt(args[0]);


        try{
            ServerSocket welcomeSocket = new ServerSocket(portport);

            while (true) {
                Socket client = welcomeSocket.accept();

                MyRunnable runnable = new MyRunnable(client);
                Thread thread = new Thread(runnable);
                thread.start();
            }
        }
        catch (IOException e){
            System.out.println("ERROR");
        }
    }
}