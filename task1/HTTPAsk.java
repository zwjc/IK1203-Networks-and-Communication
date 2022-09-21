//Iley's nya
import java.net.*;
import java.io.*;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class HTTPAsk
{
    public static void main(String[] args) throws IOException
    {
        ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
        while(true)
        {
            Socket clientSocket = serverSocket.accept();
            try
            {
                boolean shutdown = false;               // True if client should shutdown connection
	            Integer timeout = null;			        // Max time to wait for data from server (null if no limit)
	            Integer limit = null;			        // Max no. of bytes to receive from server (null if no limit)
	            String hostname = null;			        // Domain name of server
	            int port = 0;					        // Server port number
	            byte[] userInputBytes = new byte[0];    // Data to send to server
                InputStreamReader iSReader = new InputStreamReader(clientSocket.getInputStream());
                BufferedReader bReader = new BufferedReader(iSReader);
                String line = bReader.readLine();
                Hashtable<String, String> d = stringFilter(line); 
                if(d.containsKey("hostname"))
                {
                    hostname = d.get("hostname");
                }
                if(d.containsKey("port"))
                {
                    port = Integer.parseInt(d.get("port"));
                }
                if(d.containsKey("string"))
                {
                    userInputBytes = d.get("string").getBytes();
                }
                if(d.containsKey("shutdown"))
                {
                    shutdown = Boolean.parseBoolean(d.get("shutdown"));
                }
                if(d.containsKey("limit"))
                {
                    limit = Integer.parseInt(d.get("limit"));
                }
                if(d.containsKey("timeout"))
                {
                    timeout = Integer.parseInt(d.get("timeout"));
                }
                if(hostname == null || port == 0)
                {
                    String httpResponse = "HTTP/1.1 400 BAD REQUEST\r\n\r\n";
                    clientSocket.getOutputStream().write(httpResponse.getBytes());
                    clientSocket.getOutputStream().flush();
                    clientSocket.getOutputStream().close();
                }
                else
                {
                    TCPClient tcpClient = new TCPClient(shutdown, timeout, limit);
			        byte[] serverBytes  = tcpClient.askServer(hostname, port, userInputBytes);
                    String string = new String(serverBytes, StandardCharsets.UTF_8); 
                    String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + string; 
                    clientSocket.getOutputStream().write(httpResponse.getBytes());
                    clientSocket.getOutputStream().flush();
                    clientSocket.getOutputStream().close();
                }
            }
            catch(UnknownHostException e)
            {
                String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + "Unknown host";
                clientSocket.getOutputStream().write(httpResponse.getBytes()); 
                clientSocket.getOutputStream().flush();
                clientSocket.getOutputStream().close();
            }
            catch(Exception e)
            {
                String httpResponse = "HTTP/1.1 404 NOT FOUND\r\n\r\n";
                clientSocket.getOutputStream().write(httpResponse.getBytes());
                clientSocket.getOutputStream().flush();
                clientSocket.getOutputStream().close();
            }
        }
    }

    public static Hashtable<String, String> stringFilter(String line) throws Exception
    {
        System.out.println(line);
        String[] arrOfStr = line.split(" ");
        line = arrOfStr[1];
        if(!line.startsWith("/ask?"))
        {
            throw new Exception();
        }
        line = line.substring(5);
        arrOfStr = line.split("&");
        Hashtable<String, String> d = new Hashtable<String, String>();
        for(String s : arrOfStr)
        {
            String[] array = s.split("=");
            d.put(array[0], array[1]);
        }
        return d;
    }
}

