package lreis.bigdata.indoor.mr.io;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by dq on 10/23/16.
 */
public class RecordWritable implements Writable {


    private String mac;
    private String time;
    private String x;
    private String y;
    private String floor;


    public RecordWritable() {

    }

    public RecordWritable(RecordWritable obj) {
        this.mac = obj.getMac();
        this.time = obj.getTime();
        this.x = obj.getX();
        this.y = obj.getY();
        this.floor = obj.getFloor();
    }

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

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {


        dataOutput.writeUTF(mac);
        dataOutput.writeUTF(floor);
        dataOutput.writeUTF(x);
        dataOutput.writeUTF(y);
        dataOutput.writeUTF(time);

    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {

        this.mac = dataInput.readUTF();
        this.floor = dataInput.readUTF();
        this.x = dataInput.readUTF();
        this.y = dataInput.readUTF();
        this.time = dataInput.readUTF();

    }


    @Override
    public String toString() {
        return String.join(",", this.mac, this.floor, this.x, this.y, this.time);
    }
}
