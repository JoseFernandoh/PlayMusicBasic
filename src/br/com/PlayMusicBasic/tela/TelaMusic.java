package br.com.PlayMusicBasic.tela;

import br.com.PlayMusicBasic.config.ConfigMusic;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class TelaMusic implements Initializable {

    private boolean aleatorio = true;
    private int repet;
    private int repetidor;
    private MediaPlayer mediaPlayer;
    private boolean statusPlay;
    private ConfigMusic music;
    private Timeline nomeRotation;
    private int number;
    private static final double MIN_CHANGE = 0.5;

    @FXML
    private Label nomeMusic;

    @FXML
    private Label timeFinal;

    @FXML
    private Label timeInicio;

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
        tocarMusica();
    }

    public void tocarMusica() {
        statusPlay = false;
        mediaPlayer = new MediaPlayer(music.audio());
        startSliderNomeMusic();
        pausePlay();
    }

    public void pausePlay(){
        if (statusPlay) {
            mediaPlayer.pause();
            imgPausePlay.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("imgFundo/icon/Play.png"))));
            statusPlay = false;
        } else {
            mediaPlayer.play();
            imgPausePlay.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("imgFundo/icon/Pause.png"))));
            statusPlay = true;
        }
    }

    public void configMusic() {
        nomeRotation.stop();
        mediaPlayer.stop();
        tocarMusica();
    }

    public boolean testButoes(){
        if(repetidor > 0){
            repetidor--;
            if(repetidor == 0){
                repet = -1;
                repet();
            }
            return false;
        }else if (aleatorio){
            music.aleatoriaMusic();
            return false;
        }
        return true;
    }

    public void proximo(){
        if(testButoes()){
            music.proximaMusic();
        }
        configMusic();
    }

    public void voltar(){
        if (testButoes()){
            music.voltarMusic();
        }
        configMusic();
    }

    public void aleatorio(){
        if (aleatorio) {
            imgaleatorio.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("imgFundo/icon/aleatorio.png"))));
            aleatorio = false;
        } else {
            imgaleatorio.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("imgFundo/icon/aleatorioSelect.png"))));
            aleatorio = true;
        }
    }

    public void repet() {
        if (repet == -1) {
            imgrepet.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("imgFundo/icon/repet.png"))));
            repetidor = 0;
            repet = 0;
        } else if (repet == 0) {
            imgrepet.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("imgFundo/icon/repet1.png"))));
            repetidor = 1;
            repet = 1;
        } else {
            imgrepet.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("imgFundo/icon/repetinfynit.png"))));
            repetidor = Integer.MAX_VALUE;
            repet = -1;
        }
    }

    public void startSliderNomeMusic() {
        number = 0;
        mediaPlayer.setVolume(volumePlaymusic.getValue() * 0.01);

        // Nome Rotacionando
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

        // Volume
        volumePlaymusic.valueProperty().addListener((observableValue, number, t1) ->
                mediaPlayer.setVolume(volumePlaymusic.getValue() * 0.01));

        // Carregar dados do Final da Musica
        mediaPlayer.totalDurationProperty().addListener(((observableValue, duration, t1) -> {
            pgreceMusic.setMax(t1.toSeconds());
            timeFinal.setText(music.timerMusic((long) t1.toSeconds()));
        }));

        // Arrastar e soltar para Alterar a Posiçao da Musica
        pgreceMusic.valueChangingProperty().addListener(((observableValue, aBoolean, t1) -> {
            if (!t1) {
                mediaPlayer.seek(Duration.seconds(pgreceMusic.getValue()));
            }
        }));

        // Clicar para Alterar a Posiçao da Musica
        pgreceMusic.valueProperty().addListener(((observableValue, number1, t1) -> {
            if (!pgreceMusic.isValueChanging()) {
                double time = mediaPlayer.getCurrentTime().toSeconds();
                if (Math.abs(time - t1.doubleValue()) > MIN_CHANGE) {
                    mediaPlayer.seek(Duration.seconds(t1.doubleValue()));
                }
            }
        }));

        // Configurar Dados do Time Percorrido da Musica
        mediaPlayer.currentTimeProperty().addListener((observableValue, number1, t1) -> {
            if (!pgreceMusic.isValueChanging()) {
                pgreceMusic.setValue(t1.toSeconds());
                timeInicio.setText(music.timerMusic((long) t1.toSeconds()));
            }
        });

        // Ir para Proxima Musica ao Acabara
        mediaPlayer.setOnEndOfMedia(this::proximo);
    }
}