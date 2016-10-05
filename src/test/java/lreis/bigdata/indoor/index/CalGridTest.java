package lreis.bigdata.indoor.index;

import lreis.bigdata.indoor.vo.Floor;
import lreis.bigdata.indoor.vo.Grid;
import lreis.bigdata.indoor.vo.SemanticCell;
import org.junit.Test;

import java.util.Map;

/**
 * Created by Q on 2016/6/20.
 */
public class CalGridTest {

    private final String shpFile = "D:\\big_joy\\floors\\20030.dbf";

    @Test
    public void getGrid2CellMap() throws Exception {
        Floor floor = new Floor("20030", shpFile);
        GridIndex gridIndex = new GridIndex(floor);
        Map<Grid, SemanticCell> map = gridIndex.getGrid2CellMap();
        System.out.print(map.size());

    }

}