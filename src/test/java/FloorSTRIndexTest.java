import lreis.bigdata.indoor.index.FloorSTRIndex;
import org.junit.Test;

/**
 * Created by dq on 4/25/16.
 */
public class FloorSTRIndexTest {

    @Test
    public void build() throws Exception {

        String shpFile = "/Users/dq/subject_study/20150915_xddyc_download/860100010020300001_f4.dbf";
        FloorSTRIndex.build(shpFile);
        System.out.println(FloorSTRIndex.floorIdx);

    }
}