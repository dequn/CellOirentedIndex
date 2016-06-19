package lreis.bigdata.indoor.utils;

import lreis.bigdata.indoor.index.FloorSTRIndex;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by dq on 5/11/16.
 */
public class FloorShpUtilsTest {

    @Ignore
    @Test
    public void setPolygonNum() throws Exception {

        List<String> files = new ArrayList<String>();
        files.add("/Users/dq/subject_study/20150915_xddyc_download/860100010020300001_f4.dbf");
        files.add("/Users/dq/subject_study/20150915_xddyc_download/860100010020300001_f3.dbf");
        files.add("/Users/dq/subject_study/20150915_xddyc_download/860100010020300001_f2.dbf");
        files.add("/Users/dq/subject_study/20150915_xddyc_download/860100010020300001_f1.dbf");

        FloorShpUtils.setPolygonNum(files);

    }




}