package tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient
{
    TCPClient(boolean shutdown, Integer timeout, Integer limit)
{
    this.shutdown = shutdown;
    this.timeout = timeout;
    this.limit = limit;
    if (shutdown = 1 || timeout = maxTime || maxLimit < limit)
}
    private boolean shutdown;
    private Integer timeout;
    private Integer limit;
    private Integer sizeOfData;

    public static int BUFFERSIZE = 1024;

    




    public byte[] askServer(String hostname, int port, byte[] bytesToServer) throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] fromServerBuffer = new byte[BUFFERSIZE];


        Socket clientSocket = new Socket(hostname, port);
        clientSocket.

        int fromUserLength = bytesToServer.length;
        clientSocket.getOutputStream().write(bytesToServer, 0, fromUserLength);

        int fromServerLength = 0;
        int length;
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
