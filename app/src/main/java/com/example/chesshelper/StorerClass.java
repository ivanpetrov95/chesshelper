package com.example.chesshelper;

import java.util.HashMap;
import java.util.Map;

public class StorerClass {

    private Map<String, String> figuresMovementDescriptions = new HashMap<String, String>();

    public StorerClass()
    {
        figuresMovementDescriptions.put("pawn","This is a pawn. The pawn moves only one square, only forward. After the demonstration the robot will return in its start position.");
        figuresMovementDescriptions.put("bishop", "This is a bishop. The bishop can move few squares, only diagonally. After the demonstration the robot will return in its start position.");
        figuresMovementDescriptions.put("rook", "This is a rook. The rook can move few squares, horizontally and vertically. After the demonstration the robot will return in its start position.");
        figuresMovementDescriptions.put("queen", "This is a queen. The queen can move few squares, horizontally, vertically and diagonally. After the demonstration the robot will return in its start position.");
        figuresMovementDescriptions.put("king", "This is a king. The king can move only one square, horizontally, vertically and diagonally. After the demonstration the robot will return in its start position.");
        figuresMovementDescriptions.put("knight", "This is a knight. The knight can move only in an L shape, horizontally and vertically . After the demonstration the robot will return in its start position.");
    }
    private String[] informationText =
            {
                    "Welcome to Chess Helper!",
                    "Press on the capture button in order to take a picture of the object for recognition.",
                    "In this menu you can choose to identify the object and see a demonstration of the movement or go back to take another picture."
            };

    public String[] retrieveInformationText()
    {
        return informationText;
    }

    public String retrieveFigureMovementText(String key)
    {
        return figuresMovementDescriptions.get(key);
    }
}
