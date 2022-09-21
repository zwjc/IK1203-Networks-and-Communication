import java.net.*;
import tcpclient.TCPClient;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class HTTPAsk {

    public static HashMap <String, String> hashMap (String string) throws Exception {
        System.out.println(string);
        String[] check = string.split(" ");
        string = check[1];

        if (string.startsWith("/ask?")) {
            string = string.substring(5);
            check = string.split("&");

            HashMap<String, String> params = new HashMap <String, String>();

            for (String url : check) {
                String[] array = url.split("=");
                params.put(array[0], array[1]);
            }
            return params;
        }
        else {
            throw new Exception("ERROR");
        }
    }

    public static void main(String[] args) throws IOException {
        int portport = Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(portport);

        while (true) {
            Socket clientSocket = serverSocket.accept();

            try {
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String hostname = null;
                int port = 0;

                boolean shutdown = false;
                Integer timeout = null;
                Integer limit = null;

                byte[] bytesToServer = new byte[0];
                String url = in.readLine();
                HashMap <String, String> params = hashMap(url);

                if(params.containsKey("shutdown")){
                    shutdown = Boolean.parseBoolean(params.get("shutdown"));
                }
                if(params.containsKey("limit")){
                    limit = Integer.parseInt(params.get("limit"));
                }
                if(params.containsKey("timeout")){
                    timeout = Integer.parseInt(params.get("timeout"));
                }


                if (params.containsKey("hostname")) {
                    hostname = params.get("hostname");
                }
                if (params.containsKey("port")) {
                    port = Integer.parseInt(params.get("port"));
                }


                if (params.containsKey("string")) {
                    bytesToServer = params.get("string").getBytes();
                }

                try {
                    if (!hostname.equals("") && !(port == 0)) {
                        TCPClient tcpClient = new TCPClient(shutdown, timeout, limit);
                        byte[] inBytes = tcpClient.askServer(hostname, port, bytesToServer);

                        String response = new String(inBytes, StandardCharsets.UTF_8);
                        String answer = "HTTP/1.1 200 OK\r\n\r\n" + response;

                        clientSocket.getOutputStream().write(answer.getBytes());
                        clientSocket.getOutputStream().flush();
                        clientSocket.getOutputStream().close();

                    }

                    else {
                        String answer = "HTTP/1.1 400 BAD REQUEST\r\n\r\n";

                        clientSocket.getOutputStream().write(answer.getBytes());
                        clientSocket.getOutputStream().flush();
                        clientSocket.getOutputStream().close();
                    }
                }

                catch (UnknownHostException unknownHostException) {
                    System.out.println(unknownHostException);
                    String answer = "HTTP/1.1 400 BAD REQUEST\r\n\r\n";

                    clientSocket.getOutputStream().write(answer.getBytes());
                    clientSocket.getOutputStream().flush();
                    clientSocket.getOutputStream().close();
                }
            }

            catch (Exception e){
                String response = "HTTP/1.1 404 NOT FOUND\r\n\r\n";
                clientSocket.getOutputStream().write(response.getBytes());
                clientSocket.getOutputStream().flush();
                clientSocket.getOutputStream().close();
            }
        }
    }
}