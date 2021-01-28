import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

public class mapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();

    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        StringTokenizer stringTokenizer = new StringTokenizer(value.toString().toLowerCase(), "\t\n\r\f,.`\"():;?![₩~/]' \"\\");
        Configuration conf = context.getConfiguration();
        String from_user = conf.get("User Parameter");

        while (stringTokenizer.hasMoreTokens()) {
            word.set(stringTokenizer.nextToken());
            if(from_user.equals(word.toString()))
                context.write(word, one);
        }
    }
}
