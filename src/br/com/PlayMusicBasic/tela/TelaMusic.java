package br.com.PlayMusicBasic.tela;

import br.com.PlayMusicBasic.config.ConfigMusic;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class TelaMusic implements Initializable {

    private MediaPlayer mediaPlayer;
    private Media media;
    private int statusPausePlay;
    private Timer timer;
    private ConfigMusic music;

    @FXML
    private Label nomeMusic;

    @FXML
    private ImageView imgPausePlay;

    @FXML
    private ProgressBar pgreceMusic;

    @FXML
    private Slider volumePlaymusic;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        music = new ConfigMusic();
        music.setPosicao(0);
        tocarMusica();
    }

    public void tocarMusica(){
        statusPausePlay = 1;
        media = new Media(new File(music.audio()).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        nomeMusic.setText(music.nomeMusic());
        pausePlay();
        volumePlaymusic.valueProperty().addListener((observableValue, number, t1) -> mediaPlayer.setVolume(volumePlaymusic.getValue() * 0.01));
    }

    public void pausePlay(){
        if(statusPausePlay == 1){
            mediaPlayer.play();
            imgPausePlay.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("imgFundo/icon/Pause.png"))));
            statusPausePlay = 0;
            timeMusic();
        }else{
            mediaPlayer.pause();
            imgPausePlay.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("imgFundo/icon/Play.png"))));
            statusPausePlay = 1;
            cacelTimeMusic();
        }
    }

    public void limparmideia(){
        cacelTimeMusic();
        mediaPlayer.stop();
    }

    public void proximo(){
        if(music.getPosicao() < music.getquantidadeMusic()-1){
            music.setPosicao(music.getPosicao()+1);
        }else{
            music.setPosicao(0);
        }
        limparmideia();
        tocarMusica();
    }

    public void voltar(){
        if(music.getPosicao() != 0){
            limparmideia();
            music.setPosicao(music.getPosicao()-1);
            tocarMusica();
        }
    }

    public void timeMusic(){
        timer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();
                pgreceMusic.setProgress(current / end);

                if (current / end == 1) {
                    cacelTimeMusic();
                    proximo();
                }
            }
        };

        timer.scheduleAtFixedRate(timerTask,1000,1000);
    }

    public void cacelTimeMusic(){
        timer.cancel();
    }



}
