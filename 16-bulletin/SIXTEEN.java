// package 16-bulletin-board;
// package bulletin;
import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class SIXTEEN{
    public static void main(String[] args) {
		String filePath="";
		// Check the number of parameters.
		//args[0] = "../pride-and-prejudice";
		if (args.length == 1) {
			filePath = args[0];
		} else {
			System.out.println("Please provide the file path.");
		}
		
		// Check if input files exists.
		if (!new File(filePath).exists()) {
			System.err.println("Could not find " + filePath);
			System.exit(1);
		}
		// Initialize event bus.
		EventManager.initialize();

        

		Map<String, Object> map = new HashMap<>();
        map.put("path", filePath);
        DataStorage dataStorage = new DataStorage(map, EventManager.DATA_STORAGE, EventManager.WORD_FREQUENCY_COUNTER);
        WordFrequencyCounter wordFrequencyCounter_top = new WordFrequencyCounter(map,
                EventManager.WORD_FREQUENCY_COUNTER, EventManager.TOP_25);
        // Top25 wordFrequencyCounter_z = new Top25(map, EventManager.WORD_FREQUENCY_COUNTER,
        //         EventManager.WORD_WITH_Z);
        Top25 wordFrequencyCounter_z = new Top25(map, EventManager.TOP_25,
        EventManager.WORD_WITH_Z);

        EventManager.announce(EventManager.DATA_STORAGE, map);
        // EventManager.announce(EventManager.WORD_FREQUENCY_COUNTER, map);
        // EventManager.announce(EventManager.TOP_25, map);
	}
    

}

