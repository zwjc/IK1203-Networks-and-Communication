package tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient
{
    private static boolean shutdown = false;
    private static Integer timeout = null;
    private static Integer limit = null;

    public TCPClient(boolean shutdown, Integer timeout, Integer limit)
    {
        this.shutdown = shutdown;
        this.timeout = timeout;
        this.limit = limit;

    }

    public static int BUFFERSIZE = 1024;

    public static byte[] askServer(String hostname, int port, byte[] bytesToServer) throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] fromServerBuffer = new byte[BUFFERSIZE];


        Socket clientSocket = new Socket(hostname, port);

        int fromServerLength = 0;
        try
        {
            if(timeout != null)
                clientSocket.setSoTimeout(timeout);

            int fromUserLength = bytesToServer.length;
            clientSocket.getOutputStream().write(bytesToServer, 0, fromUserLength);

            if(shutdown = true)
                clientSocket.shutdownOutput();

            int length;
            while ((length = clientSocket.getInputStream().read(fromServerBuffer)) != -1)
            {
                out.write(fromServerBuffer, 0, length);
                fromServerLength += length;
                if(limit != null && fromServerLength > limit)
                {
                    System.out.println("From Server:");
                    System.out.write(out.toByteArray(), 0, fromServerLength);
                    clientSocket.close();
                    return out.toByteArray();
                }
            }
        }
        catch (SocketException socketException)
        {
            System.out.println("There is an error!");
        }

        System.out.println("From Server:");
        System.out.write(out.toByteArray(), 0, fromServerLength);

        clientSocket.close();

        return out.toByteArray();
    }
}