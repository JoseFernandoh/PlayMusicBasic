package br.com.PlayMusicBasic.tela;

import br.com.PlayMusicBasic.config.ConfigMusic;
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
    private int repet;
    private int repetidor;
    private MediaPlayer mediaPlayer;
    private Media media;
    private boolean statusPlay;
    private Timer timer;
    private ConfigMusic music;

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
        nomeMusic.setText(music.nomeMusic());
        pausePlay();

        volumePlaymusic.valueProperty().addListener((observableValue, number, t1) -> mediaPlayer.setVolume(volumePlaymusic.getValue() * 0.01));

        pgreceMusic.valueChangingProperty().addListener((observableValue, number, t1) -> {
            mediaPlayer.pause();
            Duration time = new Duration((pgreceMusic.getValue() * media.getDuration().toSeconds()) * 1000);
            mediaPlayer.setStartTime(time);
            mediaPlayer.play();
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
        tocarMusica();
        resetRepetidor();
    }

    public void timeMusic() {
        timer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();
                pgreceMusic.setValue((current / end));
                if (current / end == 1) {
                    cacelTimeMusic();
                    proximo();
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 1000, 1000);
    }

    public void cacelTimeMusic() {
        timer.cancel();
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

    public void tocarMusicRepetVoltar(){
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

}
