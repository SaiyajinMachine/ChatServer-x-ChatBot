package io.codeforall.forsome;

import org.academiadecodigo.simplegraphics.pictures.Picture;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private int counter = 0;
    private LinkedList<Worker> workers = new LinkedList<>();
    protected static Picture chatBox = new Picture(10,10, "io/codeforall/forsome/pictures/6nf8162y5as01.png");

    public void broadcast(String message,String name) {
        for(Worker worker : workers) {
            worker.out.println("Client " + name + ": " + message);
        }
    }

    public void serverWork() {
        chatBox.draw();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.print("Port: ");
            int portNumber = Integer.parseInt(reader.readLine());
            System.out.println("Binding to port " + portNumber);
            ServerSocket serverSocket = new ServerSocket(portNumber);
            System.out.println("Server started: " + serverSocket);
            System.out.println("Waiting connection...");
            ExecutorService cachedPool = Executors.newCachedThreadPool();

            while (!serverSocket.isClosed()) {

                Socket clientSocket = serverSocket.accept();
                Worker worker = new Worker(String.valueOf(counter), clientSocket, this);
                workers.add(worker);
                cachedPool.submit(worker);
                System.out.println("Client " + counter + " has connected.");
                counter++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
