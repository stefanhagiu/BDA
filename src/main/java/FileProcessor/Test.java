package FileProcessor;

import SentiWordNet.SentiWordNetDemo;
import com.opencsv.CSVReader;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by Nyan on 6/21/2017.
 */
public class Test {

    public static final String FINAL_FINAL = "./first_final.json";

    public static void main(String[] args) throws Exception {
        File f = new File("D:\\hadoop-dump\\pmc_oa_processed\\first_part");
        ArrayList<File> files = new ArrayList<File>(Arrays.asList(f.listFiles()));

        System.out.println(files);
        try (FileWriter finalResult = new FileWriter(FINAL_FINAL)) {
            finalResult.write("[");
            finalResult.flush();
            int i = 0;
            for(File partFile : files) {
                System.out.println("Running " + i);
                csvReader(partFile, finalResult);

                if (i + 1 < files.size()) {
                    finalResult.write(",");
                    finalResult.flush();
                }
                i++;
            }
            finalResult.write("]");
            finalResult.flush();
        }
    }
    public static void csvReader(File file, FileWriter finalResult) {

        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(file));
            String[] line;
            SentiWordNetDemo senti = new SentiWordNetDemo();
            List<Map<String,String>> json = new ArrayList<>();
            while ((line = reader.readNext()) != null) {
                Map<String, String> jsonObj= new HashMap<>();
                jsonObj.put("journaltitle",line[0]); jsonObj.put("publishername",line[1]);
                jsonObj.put("title",line[2]); jsonObj.put("subject1",line[3]);
                jsonObj.put("subject2",line[4]);
                jsonObj.put("subject3",line[5]); jsonObj.put("subject4",line[6]);
                jsonObj.put("subject5",line[7]); jsonObj.put("institution",line[8]);
                jsonObj.put("location",line[9]); jsonObj.put("locationjournal",line[10]);
                jsonObj.put("year",line[11]); jsonObj.put("month",line[12]);
                String mergedCorpusText = line[13] + " " + line[14] + " " + line[15];
                jsonObj.put("length", String.valueOf(mergedCorpusText.length()));

                mergedCorpusText = mergedCorpusText.replaceAll("[^\\w]", " ");
                mergedCorpusText = mergedCorpusText.replaceAll("[0-9]", "");
                mergedCorpusText = mergedCorpusText.replaceAll("\\s+", " ");

                senti.setText(mergedCorpusText);
                Double rate = senti.classifyAllPOSY();
                jsonObj.put("rate", String.valueOf(rate));

                json.add(jsonObj);
            }

            ObjectMapper mapepr = new ObjectMapper();
            finalResult.write(mapepr.writeValueAsString(json));
            finalResult.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
