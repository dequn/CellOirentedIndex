package lreis.bigdata.indoor.mr.main;

import lreis.bigdata.indoor.mr.db.PointWritable;
import lreis.bigdata.indoor.utils.RecordUtils;
import lreis.bigdata.indoor.vo.PositioningPoint;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.phoenix.mapreduce.PhoenixOutputFormat;
import org.apache.phoenix.mapreduce.util.PhoenixConfigurationUtil;
import org.apache.phoenix.mapreduce.util.PhoenixMapReduceUtil;

import java.awt.*;
import java.io.IOException;
import java.text.ParseException;

/**
 * Created by dq on 10/24/16.
 */
public class UpsertRecordsIntoPhoenix {


    public static class UpsertMapper extends Mapper<Object, Text, Text, Text> {


        @Override
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {


            context.write(value, value);


        }


    }

    public static class UpsertReducer extends Reducer<Text, Text, NullWritable, PointWritable> {


        MultipleOutputs mos;


        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            mos = new MultipleOutputs(context);
        }

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) {


            for (Text value : values) {
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
                    try {
                        mos.write("text", NullWritable.get(), value, "UPSERT_ERROR");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    continue;
                }

                PositioningPoint pp = new PositioningPoint(mac, time, x, y, floorNum);
                String id = PositioningPoint.calRowkey(pp, PositioningPoint.QueryMethod.STR);

                if (id == null || id.length() != 33) {// output to a file
                    try {
                        mos.write("text", NullWritable.get(), value, "UPSERT_ERROR");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    PointWritable point = new PointWritable(id, pp);
                    try {
                        mos.write("db", NullWritable.get(), point);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }


        }


        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
//            super.cleanup(context);
            mos.close();
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {


        Configuration conf = HBaseConfiguration.create();


//        conf.set("mapred.job.tracker", "local");
//        conf.set("fs.defaultFS", "hdfs://hadoop-master:9000");
//        conf.set("mapreduce.framework.name", "yarn");
//        conf.set("yarn.resourcemanager.hostname", "hadoop-master");
//        conf.set("yarn.resourcemanager.address", "hadoop-master:8032");
//        System.setProperty("hadoop.home.dir", "/usr/local/bin/hadoop");


        Job job = Job.getInstance(conf, "Ingest Points");

        job.setMapperClass(UpsertMapper.class);
        job.setReducerClass(UpsertReducer.class);

        MultipleOutputs.addNamedOutput(job, "db", PhoenixOutputFormat.class, NullWritable.class, PointWritable.class);
        MultipleOutputs.addNamedOutput(job, "text", TextOutputFormat.class, NullWritable.class, Text.class);


        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);


        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));


//        PhoenixMapReduceUtil.setOutput(job, "BIGJOY.IMOS2", "ID,FLOOR,TIME,MAC,X,Y,SEM_CELL,LTIME");


        PhoenixConfigurationUtil.setOutputTableName(job.getConfiguration(), "BIGJOY.IMOS");
        PhoenixConfigurationUtil.setUpsertColumnNames(job.getConfiguration(), "ID,FLOOR,TIME,MAC,X,Y,SEM_CELL,LTIME".split(","));

        TableMapReduceUtil.addDependencyJars(job);


        job.waitForCompletion(true);

    }


}
