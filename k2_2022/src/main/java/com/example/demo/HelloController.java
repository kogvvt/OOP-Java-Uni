package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HelloController {

    ServerThread serverThread;
    ClientGUIReceiver receiver;

    public HelloController (ServerThread serverThread, ClientGUIReceiver receiver) {
        this.serverThread = serverThread;
        this.receiver = receiver;
        this.receiver.setHelloController (this);
    }

    @FXML
    private Label wordCountLabel;

    @FXML
    private ListView wordList;

    @FXML
    private TextField filterField;

    public void setWordCountLabel (int wordCount) {
        wordCountLabel.setText (Integer.toString (wordCount));
    }

    public void setWordList (List<String> words) {
        if (filterField.getText ().trim ().isEmpty ()) {
            ObservableList<String> wordsObservable = FXCollections.observableArrayList(words.stream()
                    .sorted (Comparator.comparing (firstEntry -> firstEntry.split (" ")[1]))
                    .collect(Collectors.toList())
            );
            wordList.setItems(wordsObservable);
        }
        else {
            ObservableList<String> wordsObservable = FXCollections.observableArrayList(words.stream()
                    .filter (word -> word.split (" ")[1].startsWith (filterField.getText ()))
                    .sorted (Comparator.comparing (firstEntry -> firstEntry.split (" ")[1]))
                    .collect(Collectors.toList())
            );
            wordList.setItems(wordsObservable);
        }
    }
}