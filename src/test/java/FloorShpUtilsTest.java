import lreis.bigdata.indoor.utils.FloorShpUtils;
import lreis.bigdata.indoor.vo.Shop;
import org.junit.Test;

import java.util.List;

/**
 * Created by dq on 4/25/16.
 */
public class FloorShpUtilsTest {

    @Test
    public void getFloorNum() throws Exception {

    }

    @Test
    public void read() throws Exception {

        List<Shop> list = FloorShpUtils.getShopList("/Users/dq/subject_study/20150915_xddyc_download/860100010020300001_f4.dbf");
        for (Shop l : list) {
            System.out.println(l);
        }
    }

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