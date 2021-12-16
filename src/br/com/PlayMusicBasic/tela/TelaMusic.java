package br.com.PlayMusicBasic.tela;

import br.com.PlayMusicBasic.config.ConfigMusic;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.*;

public class TelaMusic implements Initializable {

    private boolean aleatorio = true;
    private int repet = 1;
    private int repetidor;
    private MediaPlayer mediaPlayer;
    private Media media;
    private boolean statusPlay;
    private ConfigMusic music;
    private Timeline progressMusic;
    private Timeline nomeRotation;
    private int number;
    private static final double MIN_CHANGE = 500;

    @FXML
    private Label nomeMusic;

    @FXML
    private ImageView imgaleatorio;

    @FXML
    private ImageView imgrepet;

    @FXML
    private ImageView imgPausePlay;

    @FXML
    private Slider pgreceMusic;

    @FXML
    private Slider volumePlaymusic;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        music = new ConfigMusic();
        aleatorioMusic();
        tocarMusica();
    }

    public void tocarMusica() {
        statusPlay = false;
        media = new Media(new File(music.audio()).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        nomeRotation();
        pausePlay();
        volumePlaymusic.valueProperty().addListener((observableValue, number, t1) -> mediaPlayer.setVolume(volumePlaymusic.getValue() * 0.01));

        pgreceMusic.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                if(!pgreceMusic.isValueChanging()){
                    double time = mediaPlayer.getCurrentTime().toSeconds();
                    double end = media.getDuration().toSeconds();
                    System.out.println(time / end);
                    System.out.println(t1.doubleValue());
                    System.out.println(Math.abs(time - t1.doubleValue()));
                    System.out.println("--------------------------------");
                    if(Math.abs(time - t1.doubleValue()) > MIN_CHANGE){
                        mediaPlayer.seek(Duration.millis(t1.doubleValue()));
                    }

                }
            }
        });
    }

    public void pausePlay() {
        if (statusPlay) {
            mediaPlayer.pause();
            imgPausePlay.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("imgFundo/icon/Play.png"))));
            statusPlay = false;
            cacelTimeMusic();
        } else {
            mediaPlayer.play();
            imgPausePlay.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("imgFundo/icon/Pause.png"))));
            statusPlay = true;
            timeMusic();
        }
    }

    public void limparmideia() {
        cacelTimeMusic();
        mediaPlayer.stop();
    }

    public void proximo() {
        tocarMusicRepetproximo();
        if (aleatorio && repet == 1) {
            aleatorioMusic();
        } else {
            if (music.getPosicao() < music.getquantidadeMusic() - 1) {
                music.setPosicao(music.getPosicao() + 1);
            } else {
                music.setPosicao(0);
            }
        }
        limparmideia();
        nomeRotation.stop();
        tocarMusica();
        resetRepetidor();
    }

    public void voltar() {
        tocarMusicRepetVoltar();
        if (aleatorio && repet == 1) {
            aleatorioMusic();
        } else {
            if (music.getPosicao() != 0) {
                music.setPosicao(music.getPosicao() - 1);
            }
        }
        limparmideia();
        nomeRotation.stop();
        tocarMusica();
        resetRepetidor();
    }

    public void timeMusic() {
        progressMusic = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
            double current = mediaPlayer.getCurrentTime().toSeconds();
            double end = media.getDuration().toSeconds();
            pgreceMusic.setValue((current / end));
            if (current / end == 1) {
                proximo();
            }
        }));
        progressMusic.setCycleCount(Animation.INDEFINITE);
        progressMusic.play();
    }

    public void cacelTimeMusic() {
        progressMusic.stop();
    }

    public void aleatorioMusic() {
        Random musicAleatorio = new Random();
        music.setPosicao(musicAleatorio.nextInt(music.getquantidadeMusic()));
    }

    public void aleatorio() {
        if (aleatorio) {
            imgaleatorio.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("imgFundo/icon/aleatorio.png"))));
            aleatorio = false;
        } else {
            imgaleatorio.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("imgFundo/icon/aleatorioSelect.png"))));
            aleatorio = true;
        }
    }

    public void repet() {
        if (repet == 0) {
            imgrepet.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("imgFundo/icon/repet.png"))));
            repetidor = 0;
            repet = 1;
        } else if (repet == 1) {
            imgrepet.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("imgFundo/icon/repet1.png"))));
            repetidor = 1;
            repet = 2;
        } else {
            imgrepet.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("imgFundo/icon/repetinfynit.png"))));
            repetidor = Integer.MAX_VALUE;
            repet = 0;
        }
    }

    public void tocarMusicRepetproximo() {
        if (repetidor > 0) {
            music.setPosicao(music.getPosicao() - 1);
            repetidor--;
        }
    }

    public void tocarMusicRepetVoltar() {
        if (repetidor > 0) {
            music.setPosicao(music.getPosicao() + 1);
            repetidor--;
        }
    }

    public void resetRepetidor() {
        if (repet == 2) {
            repet = 0;
            repet();
        }
    }

    public void nomeRotation() {
        number = 0;
        nomeRotation = new Timeline(new KeyFrame(Duration.millis(170), ev -> {
            if (number != music.nomeMusic().length()) {
                nomeMusic.setText(music.nomeMusic().substring(number));
                number++;
            } else {
                number = 0;
            }
        }));
        nomeRotation.setCycleCount(Animation.INDEFINITE);
        nomeRotation.play();
    }

}
