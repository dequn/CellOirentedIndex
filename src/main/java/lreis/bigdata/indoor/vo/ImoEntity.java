package lreis.bigdata.indoor.vo;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Q on 2016/6/27.
 */
@Entity
@Table(name = "imo", schema = "public", catalog = "big_data_zdq")
public class ImoEntity {
    private String rowkey;
    private Short x;
    private Short y;
    private String mac;
    private Timestamp time;

    @Id
    @Column(name = "rowkey")
    public String getRowkey() {
        return rowkey;
    }

    public void setRowkey(String rowkey) {
        this.rowkey = rowkey;
    }

    @Basic
    @Column(name = "x")
    public Short getX() {
        return x;
    }

    public void setX(Short x) {
        this.x = x;
    }

    @Basic
    @Column(name = "y")
    public Short getY() {
        return y;
    }

    public void setY(Short y) {
        this.y = y;
    }

    @Basic
    @Column(name = "mac")
    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    @Basic
    @Column(name = "time")
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImoEntity imoEntity = (ImoEntity) o;

        if (rowkey != null ? !rowkey.equals(imoEntity.rowkey) : imoEntity.rowkey != null) return false;
        if (x != null ? !x.equals(imoEntity.x) : imoEntity.x != null) return false;
        if (y != null ? !y.equals(imoEntity.y) : imoEntity.y != null) return false;
        if (mac != null ? !mac.equals(imoEntity.mac) : imoEntity.mac != null) return false;
        if (time != null ? !time.equals(imoEntity.time) : imoEntity.time != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = rowkey != null ? rowkey.hashCode() : 0;
        result = 31 * result + (x != null ? x.hashCode() : 0);
        result = 31 * result + (y != null ? y.hashCode() : 0);
        result = 31 * result + (mac != null ? mac.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        return result;
    }
}
