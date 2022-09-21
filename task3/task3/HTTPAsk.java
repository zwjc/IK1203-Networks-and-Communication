import java.net.*;
import tcpclient.TCPClient;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class HTTPAsk {

    public static void main(String[] args) throws IOException {
        int portport = Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(portport);

        while (true) {
            Socket clientSocket = serverSocket.accept();

            try {
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String hostname = null;
                int port = 0;
                boolean ask = false;
                boolean get = false;

                boolean shutdown = false;
                Integer timeout = null;
                Integer limit = null;

                byte[] bytesFromServer = new byte[0];
                String url = in.readLine();
                String[] string = url.split("[? &=/]");

                for (int i = 0; i < string.length; i++) {
                    switch(string[i]){

                        case "hostname":
                            hostname = string[i + 1];
                            break;
                        case "port":
                            port = Integer.parseInt(string[i + 1]);
                            break;

                        case "shutdown":
                            shutdown = Boolean.parseBoolean(string[i + 1]);
                            break;
                        case "timeout":
                            timeout = Integer.parseInt(string[i + 1]);
                            break;
                        case "limit":
                            limit = Integer.parseInt(string[i + 1]);
                            break;

                        case "string":
                            bytesFromServer = string[i + 1].getBytes("UTF-8");
                            break;
                        case "ask":
                            ask = true;
                            break;
                        case "GET":
                            get = true;
                            break;
                    }
                }

                if(ask){
                    try {
                        if ((!hostname.equals("") && !(port == 0)) && get) {
                            TCPClient tcpClient = new TCPClient(shutdown, timeout, limit);
                            byte[] inBytes = tcpClient.askServer(hostname, port, bytesFromServer);

                            String response = new String(inBytes, StandardCharsets.UTF_8);
                            String answer = "HTTP/1.1 200 OK\r\n\r\n" + response;

                            clientSocket.getOutputStream().write(answer.getBytes());
                            clientSocket.getOutputStream().flush();
                            clientSocket.getOutputStream().close();
                        } else {
                            String answer = "HTTP/1.1 400 BAD REQUEST\r\n\r\n";

                            clientSocket.getOutputStream().write(answer.getBytes());
                            clientSocket.getOutputStream().flush();
                            clientSocket.getOutputStream().close();
                        }
                    } catch (UnknownHostException unknownHostException) {
                        System.out.println(unknownHostException + " Unknown Host");
                        String answer = "HTTP/1.1 400 BAD REQUEST\r\n\r\n";

                        clientSocket.getOutputStream().write(answer.getBytes());
                        clientSocket.getOutputStream().flush();
                        clientSocket.getOutputStream().close();
                    }
                }
            } catch (Exception e) {
                String response = "HTTP/1.1 404 NOT FOUND\r\n\r\n";
                clientSocket.getOutputStream().write(response.getBytes());
                clientSocket.getOutputStream().flush();
                clientSocket.getOutputStream().close();
            }
        }
    }
}