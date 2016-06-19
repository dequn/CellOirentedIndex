package lreis.bigdata.indoor.vo;

/**
 * Created by dq on 5/12/16.
 */
public class TraceNode {
    private Integer polygonNum;
    private Long entryTime;
    private Long exitTime;


    public TraceNode() {
    }

    public TraceNode(Integer polygonNum, Long outTime, Long entryTime) {
        this.polygonNum = polygonNum;
        this.exitTime = outTime;
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

    public Long getExitTime() {
        return exitTime;
    }

    public void setExitTime(Long exitTime) {
        this.exitTime = exitTime;
    }


}
