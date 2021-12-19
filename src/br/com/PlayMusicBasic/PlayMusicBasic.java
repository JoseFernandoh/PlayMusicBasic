package br.com.PlayMusicBasic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class PlayMusicBasic extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(this.getClass().getResource("tela/TelaMusic.fxml")));
            primaryStage.setScene(new Scene(fxmlLoader.load()));
            primaryStage.setTitle("PlayMusic");
            primaryStage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("tela/imgFundo/icon/PlayMusic.png"))));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
