package SentiWordNet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Nyan on 6/21/2017.
 */
public class SentiWordNetDemo {

    /**
     * String that stores the text to guess its polarity.
     */
    String text;

    /**
     * SentiWordNet object to query the polarity of a word.
     */
    SWN3 sentiwordnet = new SWN3("SentiWordNet_3.0.0_20130122.txt");

    public SentiWordNetDemo() throws Exception {
    }

    /**
     * This method loads the text to be classified.
     *
     * @param fileName The name of the file that stores the text.
     */
    public void load(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            text = "";
            while ((line = reader.readLine()) != null) {
                text = text + " " + line;
            }
            // System.out.println("===== Loaded text data: " + fileName + " =====");
            reader.close();
            // System.out.println(text);
        } catch (IOException e) {
            System.out.println("Problem found when reading: " + fileName);
        }
    }

    public void setText(String text) {
        this.text = text;
    }

    /**
     * This method performs the classification of the text.
     * Algorithm: Use all POS, say "yes" in case of 0.
     *
     * @return An string with "no" (negative) or "yes" (positive).
     */
    public Double classifyAllPOSY() {

        double count = 0;
        try {
            String delimiters = " ";
            String[] tokens = text.split(delimiters);

            for (int i = 0; i < tokens.length; ++i) {
                // Add weights -- positive => +1, strong_positive => +2, negative => -1, strong_negative => -2
                if (!tokens[i].equals("")) {
                    // Search as adjetive
                    count += sentiwordnet.extract(tokens[i], "a");
                    // Search as noun
                    count += sentiwordnet.extract(tokens[i], "n");
                    // Search as adverb
                    count += sentiwordnet.extract(tokens[i], "r");
                    // Search as verb
                    count += sentiwordnet.extract(tokens[i], "v");
                }
            }
            // System.out.println(count);
        } catch (Exception e) {
            count = 0;
        }
        // Returns "yes" in case of 0
        return count;
    }
}
