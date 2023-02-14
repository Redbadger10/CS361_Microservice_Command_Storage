package org.example;

import java.io.*;
import java.util.*;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

public class Server {
    public static void main(String[] args) throws Exception {
        try (ZContext context = new ZContext()) {
            ZMQ.Socket socket = context.createSocket(SocketType.REP);
            socket.bind("tcp://*:5555");

            while (!Thread.currentThread().isInterrupted()) {
                String response = "message recieved";
                byte[] reply = socket.recv(0);
                System.out.println(
                        "Received " + ": [" + new String(reply, ZMQ.CHARSET) + "]"
                );

                ///NEW CODE TO EDIT//////////////////////////////////////////////////////////////////////////////
                if (new String(reply, ZMQ.CHARSET).equals("retrieve")) {
                    try {
                        FileReader reader = new FileReader("commands.txt");
                        BufferedReader bufferedReader = new BufferedReader(reader);

                        List<String> entries = new ArrayList<>();
                        String line = bufferedReader.readLine();
                        while (line != null) {
                            entries.add(line);
                            line = bufferedReader.readLine();
                        }

                        bufferedReader.close();

                        String output = String.join("\31", entries);
                        response = output;

                    } catch (IOException e) {
                        System.out.println("Error reading file: " + e.getMessage());
                    }
                } else {
                    try {
                        FileWriter writer = new FileWriter("commands.txt", true);
                        writer.write(new String(reply, ZMQ.CHARSET) + "\n");
                        writer.close();

                    } catch (IOException e) {
                        System.out.println("Error writing file: " + e.getMessage());
                    }
                }





        ///NEW CODE TO EDIT END////////////////////////////////////////////////////////////////////////////////////


                socket.send(response.getBytes(ZMQ.CHARSET), 0);
                Thread.sleep(1000);
            }
        }
    }
}