import lreis.bigdata.indoor.utils.FloorShpUtils;
import lreis.bigdata.indoor.vo.Cell;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

/**
 * Created by dq on 4/25/16.
 */
public class FloorShpUtilsTest {


//    private String f4Shp = "/Users/dq/subject_study/20150915_xddyc_download/860100010020300001_f4.dbf";
    private String f4Shp = "D:\\big_joy\\20150915_xddyc_download\\860100010020300001_f4.dbf";

    @Test
    public void getFloorNum() throws Exception {

    }

    @Test
    public void read() throws Exception {

        List<Cell> list = FloorShpUtils.getCellList(f4Shp);
        for (Cell cell : list) {
            System.out.println(cell);
        }
    }

    @Ignore
    @Test
    public void setPolygonNum() throws Exception {

//        List<String> files = new ArrayList<String>();
//        files.add("/Users/dq/subject_study/20150915_xddyc_download/860100010020300001_f4.dbf");
//        files.add("/Users/dq/subject_study/20150915_xddyc_download/860100010020300001_f3.dbf");
//        files.add("/Users/dq/subject_study/20150915_xddyc_download/860100010020300001_f2.dbf");
//        files.add("/Users/dq/subject_study/20150915_xddyc_download/860100010020300001_f1.dbf");
//
//
//        FloorShpUtils.setPolygonNum(files);

    }
}