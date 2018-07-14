package br.com.aee.controller;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by holanda on 30/09/17.
 */
public class FotoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Path diretoriaRaiz;

    private Path diretorioRaizTemp;

    @PostConstruct
    public void init() {
        Path raizAplicacao = FileSystems.getDefault().getPath(System.getProperty("user.home"), ".fotos");

        diretoriaRaiz = raizAplicacao.resolve("convenio");
        diretorioRaizTemp = FileSystems.getDefault().getPath(System.getProperty("java.io.tmpdir"), "foto-convenio-temp");

        try {
            Files.createDirectories(diretoriaRaiz);
            Files.createDirectories(diretorioRaizTemp);
        } catch(IOException e) {
            throw new RuntimeException("Problemas ao tentar criar diretorios", e);
        }
    }

}
