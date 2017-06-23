package Mapper;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.regex.Pattern;


import SentiWordNet.SentiWordNetDemo;
import com.opencsv.CSVReader;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Created by Nyan on 5/27/2017.
 */
public class SemanticMapper extends Mapper<LongWritable, Text, Text, DoubleWritable> {
    private  DoubleWritable count = null;
    private  Text reusableText = null;
    private  Pattern pattern = null;
    private  SentiWordNetDemo senti = null;

    @Override
    public void setup(Context context) throws IOException {
        try {
            senti = new SentiWordNetDemo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        pattern = Pattern.compile(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        reusableText = new Text();
        count = new DoubleWritable();
    }
    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {


        CSVReader reader = null;
        try {
            reader = new CSVReader(new StringReader(value.toString()));
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

                count.set(rate);

            }
//            System.out.println(json);
            ObjectMapper mapepr = new ObjectMapper();
            reusableText.set(mapepr.writeValueAsString(json));
            context.write(reusableText, count);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        context.write(reusableText, count);


    }
}
