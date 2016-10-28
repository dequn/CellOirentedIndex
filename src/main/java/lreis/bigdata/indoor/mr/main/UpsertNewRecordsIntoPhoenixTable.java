package lreis.bigdata.indoor.mr.main;

import lreis.bigdata.indoor.mr.db.PointWritable;
import lreis.bigdata.indoor.utils.RecordUtils;
import lreis.bigdata.indoor.vo.Building;
import lreis.bigdata.indoor.vo.PositioningPoint;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.phoenix.mapreduce.PhoenixOutputFormat;
import org.apache.phoenix.mapreduce.util.PhoenixConfigurationUtil;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by dq on 10/27/16.
 */
public class UpsertNewRecordsIntoPhoenixTable {
    public static class UpsertNewRecordMapper extends Mapper<Object, Text, NullWritable, MultipleOutputs> {

        MultipleOutputs mos;

        @Override
        protected void setup(Mapper.Context context) throws IOException, InterruptedException {
            mos = new MultipleOutputs(context);
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            super.cleanup(context);
            mos.close();
        }

        @Override
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {


            String line = value.toString();
            String items[] = line.split("\t");

            if (items[5].equals("0") && items[4].equals("0")) {
                mos.write("text", NullWritable.get(), value, "LOCATION_0_0");
                return;
            }
            if (items[3].equals("20060") || items[3].equals("20070") || items[3].equals("20080")) {
                mos.write("text", NullWritable.get(), value, "UNDO");
                return;
            }

            String mac = items[1];
            String floorNum = items[3];
            float x = Float.parseFloat(items[4]) / 1000;
            float y = Float.parseFloat(items[5]) / -1000;
            long time = 0;

            try {
                time = RecordUtils.calcTimeStamp(items[0]);
            } catch (ParseException e) {
                mos.write("text", NullWritable.get(), value, "TIME_PARSE_ERROR");
                return;
            }
            PositioningPoint pp = new PositioningPoint(mac, time, x, y, floorNum);
            String id = PositioningPoint.calRowkey(pp, PositioningPoint.QueryMethod.STR);

            if (id == null) {// output to a file
                mos.write("text", NullWritable.get(), value, "NOT_DROPED_IN_A_CELL");
            } else {
                PointWritable point = new PointWritable(id, pp);
                mos.write("db", NullWritable.get(), point);
            }

        }


    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {


        Configuration conf = HBaseConfiguration.create();

//        conf.set("hbase.zookeeper.quorum", "hadoop-master,hadoop-slave1,hadoop-slave2");
//        conf.set("hbase.zookeeper.property.clientPort", "2181");
//
//
//        conf.set("mapred.job.tracker", "local");
//        conf.set("fs.defaultFS", "hdfs://hadoop-master:9000");
//        conf.set("mapreduce.framework.name", "yarn");
//        conf.set("yarn.resourcemanager.hostname", "hadoop-master");
//        conf.set("yarn.resourcemanager.address", "hadoop-master:8032");
//        System.setProperty("hadoop.home.dir", "/usr/local/bin/hadoop");



        Job job = Job.getInstance(conf, "Ingest New Points");

//        job.setJar("/home/hadoop/IdeaProjects/CellOirentedIndex/classes/artifacts/CellOrientedIndex_jar/CellOrientedIndex.jar");
        job.setMapperClass(UpsertNewRecordMapper.class);
        job.setNumReduceTasks(0);

        job.setMapOutputValueClass(MultipleOutputs.class);

        MultipleOutputs.addNamedOutput(job, "db", PhoenixOutputFormat.class, NullWritable.class, PointWritable.class);
        MultipleOutputs.addNamedOutput(job, "text", TextOutputFormat.class, NullWritable.class, Text.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        PhoenixConfigurationUtil.setOutputTableName(job.getConfiguration(), "BIGJOY.IMOS");
        PhoenixConfigurationUtil.setUpsertColumnNames(job.getConfiguration(), "ID,FLOOR,TIME,MAC,X,Y,SEM_CELL,LTIME".split(","));

        TableMapReduceUtil.addDependencyJars(job);


        System.exit(job.waitForCompletion(true) ? 0 : 1);

    }
}
