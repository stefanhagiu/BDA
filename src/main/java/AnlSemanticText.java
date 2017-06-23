import Mapper.SemanticMapper;
import Reducer.SemanticReducer;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/*
 * MapReduce jobs are typically implemented by using a driver class.
 * The purpose of a driver class is to set up the configuration for the
 * MapReduce job and to run the job.
 * Typical requirements for a driver class include configuring the input
 * and output data formats, configuring the map and reduce classes,
 * and specifying intermediate data formats.
 *
 * The following is the code for the driver class:
 */
public class AnlSemanticText extends Configured implements Tool {

    @Override
    public int run(String[] args) throws Exception {
        Job job = Job.getInstance(getConf(), "AnlSemanticText");
        job.setJarByClass(getClass());

        job.addFileToClassPath(new Path("/uimaj-core-2.8.1.jar"));
        job.addFileToClassPath(new Path("/datavec-api-0.8.0.jar"));
        job.addFileToClassPath(new Path("/opencsv-3.3.jar"));
        job.addFileToClassPath(new Path("/jackson-mapper-asl-1.8.8.jar"));

        // configure input source
        TextInputFormat.addInputPath(job, new Path(args[0]));
        job.setInputFormatClass(TextInputFormat.class);

        // configure mapper and reducer
        job.setMapperClass(SemanticMapper.class);
        job.setCombinerClass(SemanticReducer.class);
        job.setReducerClass(SemanticReducer.class);

        // configure output
        TextOutputFormat.setOutputPath(job, new Path(args[1]));
        job.setOutputFormatClass(TextOutputFormat.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);

        return job.waitForCompletion(true) ? 0 : 1;
    }
    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new AnlSemanticText(), args);
        System.exit(exitCode);
    }
}
