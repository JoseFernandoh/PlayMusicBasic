package br.com.PlayMusicBasic.config;

import javafx.scene.media.Media;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ConfigMusic {

    private final AcharMusic pegarMusicas = new AcharMusic(this);
    private final List<Path> files = new ArrayList<>();
    private int posicao;

    public ConfigMusic() {
        pegarMusicas();
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
        return files.get(posicao).getFileName().toString().concat("                    ");
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
        posicao = ThreadLocalRandom.current().nextInt(getquantidadeMusic());
    }

    public int getquantidadeMusic() {
        return files.size();
    }

    public void addMusic(Path path){
        files.add(path);
    }
}
