package com.example.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ServerThread extends Thread {
    Socket socket;
    List<String> words = new ArrayList<String> ();
    int wordCount = 0;
    private ClientReceiver receiver = null;

    public void setReceiver (ClientReceiver receiver) {
        this.receiver = receiver;
    }

    public ServerThread () {
        try {
            socket = new Socket ("localhost", 5000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run () {
        try {
            BufferedReader input = new BufferedReader (new InputStreamReader (socket.getInputStream ()));
            String message;
            while ((message = input.readLine ()) != null) {
                System.out.println(message);
                words.add (DateTimeFormatter.ofPattern ("HH:mm:ss").format (LocalDateTime.now ()) + " " + message);
                wordCount++;
                receiver.receiveWord (words, wordCount);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
