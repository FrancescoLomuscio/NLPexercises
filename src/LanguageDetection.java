import java.io.*;
import opennlp.tools.langdetect.*;
import opennlp.tools.util.*;

class LanguageDetection {

	private static LanguageDetectorModel model;

	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		File dir = new File("C:\\Users\\PC\\eclipse-workspace\\NLPexercise\\resources\\models");
		File[] inDir = dir.listFiles();
		boolean exist = false;
		for(File f : inDir) {
			if(f.getName().equals("lang-detect.data")) {
				exist = true;
				break;
			}
		}
		if(!exist) {
			LanguageDetection m = new LanguageDetection("training-data.txt");
		}
		model = new LanguageDetectorModel(new FileInputStream("C:\\Users\\PC\\eclipse-workspace\\NLPexercise\\resources\\models\\lang-detect.data"));
		LanguageDetector ld = new LanguageDetectorME(model);
		// use model for predicting the language
		Language[] languages = ld.predictLanguages("Mi chiamo Claudio e sono di Andria");
		System.out.println("Predicted languages..");
		for (Language language : languages) {
			// printing the language and the confidence score for the test data to belong to
			// the language
			System.out.println(language.getLang() + "  confidence:" + language.getConfidence());
		}
	}

	LanguageDetection(String data) {
		LanguageDetectorSampleStream sampleStream = null;
		try {
			InputStreamFactory dataIn = new MarkableFileInputStreamFactory(
					new File("resources" + File.separator + "training" + File.separator + data));
			ObjectStream lineStream = new PlainTextByLineStream(dataIn, "UTF-8");
			sampleStream = new LanguageDetectorSampleStream(lineStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// training parameters
		TrainingParameters params = new TrainingParameters();
		params.put(TrainingParameters.ITERATIONS_PARAM, 100);
		params.put(TrainingParameters.CUTOFF_PARAM, 5);
		params.put("DataIndexer", "TwoPass");
		params.put(TrainingParameters.ALGORITHM_PARAM, "NAIVEBAYES");
		// train the model
		try {
			model = LanguageDetectorME.train(sampleStream, params, new LanguageDetectorFactory());
			model.serialize(new File("C:\\Users\\PC\\eclipse-workspace\\NLPexercise\\resources\\models\\lang-detect.data"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Completed");
	}
}
