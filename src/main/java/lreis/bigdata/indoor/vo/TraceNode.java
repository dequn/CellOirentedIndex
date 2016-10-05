package lreis.bigdata.indoor.vo;

/**
 * Created by dq on 5/12/16.
 */
public class TraceNode {
    private String polygonNum;

    private SemanticCell semanticCell;

    private Long entryTime;
    private Long exitTime;


    public TraceNode(String polygonNum, Long entryTime) {

        this.polygonNum = polygonNum;
        this.entryTime = entryTime;
        semanticCell = Building.getInstatnce().getCellByNum(polygonNum);
    }

    public TraceNode(SemanticCell semanticCell, Long entryTime, Long exitTime) {
        this.semanticCell = semanticCell;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
    }

    public TraceNode() {

    }

    public TraceNode(String polygonNum, Long exitTime, Long entryTime) {
        this.polygonNum = polygonNum;
        this.exitTime = exitTime;
        this.entryTime = entryTime;
    }

    public SemanticCell getSemanticCell() {
        return semanticCell;
    }

    public void setSemanticCell(SemanticCell semanticCell) {
        this.semanticCell = semanticCell;
    }

    public String getPolygonNum() {
        return polygonNum;
    }

    public void setPolygonNum(String polygonNum) {
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
