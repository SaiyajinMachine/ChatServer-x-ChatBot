package io.codeforall.forsome;

import org.academiadecodigo.simplegraphics.graphics.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Worker implements Runnable {

    private Server server;
    private Socket clientSocket;
    protected String name;
    protected BufferedReader in;
    protected PrintWriter out;
    protected String message = null;
    private static int cols = 30;
    private static int rows = 155;

    public Worker(String name, Socket clientSocket, Server server) {
        this.name = name;
        this.clientSocket = clientSocket;
        this.server = server;

        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void checkLimit() {
        if (rows == 275) {
            server.chatBox.delete();
            server.chatBox.draw();
            rows = 155;
            Text text = new Text(cols, rows, "* Mékie cleared the chat *");
            text.draw();
            rows += 15;
        }
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            try {
                message = this.in.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (message.equals("/quit")) {
                System.out.println("Client closed, exiting!");
                break;
            }

            System.out.println("Client " + this.name + ": " + message);
            checkLimit();
            Text text = new Text(cols, rows, "Client " + this.name + ": " + message);
            text.draw();
            rows += 15;

            if (message.equals("mekie")) {
                checkLimit();
                text = new Text(cols, rows, "Mékie: Quack quack simple quack quack graphic");
                text.draw();
                rows += 15;
            } else if (message.equals("adoro")) {
                checkLimit();
                text = new Text(cols, rows, "Mékie: gatas");
                text.draw();
                rows += 15;
            } else if (message.equals("paper") || message.equals("rock") || message.equals("scissors")) {
                playGame(message);
            }

            server.broadcast(message, name);
        }
    }

    private void playGame(String playerMove) {
        String[] moves = {"rock", "paper", "scissors"};
        int choice = (int) (Math.random() * 3);
        String mekiesMove = moves[choice];

        checkLimit();
        Text text = new Text(cols, rows, "Mékie: " + mekiesMove);
        text.draw();
        rows += 15;

        String result;
        if (playerMove.equals(mekiesMove)) {
            result = "Wow no one won... Boring quack!";
        } else if ((playerMove.equals("rock") && mekiesMove.equals("paper")) ||
                (playerMove.equals("scissors") && mekiesMove.equals("rock")) ||
                (playerMove.equals("paper") && mekiesMove.equals("scissors"))) {
            result = "Noob! I won quack!";
        } else {
            result = "I always lose at games quack :(";
        }

        checkLimit();
        text = new Text(cols, rows, "Mékie: " + result);
        text.draw();
        rows += 15;
    }
}
