package lreis.bigdata.indoor.vo;

/**
 * Created by dq on 5/12/16.
 */
public class TraceNode {
    private Integer polygonNum;
    private Long entryTime;
    private Long outTime;


    public TraceNode() {
    }

    public TraceNode(Integer polygonNum, Long outTime, Long entryTime) {
        this.polygonNum = polygonNum;
        this.outTime = outTime;
        this.entryTime = entryTime;
    }

    public Integer getPolygonNum() {
        return polygonNum;
    }

    public void setPolygonNum(Integer polygonNum) {
        this.polygonNum = polygonNum;
    }

    public Long getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Long entryTime) {
        this.entryTime = entryTime;
    }

    public Long getOutTime() {
        return outTime;
    }

    public void setOutTime(Long outTime) {
        this.outTime = outTime;
    }


}
