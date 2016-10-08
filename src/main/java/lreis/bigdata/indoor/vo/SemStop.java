package lreis.bigdata.indoor.vo;

/**
 * Created by dq on 5/12/16.
 */
public class SemStop {
    private String polygonNum;
    private SemanticCell semanticCell;

    private long entryTime;
    private long exitTime;
    private long stayTime;


    public long getStayTime() {
        return stayTime;
    }

    public SemStop(String polygonNum, Long entryTime) {

        this.polygonNum = polygonNum;
        this.entryTime = entryTime;
//        semanticCell = Building.getInstatnce().getSemCellByNum(polygonNum);
    }

    public SemStop(SemanticCell semanticCell, Long entryTime, Long exitTime) {
        this.semanticCell = semanticCell;
        this.entryTime = entryTime;
        this.exitTime = exitTime;

        this.calStopTime();
    }

    public SemStop() {

    }

    public SemStop(String polygonNum, Long exitTime, Long entryTime) {
        this.polygonNum = polygonNum;
        this.exitTime = exitTime;
        this.entryTime = entryTime;
        this.calStopTime();
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

    public long getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(long entryTime) {
        this.entryTime = entryTime;
        this.calStopTime();

    }

    public long getExitTime() {
        return exitTime;
    }

    public void setExitTime(long exitTime) {
        this.exitTime = exitTime;
        this.calStopTime();
    }


    public void calStopTime() {
        this.stayTime = this.exitTime - this.entryTime;
    }

}
