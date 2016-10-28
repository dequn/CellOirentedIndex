package lreis.bigdata.indoor.mr.main;

import lreis.bigdata.indoor.utils.RecordUtils;
import lreis.bigdata.indoor.vo.PositioningPoint;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by dq on 10/26/16.
 */
public class InspectUpsertErrors {


    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {


        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf, "Inspect Upsert Error");

        job.setJarByClass(InspectUpsertErrors.class);

        job.setMapperClass(InpsectMapper.class);
        job.setMapOutputKeyClass(NullWritable.class);
        job.setMapOutputValueClass(Text.class);


        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.waitForCompletion(true);

    }

    public static class InpsectMapper extends Mapper<Object, Text, NullWritable, Text> {

        MultipleOutputs mos;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            super.setup(context);
            mos = new MultipleOutputs(context);
        }

        @Override
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {


            String line = value.toString();

            String items[] = line.split(",");
            String mac = items[0];
            String floorNum = items[1];
            float x = Float.parseFloat(items[2]) / 1000;
            float y = Float.parseFloat(items[3]) / -1000;
            long time = 0;
            try {
                time = RecordUtils.calcTimeStamp(items[4]);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            PositioningPoint pp = new PositioningPoint(mac, time, x, y, floorNum);
            String id = PositioningPoint.calRowkey(pp, PositioningPoint.QueryMethod.STR);

            if (id == null) {
                mos.write(NullWritable.get(), value, "NULL");
            } else if (id.length() != 33) {
                mos.write(NullWritable.get(), value, "LENGTH_ERROR");
            }

        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            super.cleanup(context);
            mos.close();
        }

    }

}
