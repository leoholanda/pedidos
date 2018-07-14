package br.com.aee.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Named;

@Named("imagemMB")
public class ImagemBean {

	private List<String> images;

	@PostConstruct
	public void init() {
		images = new ArrayList<String>();
		for (int i = 1; i <= 12; i++) {
			images.add("nature" + i + ".jpg");
		}
	}

	public List<String> getImages() {
		return images;
	}
}
