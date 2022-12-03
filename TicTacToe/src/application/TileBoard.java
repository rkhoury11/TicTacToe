package application;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class TileBoard {

    private StackPane pane;
    private InfoCenter infoCenter;
    private Tile[][] tiles = new Tile[3][3];

    private char playerTurn = 'X';
    private boolean isEndOfGame = false;

    public TileBoard(InfoCenter infoCenter) {
        this.infoCenter = infoCenter;
        pane = new StackPane();
        pane.setMinSize(Constants.APP_WIDTH, Constants.TILE_BOARD_HEIGHT);
        pane.setTranslateX(Constants.APP_WIDTH / 2);
        pane.setTranslateY((Constants.TILE_BOARD_HEIGHT / 2) + Constants.INFO_CENTER_HEIGHT);

        addAllTiles();
    }

    private void addAllTiles() {
        for (int row = 0; row < 3; row++){
            for (int col = 0; col < 3; col++){
                Tile tile = new Tile();
                tile.getStackPane().setTranslateX((col * 100) - 100);
                tile.getStackPane().setTranslateY((row * 100) - 100);
                pane.getChildren().add(tile.getStackPane());
                tiles[row][col] = tile;
            }
        }
    }

    public void startNewGame() {
        isEndOfGame = false;
        playerTurn = 'X';
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
               tiles[row][col].setValue(""); 
            }
        }
    }

    public void changePlayerTurn() {
        if (playerTurn == 'X' ) {
            playerTurn = 'O';
        } else {
            playerTurn = 'X';
        }
        infoCenter.updateMessage("Player " + playerTurn + "'s turn");
    }

    public String getPlayerTurn() {
        return String.valueOf(playerTurn);
    }

    public StackPane getStackPane() {
        return pane;
    }

    public void checkForWinner() {
        checkColsForWinner();
        checkRowsForWinner();
        checkTopLeftToBottomRightForWinner();
        checkTopRightToBottomLeftForWinner();
        checkForStalemate();
    }

    private void checkForStalemate() {
        if (!isEndOfGame) {
            for (int row = 0; row < 3 ; row++) {
                for (int col = 0; col < 3; col++) {
                    if (tiles[row][col].getValue().isEmpty()){
                        return;
                    }
                }
            }

            isEndOfGame = true;
            infoCenter.updateMessage("Stalemate...");
            infoCenter.showStartButton();
        }
    }

    private void checkTopRightToBottomLeftForWinner() {
        if (!isEndOfGame) {
            if (!isEndOfGame){
                if (tiles[0][2].getValue().equals(tiles[1][1].getValue()) && tiles[0][2].getValue().equals(tiles[2][0].getValue()) && !tiles[0][2].getValue().isEmpty()) {
                    String winner = tiles[0][2].getValue();
                        endGame(winner);
                        return;
                }
            }
        }
    }

    private void checkTopLeftToBottomRightForWinner() {
        if (!isEndOfGame){
            if (tiles[0][0].getValue().equals(tiles[1][1].getValue()) && tiles[0][0].getValue().equals(tiles[2][2].getValue()) && !tiles[0][0].getValue().isEmpty()) {
                String winner = tiles[0][0].getValue();
                    endGame(winner);
                    return;
            }
        }
    }

    private void checkColsForWinner() {
        if (!isEndOfGame) {
            for (int col = 0; col < 3; col ++) {
                if (tiles[0][col].getValue().equals(tiles[1][col].getValue()) && tiles[0][col].getValue().equals(tiles[2][col].getValue()) && !tiles[0][col].getValue().isEmpty()) {
                    String winner = tiles[0][col].getValue();
                    endGame(winner);
                    return;
                }
            }
        }
    }

    private void checkRowsForWinner() {
        if (!isEndOfGame) {
            for (int row = 0; row < 3; row ++) {
                if (tiles[row][0].getValue().equals(tiles[row][1].getValue())
                        && tiles[row][0].getValue().equals(tiles[row][2].getValue())
                        && !tiles[row][0].getValue().isEmpty()) {
                    String winner = tiles[row][0].getValue();
                    endGame(winner);
                    return;
                }
            }
        }
    }

    private void endGame(String winner) {
            isEndOfGame = true;
            infoCenter.updateMessage("Player " + winner + " wins");
            infoCenter.showStartButton();
    }

    private class Tile {

        private StackPane pane;
        private Label label;

        public Tile() {
            pane = new StackPane();
            pane.setMinSize(100,100);

            Rectangle border = new Rectangle();
            border.setWidth(100);
            border.setHeight(100);
            border.setFill(Color.TRANSPARENT);
            border.setStroke(Color.BLACK);
            pane.getChildren().add(border);

            label = new Label("");
            label.setAlignment(Pos.CENTER);
            label.setFont(Font.font(24));
            pane.getChildren().add(label);

            pane.setOnMouseClicked(event -> {
                if (infoCenter.clicked == true){
                    if (label.getText().isEmpty() && !isEndOfGame){
                        label.setText(getPlayerTurn());
                        changePlayerTurn();
                        checkForWinner();
                    }
                }
            });
        }

        public StackPane getStackPane() {
            return pane;
        }

        public String getValue() {
            return label.getText();
        }

        public void setValue(String value) {
            label.setText(value);
        }
    }
}
