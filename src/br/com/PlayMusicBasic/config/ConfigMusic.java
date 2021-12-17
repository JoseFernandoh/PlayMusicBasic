package br.com.PlayMusicBasic.config;

import javafx.scene.media.Media;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.Random;

public class ConfigMusic {

    private final AcharMusic pegarMusicas = new AcharMusic();
    private final List<Path> files;
    private int posicao;

    public ConfigMusic() {
        pegarMusicas();
        files = pegarMusicas.getFiles();
        aleatoriaMusic();
    }

    public void pegarMusicas() {
        try {
            Files.walkFileTree(Paths.get(System.getProperty("user.home") + "/Music"), pegarMusicas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Media audio() {
        return new Media(files.get(posicao).toUri().toString());
    }

    public String nomeMusic() {
        return "                                                                           " + files.get(posicao).getFileName().toString();
    }

    public String timerMusic(long timer){
        Duration total = Duration.ofSeconds(timer);
        return String.format("%02d:%02d\n" ,total.toMinutesPart(),total.toSecondsPart());
    }

    public void proximaMusic(){
        if(posicao < (getquantidadeMusic() - 1)){
            posicao++;
        }else{
            posicao = 0;
        }

    }

    public void voltarMusic(){
        if(posicao != 0){
            posicao--;
        }else{
            posicao = getquantidadeMusic() - 1;
        }

    }

    public void aleatoriaMusic(){
        Random musicAleatorio = new Random();
        posicao = musicAleatorio.nextInt(getquantidadeMusic());
    }

    public int getquantidadeMusic() {
        return files.size();
    }
}
