package tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient
{
    private boolean shutdown = false;
    private Integer timeout = null;
    private Integer limit = null;

    TCPClient(boolean shutdown, Integer timeout, Integer limit)
{
    this.shutdown = shutdown;
    this.timeout = timeout;
    this.limit = limit;

}

    public static int BUFFERSIZE = 1024;

    public byte[] askServer(String hostname, int port, byte[] bytesToServer) throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] fromServerBuffer = new byte[BUFFERSIZE];


        Socket clientSocket = new Socket(hostname, port);

        try 
        {
            clientSocket.setSoTimeout(timeout);
        }
        catch (SocketException socketException)
        {
            System.out.println("There is an error!");
        }
    
        if(shutdown = true)
            clientSocket.shutdownOutput();

        int fromUserLength = bytesToServer.length;
        clientSocket.getOutputStream().write(bytesToServer, 0, fromUserLength);

        int fromServerLength = 0;
        int length;
        while ((length = clientSocket.getInputStream().read(fromServerBuffer)) != -1)
        {
            out.write(fromServerBuffer, 0, length);
            fromServerLength += length;
            if(fromServerLength > limit && limit != null)
            {
                System.out.println();
                System.out.write(out.toByteArray(), 0, fromServerLength);
                clientSocket.close();
                return out.toByteArray();
            }
        }

        System.out.println("From Server:");
        System.out.write(out.toByteArray(), 0, fromServerLength);

        clientSocket.close();

        return out.toByteArray();
    }
}
