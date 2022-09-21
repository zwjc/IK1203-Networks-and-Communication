package tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient
{
    public TCPClient()  {}
    public static int BUFFERSIZE = 1024;
    public byte[] askServer(String hostname, int port, byte[] bytesToServer) throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] fromServerBuffer = new byte[BUFFERSIZE];


        Socket clientSocket = new Socket(hostname, port);

        int fromUserLength = bytesToServer.length;
        clientSocket.getOutputStream().write(bytesToServer, 0, fromUserLength);

        int fromServerLength = 0;
        int length = 0;
        while ((length = clientSocket.getInputStream().read(fromServerBuffer)) != -1)
        {
            out.write(fromServerBuffer, 0, length);
            fromServerLength += length;
            System.out.println();
        }

        System.out.println("From Server:");
        System.out.write(out.toByteArray(), 0, fromServerLength);

        clientSocket.close();

        return out.toByteArray();
    }
}
