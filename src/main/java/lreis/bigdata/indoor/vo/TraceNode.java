package lreis.bigdata.indoor.vo;

/**
 * Created by dq on 5/12/16.
 */
public class TraceNode {
    private Integer polygonNum;
    private Cell cell;
    private Long entryTime;
    private Long exitTime;


    public TraceNode(Integer polygonNum, Long entryTime) {

        this.polygonNum = polygonNum;
        this.entryTime = entryTime;
        cell = Building.getInstatnce().getCellByNum(polygonNum);
    }

    public TraceNode(Cell cell, Long entryTime, Long exitTime) {
        this.cell = cell;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
    }

    public TraceNode() {
    }

    public TraceNode(Integer polygonNum, Long outTime, Long entryTime) {
        this.polygonNum = polygonNum;
        this.exitTime = outTime;
        this.entryTime = entryTime;
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
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
