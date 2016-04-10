import java.io.File;
import java.util.*;

import javax.speech.Central;
import javax.speech.Engine;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import javax.speech.synthesis.SynthesizerProperties;
import javax.speech.synthesis.Voice;

/**
 * Simple program showing how to use FreeTTS in JSAPI
 */
public class PrintLoudly {
    private static String input;
    private static Synthesizer synthesizer;
    private static Voice voice;
    private static Scanner kb;

    static private String noSynthesizerMessage() {
        String message =
            "No synthesizer created.  This may be the result of any\n" +
            "number of problems.  It's typically due to a missing\n" +
            "\"speech.properties\" file that should be at either of\n" +
            "these locations: \n\n";
        message += "user.home    : " + System.getProperty("user.home") + "\n";
        message += "java.home/lib: " + System.getProperty("java.home") +
	    File.separator + "lib\n\n" +
            "Another cause of this problem might be corrupt or missing\n" +
            "voice jar files in the freetts lib directory.  This problem\n" +
            "also sometimes arises when the freetts.jar file is corrupt\n" +
            "or missing.  Sorry about that.  Please check for these\n" +
            "various conditions and then try again.\n";
        return message;
    }

    public static void main(String[] argv) {
	kb = new Scanner(System.in);
	try {
        String synthesizerName = System.getProperty
          ("synthesizerName", "Unlimited domain FreeTTS Speech Synthesizer from Sun Labs");
	    // Create a new SynthesizerModeDesc that will match the FreeTTS
	    // Synthesizer.
	    SynthesizerModeDesc desc = new SynthesizerModeDesc(
                null,          // engine name
                "general",     // mode name
                Locale.US,     // locale
                null,          // running
                null);         // voice
	    synthesizer = Central.createSynthesizer(desc);

        /* Just an informational message to guide users that didn't
         * set up their speech.properties file. 
         */
	    if (synthesizer == null) {
		    System.err.println(noSynthesizerMessage());
		    System.exit(1);
	    }
        // create the voice
        String voiceName = System.getProperty("voiceName", "kevin16");
	    voice = new Voice
                (voiceName, Voice.GENDER_DONT_CARE, Voice.AGE_DONT_CARE, null);

	    // get it ready to speak
	    synthesizer.allocate();
	    synthesizer.resume();
            synthesizer.getSynthesizerProperties().setVoice(voice);
	    
	    do {
		// Get a string to speak
		System.out.println("What do you want me to say");
		input = kb.nextLine();
		// speak the typed string
		synthesizer.speakPlainText(input, null);
		// wait till speaking is done
		synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
	    } while (input.length() != 0);
	    
	    // clean up
	    synthesizer.deallocate();
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	System.exit(0);
    }
}