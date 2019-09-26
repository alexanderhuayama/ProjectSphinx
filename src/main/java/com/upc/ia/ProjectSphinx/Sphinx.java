package com.upc.ia.ProjectSphinx;

import java.io.IOException;
import java.nio.file.Paths;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Port;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;

public class Sphinx {
	private String result;
	Thread speechThread;
	Thread resourcesThread;
	private LiveSpeechRecognizer recognizer;

	private volatile boolean recognizerStopped = true;

	public Sphinx() {
		String pathResources = Paths.get(System.getProperty("user.dir"), "src/main/resources").toString();
		String dicEsMx = Paths.get(pathResources, "es-mx", "581HCDCONT10000SPA").toString();
		Configuration configuration = new Configuration();

		configuration.setAcousticModelPath(String.format("%s", dicEsMx));
		configuration.setDictionaryPath(String.format("%s.dic", dicEsMx));
		configuration.setLanguageModelPath(String.format("%s.lm.bin", dicEsMx));
		configuration.setGrammarPath(String.format("%s-grammars", dicEsMx));
		configuration.setGrammarName("grammar");
		configuration.setUseGrammar(true);

		try {
			recognizer = new LiveSpeechRecognizer(configuration);
		} catch (IOException ex) {
		}

		recognizer.startRecognition(true);
		startResourcesThread();
	}

	public void startSpeechThread() {
		if (speechThread != null && speechThread.isAlive())
			return;

		speechThread = new Thread(() -> {
			recognizerStopped = false;

			try {
				while (!recognizerStopped) {
					SpeechResult speechResult = recognizer.getResult();
					if (speechResult != null) {

						result = speechResult.getHypothesis();
						System.out.println("Texto: " + result);
					}

				}
			} catch (Exception ex) {
				recognizerStopped = true;
			}
		});

		speechThread.start();

	}

	public void stopSpeechThread() {
		if (speechThread != null && speechThread.isAlive()) {
			recognizerStopped = true;
		}
	}

	public void startResourcesThread() {
		if (resourcesThread != null && resourcesThread.isAlive())
			return;

		resourcesThread = new Thread(() -> {
			try {
				while (true) {
					if (AudioSystem.isLineSupported(Port.Info.MICROPHONE)) {
					} else {
					}
					Thread.sleep(350);
				}

			} catch (InterruptedException ex) {
				resourcesThread.interrupt();
			}
		});

		resourcesThread.start();
	}
}
