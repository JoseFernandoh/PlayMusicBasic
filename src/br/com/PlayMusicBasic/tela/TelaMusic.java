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
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class TelaMusic implements Initializable {

    private MediaPlayer mediaPlayer;
    private Media media;
    private boolean statusPlay;
    private Timer timer;
    private ConfigMusic music;

    @FXML
    private Label nomeMusic;

    @FXML
    private ImageView imgPausePlay;

    @FXML
    private Slider pgreceMusic;

    @FXML
    private Slider volumePlaymusic;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        music = new ConfigMusic();
        music.setPosicao(0);
        tocarMusica();
    }

    public void tocarMusica(){
        statusPlay = false;
        media = new Media(new File(music.audio()).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        nomeMusic.setText(music.nomeMusic());
        pausePlay();
        volumePlaymusic.valueProperty().addListener((observableValue, number, t1) -> mediaPlayer.setVolume(volumePlaymusic.getValue() * 0.01));
    }

    public void calcularTimeMusic(){
        mediaPlayer.pause();
        Duration time = new Duration((pgreceMusic.getValue()*media.getDuration().toSeconds()) *1000);
        System.out.println(time);
        mediaPlayer.setStartTime(time);
        mediaPlayer.play();
    }

    public void pausePlay(){
        if(statusPlay){
            mediaPlayer.pause();
            imgPausePlay.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("imgFundo/icon/Play.png"))));
            statusPlay = false;
            cacelTimeMusic();
        }else{
            mediaPlayer.play();
            imgPausePlay.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("imgFundo/icon/Pause.png"))));
            statusPlay = true;
            timeMusic();
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
                pgreceMusic.setValue((current/end));
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
