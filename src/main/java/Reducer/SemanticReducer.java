package Reducer;

import java.io.IOException;

import Mapper.SemanticMapper;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.log4j.Logger;

/**
 * Created by Nyan on 5/27/2017.
 */
public class SemanticReducer extends
        Reducer<Text, DoubleWritable, Text, DoubleWritable> {
    Logger log = Logger.getLogger(SemanticMapper.class);
    @Override
    protected void reduce(Text token, Iterable<DoubleWritable> counts,
                          Context context) throws IOException, InterruptedException {
        int sum = 0;

        for (DoubleWritable count : counts) {
            sum+= count.get();
        }
        context.write(token, new DoubleWritable(sum));
    }
}
