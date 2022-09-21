package com.company;

import java.net.*;
import java.io.*;
import com.company.tcpclient.TCPClient;

public class MyRunnable implements Runnable {

    Socket clientSocket;

    public MyRunnable(Socket c){
        clientSocket = c;
    }

    public void run(){
        System.out.println("run");
        try{
            String host = null;
            String port = null;
            String input = null;

            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());

            String s = inFromClient.readLine();
            String[] sArr = s.split("[ =&?/]");

            if(sArr[2].equals("ask")){
                for(int i = 0; i<sArr.length;i++){
                    if(sArr[i].equals("hostname")){
                        host = sArr[i+1];
                        i++;
                    }
                    else if(sArr[i].equals("port")){
                        port = sArr[i+1];
                        i++;
                    }
                    else if(sArr[i].equals("string")){
                        input = sArr[i+1];
                        i++;
                    }
                }
                if(host == null || port == null){
                    throw new IOException();
                }
                String sAnswer = null;
                try{
                    sAnswer = TCPClient.askServer(host,Integer.parseInt(port),input);
                    String header  = "HTTP/1.1 200 OK\r\n\r\n";
                    outToClient.writeBytes(header + sAnswer +"\r\n");
                }
                catch( Exception e){
                    outToClient.writeBytes("HTTP/1.1 404 Not found\r\n");
                }
            }
            else{	outToClient.writeBytes("HTTP/1.1 400 Bad request\r\n"); }

            clientSocket.close();
            inFromClient.close();
            outToClient.close();
            System.out.println("Closed");
        }
        catch(IOException e){
            System.out.println("Error:run");
        }
    }
}