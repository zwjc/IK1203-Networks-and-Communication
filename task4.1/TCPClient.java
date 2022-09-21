package com.company.tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient {

    public static String askServer(String hostname, int port, String ToServer) throws  IOException {
        if(ToServer == null) return askServer(hostname, port);

        Socket clientSocket = new Socket(hostname,port);
        clientSocket.setSoTimeout(2000);
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        outToServer.writeBytes(ToServer+'\n');

        StringBuilder sB = new StringBuilder();
        String s;
        try{
            while( (s = inFromServer.readLine()) != "\n" && s != null){
                sB.append(s+'\n');
            }
            clientSocket.close();
            return sB.toString();
        }
        catch( IOException e){
            clientSocket.close();
            return sB.toString();
        }
        //set path=%path%C:\Program Files\Java\jdk1.8.0_101\bin

    }


    public static String askServer(String hostname, int port) throws  IOException {
        Socket clientSocket = new Socket(hostname,port);
        clientSocket.setSoTimeout(2000);
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        StringBuilder sB = new StringBuilder();
        String s;
        final int MAX_LINES = 1024;
        int counter = 0;
        try{
            while( (s = inFromServer.readLine()) != "\n" && s != null){
                sB.append(s+'\n');
                counter++;
                if(counter >= MAX_LINES)
                    return sB.toString();
            }
            clientSocket.close();
            return sB.toString();
        }
        catch( IOException e){
            clientSocket.close();
            return sB.toString();
        }
    }
}