import java.io.*;
import java.nio.charset.StandardCharsets;
import opennlp.tools.sentdetect.*;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;
import opennlp.tools.util.TrainingParameters;

public class SentenceDetection {

	private static SentenceModel model;

	public static void main(String[] args) throws IOException {
		File dir = new File("C:\\Users\\PC\\eclipse-workspace\\NLPexercise\\resources\\models");
		File[] inDir = dir.listFiles();
		boolean exist = false;
		for (File f : inDir) {
			if (f.getName().equals("en-sent.data")) {
				exist = true;
				break;
			}
		}
		if (!exist) {
			SentenceDetection m = new SentenceDetection("en-sent.txt");
		}
		model = new SentenceModel(new FileInputStream(
				new File("C:\\Users\\PC\\eclipse-workspace\\NLPexercise\\resources\\models\\en-sent.data")));
		SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);
		String sentences[] = sentenceDetector.sentDetect("  First sentence. Second sentence.            Mario has black hair,blue eyes and big ears.");
		int i = 1;
		for (String s : sentences) {
			System.out.println(i + "): " + s);
			i++;
		}
		Span spans[] = sentenceDetector.sentPosDetect("  First sentence. Second sentence.            Mario has black hair,blue eyes and big ears.");
		i = 1;
		for (Span s : spans) {
			System.out.println(i + "): " + s);
			i++;
		}
	}

	SentenceDetection(String data) throws FileNotFoundException, IOException {
		InputStreamFactory in = new MarkableFileInputStreamFactory(
				new File("C:\\Users\\PC\\eclipse-workspace\\NLPexercise\\resources\\training\\" + data));

		SentenceModel model;
		TrainingParameters mlParams = new TrainingParameters();
		mlParams.put(TrainingParameters.ITERATIONS_PARAM, Integer.toString(15));
		mlParams.put(TrainingParameters.CUTOFF_PARAM, Integer.toString(1));
		model = SentenceDetectorME.train("en",
				new SentenceSampleStream(new PlainTextByLineStream(in, StandardCharsets.UTF_8)), true, null, mlParams);
		File modelOut = new File("C:\\Users\\PC\\eclipse-workspace\\NLPexercise\\resources\\models\\en-sent.data");
		FileOutputStream fs = new FileOutputStream(modelOut);
		model.serialize(modelOut);
	}

}
