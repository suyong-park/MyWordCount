import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class reducer extends Reducer<Text, IntWritable, Text, IntWritable> {

    private final IntWritable result = new IntWritable();

    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {

        Configuration conf = context.getConfiguration();
        String from_user = conf.get("User Parameter");

        int sum = 0;

        if(key.toString().equals(from_user))
            for(IntWritable val : values)
                sum += val.get();

        result.set(sum);
        context.write(key, result);
    }

    // mapper 클래스는 input split당 1개씩 호출되며, map 함수는 하나의 input split 내부에서 record 수 만큼 돌게 된다.
    // 반면 reducer 클래스는 reducer의 개수를 사용자가 지정할 수 있다.
    // 또한, Reduce는 맵 단계의 중간 결과물 마다 reduce 함수를 적용한다.
    // 따라서, map 단계의 결과물의 수를 안다면, No Result를 한 번만 출력되게 할 수 있을 것이다.
}
