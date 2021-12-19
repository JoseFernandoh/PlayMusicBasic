package br.com.PlayMusicBasic.config;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class AcharMusic extends SimpleFileVisitor<Path> {

    private final ConfigMusic music;

    public AcharMusic(ConfigMusic music){
        this.music = music;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        if (file.getFileName().toString().endsWith(".mp3")) {
            music.addMusic(file);
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc){
        return FileVisitResult.SKIP_SUBTREE;
    }

}
