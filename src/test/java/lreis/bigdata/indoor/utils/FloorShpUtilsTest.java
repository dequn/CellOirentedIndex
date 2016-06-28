package lreis.bigdata.indoor.utils;

import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by dq on 5/11/16.
 */
public class FloorShpUtilsTest {

    @Ignore
    @Test
    public void setPolygonNum() throws Exception {

        List<String> files = new ArrayList<String>();
        files.add("/Users/dq/subject_study/floors/20040.dbf");
        files.add("/Users/dq/subject_study/floors/20030.dbf");
        files.add("/Users/dq/subject_study/floors/20020.dbf");
        files.add("/Users/dq/subject_study/floors/20010.dbf");

        FloorShpUtils.setPolygonNum(files);

    }


}