package br.com.PlayMusicBasic.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ConfigMusic {

    private final AcharMusic pegarMusicas = new AcharMusic();
    private final List<Path> files;
    private int posicao;

    public ConfigMusic() {
        pegarMusicas();
        files = pegarMusicas.getFiles();
    }

    public void pegarMusicas(){
        try {
            Files.walkFileTree(Paths.get(System.getProperty("user.home")+"/Music"),pegarMusicas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String audio(){
        return files.get(posicao).toString();
    }

    public String nomeMusic(){
        return files.get(posicao).getFileName().toString();
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    public int getPosicao() {
        return posicao;
    }

    public int getquantidadeMusic(){
        return files.size();
    }
}
