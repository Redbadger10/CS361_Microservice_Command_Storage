package org.example;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

import java.util.Scanner;

public class Client {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        try (ZContext context = new ZContext()) {
            System.out.println("Connecting to hello world server");

            ZMQ.Socket socket = context.createSocket(SocketType.REQ);
            socket.connect("tcp://localhost:5555");

            while(true) {
                System.out.printf("Enter the command you would like to send: ");
                String request = scanner.nextLine();
                System.out.println("Sending: " + request);
                socket.send(request.getBytes(ZMQ.CHARSET), 0);

                byte[] reply = socket.recv(0);
                System.out.println(
                        "Received: " + new String(reply, ZMQ.CHARSET)
                );
            }
        }
    }
}