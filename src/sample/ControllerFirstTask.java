package sample;

import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Random;

public class ControllerFirstTask {


    public GridPane userGridPane;
    private final int imageHeight = 500;
    public ChoiceBox<String> userChoiceBox;
    private int iconHeight;
    private final int imageWidth = 500;
    private int iconWidth;
    public Button restartButton;
    private ImageView[] images;
    private Random random;
    private int[][] table;
    private Image image;
    private ImageView[] answer;
    private final static String[] ELEMENTS = {"3", "4", "5", "10", "20"};
    private int num = 5;

    public void initialize() {
        random = new Random();

        openImage();

        restartButton.setOnAction(e -> shuffle());

        userChoiceBox.setItems(FXCollections.observableArrayList(ELEMENTS));
        userChoiceBox.setValue("5");
        userChoiceBox.setOnAction(e -> {
            num = Integer.parseInt(userChoiceBox.getValue());
            setSizes();
            setGridPane();
            divideImage();
            shuffle();
        });

        setSizes();
        setGridPane();
        divideImage();
        shuffle();

    }

    private void openImage() {
        try {
            InputStream is = new FileInputStream(new File("src\\sample\\cat.jpg"));
            image = new Image(is, imageWidth, imageHeight, false, false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void divideImage() {

        images = new ImageView[num * num];
        table = new int[num + 1][num + 1];
        for (int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
                Image image1 = SwingFXUtils.toFXImage(bufferedImage.getSubimage(j * iconHeight, i * iconWidth
                        , iconWidth, iconHeight), null);

                ImageView imageView = new ImageView(image1);
                imageView.setOnMouseClicked(f -> createMyListener(imageView));

                images[i * num + j] = imageView;
            }
        }
    }

    private void createMyListener(ImageView imageView) {
        int index = 0;
        for (int k = 0; k < answer.length; k++) {
            if (answer[k] == imageView) {
                index = k;
                break;
            }
        }
        int row, column;
        row = index / num + 1;
        column = index % num + 1;
        if (table[row - 1][column] == 0) {
            answer[index - num] = imageView;
            answer[index] = null;
            table[row - 1][column] = 1;
            table[row][column] = 0;
            updateTable();
        } else if (table[row + 1][column] == 0) {
            answer[index + num] = imageView;
            answer[index] = null;
            table[row + 1][column] = 1;
            table[row][column] = 0;
            updateTable();
        } else if (table[row][column - 1] == 0) {
            answer[index - 1] = imageView;
            answer[index] = null;
            table[row][column - 1] = 1;
            table[row][column] = 0;
            updateTable();
        } else if (table[row][column + 1] == 0) {
            answer[index + 1] = imageView;
            answer[index] = null;
            table[row][column + 1] = 1;
            table[row][column] = 0;
            updateTable();
        }
        checkAnswer();
    }

    private void updateTable() {
        userGridPane.getChildren().clear();
        for (int k = 0; k < answer.length; k++) {
            if (answer[k] != null) {
                int indI = k / num;
                int indJ = k % num;
                userGridPane.add(answer[k], indJ, indI);
            }
        }
    }

    private void setSizes() {
        iconWidth = imageWidth / num;
        iconHeight = imageHeight / num;
    }

    private void setGridPane() {
        userGridPane.getColumnConstraints().clear();
        userGridPane.getRowConstraints().clear();
        for (int i = 0; i < num; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(iconWidth);
            userGridPane.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < num; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(iconHeight);
            userGridPane.getRowConstraints().add(rowConst);
        }
    }

    private void shuffle() {
        boolean[] used = new boolean[num * num];
        table = new int[num + 2][num + 2];
        answer = new ImageView[num * num + 1];
        userGridPane.getChildren().clear();
        for (int i = 0; i <= num + 1; i++) {
            for (int j = 0; j <= num + 1; j++) {
                if (i == 0 || i == num + 1 || j == 0 || j == num + 1) {
                    table[i][j] = 1;
                }
            }

        }
        for (int i = 0; i < num * num; i++) {

            if (i != num - 1) {
                while (true) {
                    int a = Math.abs(random.nextInt() % (num * num));
                    if (!used[a]) {
                        used[a] = true;

                        int indI = a / num;
                        int indJ = a % num;


                        table[indI + 1][indJ + 1] = 1;
                        answer[a] = images[i];

                        userGridPane.add(images[i], indJ, indI);
                        break;
                    }
                }
            }


        }
    }

    private void checkAnswer() {
        boolean flag = true;
        for (int i = 0; i < num * num; i++) {
            if (i != num - 1) {
                if (!(answer[i] == images[i])) {
                    flag = false;
                    break;
                }
            }
        }
        if (flag) {
            Alert alert = new Alert(Alert.AlertType.NONE, "You did it", ButtonType.OK);
            alert.showAndWait();
        }
    }


}

