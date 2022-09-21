import java.nio.charset.StandardCharsets;
import java.net.*;
import tcpclient.TCPClient;
import java.io.*;
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
            throw new Exception("ERROR beep boop");
        }
    }

    public static void main(String[] args) throws IOException {
        int portNumber = Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(portNumber);

        while (true) {
            Socket clientSocket = serverSocket.accept();

            try {
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String hostname = null;
                int port = 0;
                Boolean isString = false;

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
                    portNumber = Integer.parseInt(params.get("port"));
                }
                if (params.containsKey("string")) {
                    bytesToServer = params.get("string").getBytes();
                    isString = true;
                }

                if (!(hostname == null) && !(portNumber == 0)) {
                    try {
                        byte[] inBytes = TCPClient.askServer(hostname, portNumber, bytesToServer);
                        String response = new String(inBytes, StandardCharsets.UTF_8);
                        out.writeBytes("HTTP/1.1 200 OK \r\n\r\n" + response);
                    }
                    catch (Exception e) {
                        out.writeBytes("HTTP/1.1 404 NOT FOUND\r\n");
                    }
                }
                else if (hostname == null || port == 0) {
                    out.writeBytes("HTTP/1.1 400 BAD REQUEST\r\n");
                }
                clientSocket.close();
            }
            catch (Exception exception) {
                String out = "HTTP/1.1 404 NOT FOUND\r\n";
                clientSocket.getOutputStream().write(out.getBytes());
                clientSocket.close();
            }
            clientSocket.close();
        }
    }
}
