import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class wordcount {
    // 대, 소문자 구분 없게 하기.

    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();

        if(args.length != 3) {
            System.err.println("Usage : WordCount <input> <output>");
            System.exit(2);
        }

        conf.set("User Parameter", args[2]); // 사용자에게 입력값을 받기 위한 것.
        // 내가 원하는 단어가 있는지를 검색함.

        Job job = new Job(conf, "MainWordCount");

        job.setJarByClass(wordcount.class);
        job.setMapperClass(mapper.class);
        job.setCombinerClass(reducer.class);
        job.setReducerClass(reducer.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.waitForCompletion(true);
        System.out.println("Success");
    }
}