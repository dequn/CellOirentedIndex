import lreis.bigdata.indoor.index.FloorSTRIndex;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by dq on 4/25/16.
 */
public class PositioningDataUtilsTest {

    String dataFile = "/Users/dq/subject_study/f4_points.csv";

    static {
        FloorSTRIndex.build("/Users/dq/subject_study/20150915_xddyc_download/860100010020300001_f4.dbf");
    }

    @Ignore
    @Test
    public void queryNodeNum() throws Exception {
        System.out.println("-------------queryNodeNUm begin-------------------");

        BufferedReader reader = new BufferedReader(new FileReader(this.dataFile));
        for (int i = 0; i < 20; i++) {
            String line = reader.readLine();
            System.out.println(PositioningDataUtils.queryNodeNum(line));

        }
        reader.close();

        System.out.println("-------------queryNodeNUm end-------------------");
    }

    @Ignore
    @Test
    public void getFloorNum() throws Exception {

        System.out.println("-------------gelFloorNum begin-------------------");
        BufferedReader reader = new BufferedReader(new FileReader(this.dataFile));
        for (int i = 0; i < 20; i++) {
            String line = reader.readLine();
            System.out.println(PositioningDataUtils.getFloorNum(line));

        }
        reader.close();
        System.out.println("-------------gelFloorNum end-------------------");

    }

    @Ignore
    @Test
    public void genPointFeature() throws Exception {
        System.out.println("-------------genPointFeature begin-------------------");
        BufferedReader reader = new BufferedReader(new FileReader(this.dataFile));
        for (int i = 0; i < 20; i++) {
            String line = reader.readLine();
            System.out.println(PositioningDataUtils.genPointFeature(line));

        }
        reader.close();
        System.out.println("-------------genPointFeature end-------------------");

    }

    @Ignore
    @Test
    public void genRowKey() throws Exception {
        System.out.println("------------- genRowKey begin-------------------");
        BufferedReader reader = new BufferedReader(new FileReader(this.dataFile));
        for (int i = 0; i < 20; i++) {
            String line = reader.readLine();
            System.out.println(PositioningDataUtils.genRowKey(line));

        }
        reader.close();
        System.out.println("------------- genRowKey end-------------------");

    }

    @Test
    public void putLineToHBase() throws IOException {


        System.out.println("------------- put data begin-------------------");

        BufferedReader reader = new BufferedReader(new FileReader(this.dataFile));
        for (int i = 0; i < 20; i++) {
            String line = reader.readLine();
            System.out.println(PositioningDataUtils.putLineToHBase(line));

        }
        reader.close();
        HBase.close();
        System.out.println("------------- put data end-------------------");
    }
}