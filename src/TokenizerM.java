import opennlp.tools.dictionary.Dictionary;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.sentdetect.SentenceSampleStream;
import opennlp.tools.tokenize.*;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;
import opennlp.tools.util.TrainingParameters;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class TokenizerM {

	private static SimpleTokenizer simpleTokenizer;
	private static TokenizerModel model;

	public static void main(String[] args) throws IOException {
		/*
		 * String sentence = "Hi. How are you? Welcome to Tutorialspoint. " +
		 * "We provide free tutorials on various technologies";
		 * 
		 * // Instantiating SimpleTokenizer class tokenizer = SimpleTokenizer.INSTANCE;
		 * 
		 * // Tokenizing the given sentence String tokens[] =
		 * tokenizer.tokenize(sentence);
		 * 
		 * // Printing the tokens for (String token : tokens) {
		 * System.out.println(token); }
		 */
		File dir = new File("C:\\Users\\PC\\eclipse-workspace\\NLPexercise\\resources\\models");
		File[] inDir = dir.listFiles();
		boolean exist = false;
		for (File f : inDir) {
			if (f.getName().equals("en-tok.data")) {
				exist = true;
				break;
			}
		}
		if (!exist) {
			TokenizerM m = new TokenizerM(
					"C:\\Users\\PC\\eclipse-workspace\\NLPexercise\\resources\\training\\en-tok.txt",
					"C:\\Users\\PC\\eclipse-workspace\\NLPexercise\\resources\\models\\en-tok.data");
		}
		model = new TokenizerModel(
				new FileInputStream("C:\\Users\\PC\\eclipse-workspace\\NLPexercise\\resources\\models\\en-tok.data"));
		Tokenizer tokenizer = new TokenizerME(model);
		String tokens[] = tokenizer.tokenize("An input sample sentence.");
		for (String s : tokens) {
			System.out.println("\nToken: " + s);
		}

	}

	TokenizerM(String inputFile, String modelFile) throws IOException {
		Objects.nonNull(inputFile);
		Objects.nonNull(modelFile);

		Charset charset = Charset.forName("UTF-8");

		MarkableFileInputStreamFactory factory = new MarkableFileInputStreamFactory(new File(inputFile));
		ObjectStream<String> lineStream = new PlainTextByLineStream(factory, charset);
		ObjectStream<TokenSample> sampleStream = new TokenSampleStream(lineStream);

		try {
			TokenizerFactory tokenizerFactory = new TokenizerFactory("en", new Dictionary(), false, null);

			model = TokenizerME.train(sampleStream, tokenizerFactory, TrainingParameters.defaultParams());
		} finally {
			sampleStream.close();
		}

		OutputStream modelOut = null;
		try {
			modelOut = new BufferedOutputStream(new FileOutputStream(modelFile));
			model.serialize(modelOut);
		} finally {
			if (modelOut != null)
				modelOut.close();
		}

	}
}
