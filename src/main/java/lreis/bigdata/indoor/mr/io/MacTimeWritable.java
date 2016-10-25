package lreis.bigdata.indoor.mr.io;

import org.apache.hadoop.io.WritableComparable;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by dq on 10/23/16.
 */
public class MacTimeWritable implements WritableComparable<MacTimeWritable> {

    public String mac;
    public String time;


    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    @Override
    public void write(DataOutput dataOutput) throws IOException {

        dataOutput.writeUTF(mac);
        dataOutput.writeUTF(time);

    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {

        this.mac = dataInput.readUTF();
        this.time = dataInput.readUTF();
    }


    public boolean equals(MacTimeWritable obj) {

        return this.mac.equals(obj.getMac()) && this.time.equals(obj.getTime());
    }


    @Override
    public String toString() {
        return String.join(",", this.mac, this.time);
    }

    @Override
    public int compareTo(MacTimeWritable o) {
        return this.toString().compareTo(o.toString());
    }

}
