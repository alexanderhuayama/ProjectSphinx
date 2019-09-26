package com.upc.ia.ProjectSphinx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectSphinxApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectSphinxApplication.class, args);
		Sphinx sphinx = new Sphinx();
		sphinx.startSpeechThread();
	}

}
