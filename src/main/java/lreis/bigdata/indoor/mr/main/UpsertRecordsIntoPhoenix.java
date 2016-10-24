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
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.phoenix.mapreduce.util.PhoenixMapReduceUtil;

import java.awt.*;
import java.io.IOException;
import java.text.ParseException;

/**
 * Created by dq on 10/24/16.
 */
public class UpsertRecordsIntoPhoenix {


    public static class UpsertMapper extends Mapper<Object, Text,LongWritable, PointWritable> {


        PointWritable point = new PointWritable();

        static LongWritable l = new LongWritable(1
        );

        @Override
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {


            String line = value.toString();

            String items[] = line.split(",");
            String mac = items[0];
            String floorNum = items[1];
            Float x = Float.parseFloat(items[2]) / 1000;
            Float y = Float.parseFloat(items[3]) / -1000;
            long time = 0;

            try {
                time = RecordUtils.calcTimeStamp(items[4]);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            PositioningPoint pp = new PositioningPoint(mac, time, x, y, floorNum);
            String id = PositioningPoint.calRowkey(pp, PositioningPoint.QueryMethod.STR);

            if (id == null) {// output to a file

            } else {
                point.setRowkey(id);
                point.setPoint(pp);
                context.write(l, point);
            }


        }
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {


        Configuration conf = HBaseConfiguration.create();

        Job job = Job.getInstance(conf, "Ingest Points");


        FileInputFormat.addInputPath(job, new Path(args[0]));

        job.setMapOutputKeyClass(LongWritable.class);
        job.setMapOutputValueClass(PointWritable.class);


        PhoenixMapReduceUtil.setOutput(job, "BIGJOY.IMOS2", "id,floor,time,mac,x,y,sem_cell,ltime");

        job.setMapperClass(UpsertMapper.class);
        TableMapReduceUtil.addDependencyJars(job);

        job.waitForCompletion(true);

    }


}
