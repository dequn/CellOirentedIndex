package lreis.bigdata.indoor.mr.db;

import lreis.bigdata.indoor.vo.PositioningPoint;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.lib.db.DBWritable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by dq on 10/24/16.
 */
public class PointWritable implements Writable, DBWritable {


    String rowkey;
    PositioningPoint point;


    public String getRowkey() {
        return rowkey;
    }

    public void setRowkey(String rowkey) {
        this.rowkey = rowkey;
    }

    public PositioningPoint getPoint() {
        return point;
    }

    public void setPoint(PositioningPoint point) {
        this.point = point;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {

    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {

    }

    @Override
    public void write(PreparedStatement pstmt) throws SQLException {

        pstmt.setString(1, this.rowkey);
        pstmt.setString(2, this.point.getFloorNum());
        pstmt.setTimestamp(3, new Timestamp(this.point.getTime()));
        pstmt.setString(4, this.point.getMac());
        pstmt.setInt(5, (int) this.point.getX() * 1000);
        pstmt.setInt(6, (int) this.point.getY() * 1000);
        pstmt.setString(7, this.point.getSemanticCellIn().getPolygonNum());
        pstmt.setLong(8, this.point.getTime());

    }

    @Override
    public void readFields(ResultSet resultSet) throws SQLException {

    }


}
