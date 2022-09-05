package com.example.demo;

import javafx.application.Platform;

import java.util.List;

public class ClientGUIReceiver implements ClientReceiver {

    HelloController helloController = null;

    public void setHelloController (HelloController helloController) {
        this.helloController = helloController;
    }

    @Override
    public void receiveWord (List<String> words, int wordCount) {
        Platform.runLater (()->helloController.setWordCountLabel (wordCount));
        Platform.runLater (()->helloController.setWordList (words));
    }
}
