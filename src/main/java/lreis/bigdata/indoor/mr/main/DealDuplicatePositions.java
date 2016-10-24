package lreis.bigdata.indoor.mr.main;

import lreis.bigdata.indoor.mr.MacTimeWritable;
import lreis.bigdata.indoor.mr.RecordWritable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dq on 10/23/16.
 * deal with data where there are more than one position of a mac at the same time.
 */
public class DealDuplicatePositions {


    public static final int MAX_DISTANCE = 1000;
    public static final String DUPLICATE_POSITIONS = "DUPLICATE_POSITIONS";

    public static class DealDuplicatePositionMapper extends Mapper<Object, Text, MacTimeWritable, RecordWritable> {


        MacTimeWritable mt = new MacTimeWritable();
        RecordWritable record = new RecordWritable();

        @Override
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {


            String[] words = value.toString().split(",");


            mt.setMac(words[0]);
            mt.setTime(words[4]);


            record.setMac(words[0]);
            record.setFloor(words[1]);
            record.setX(words[2]);
            record.setY(words[3]);
            record.setTime(words[4]);

            context.write(mt, record);

        }

    }


    public static class DealDuplicatePositionReducer extends Reducer<MacTimeWritable, RecordWritable, NullWritable, RecordWritable> {


        static NullWritable nullKey = NullWritable.get();
        MultipleOutputs mos = null;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            mos = new MultipleOutputs(context);
        }

        @Override
        protected void reduce(MacTimeWritable key, Iterable<RecordWritable> values, Context context) throws IOException, InterruptedException {


            List<RecordWritable> list = new ArrayList<>();

            for (RecordWritable record : values) {
                list.add(new RecordWritable(record));
            }

            if (list.size() == 1) {//only one position, output directly

                mos.write(nullKey, list.get(0), list.get(0).getFloor());

            } else { // more than one position at the same time

                boolean canUse = true;

                for (int i = 0; i < list.size() && canUse; i++) {

                    if (!canUse) {
                        break;
                    }

                    RecordWritable r1 = list.get(i);
                    for (int j = i + 1; j < list.size() && canUse; j++) {
                        RecordWritable r2 = list.get(j);

                        if (r1.getFloor().equals(r2.getFloor())) {

                            if (Math.abs(Integer.parseInt(r1.getX()) - Integer.parseInt(r2.getX())) < MAX_DISTANCE
                                    &&
                                    Math.abs(Integer.parseInt(r1.getY()) - Integer.parseInt(r2.getY())) < MAX_DISTANCE
                                    ) {

                                continue;

                            } else {
                                canUse = false;
                                break;
                            }

                        } else {
                            canUse = false;
                            break;
                        }

                    }
                }


                if (canUse) {

                    int sumx = 0;
                    int sumy = 0;

                    for (RecordWritable record : list) {
                        sumx += Integer.parseInt(record.getX());
                        sumy += Integer.parseInt(record.getY());
                    }

                    RecordWritable newRecord = new RecordWritable();

                    newRecord.setMac(list.get(0).getMac());
                    newRecord.setTime(list.get(0).getTime());
                    newRecord.setFloor(list.get(0).getFloor());
                    newRecord.setX(String.valueOf(sumx / list.size()));
                    newRecord.setY(String.valueOf(sumy / list.size()));


                    mos.write(nullKey, newRecord, newRecord.getFloor());


                } else {

                    for (RecordWritable record : list) {
                        mos.write(nullKey, record, DUPLICATE_POSITIONS);
                    }

                }


            }


        }


        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            mos.close();
        }
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {


        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Deal Duplicates Positions");

        job.setJarByClass(DealDuplicatePositions.class);

        job.setMapperClass(DealDuplicatePositionMapper.class);
        job.setReducerClass(DealDuplicatePositionReducer.class);


        job.setMapOutputKeyClass(MacTimeWritable.class);
        job.setMapOutputValueClass(RecordWritable.class);

        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(RecordWritable.class);


        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));


        System.exit(job.waitForCompletion(true) ? 0 : 1);


    }


}
