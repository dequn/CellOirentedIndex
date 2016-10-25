package lreis.bigdata.indoor.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by dq on 5/11/16.
 */
public class FloorShpUtilsTest {

    @Test
    public void setPolygonNum() throws Exception {

        List<String> files = new ArrayList<String>();
        List<Integer> beginNum = new ArrayList<>();

        files.add("/home/zdq/big_joy/floors/20050.dbf");
        beginNum.add(20050000);

        files.add("/home/zdq/big_joy/floors/20040.dbf");
        beginNum.add(20040000);

        files.add("/home/zdq/big_joy/floors/20030.dbf");
        beginNum.add(20030000);

        files.add("/home/zdq/big_joy/floors/20020.dbf");
        beginNum.add(20020000);

        files.add("/home/zdq/big_joy/floors/20010.dbf");
        beginNum.add(20010000);

        files.add("/home/zdq/big_joy/floors/10020.dbf");
        beginNum.add(10020000);

        files.add("/home/zdq/big_joy/floors/10010.dbf");
        beginNum.add(10010000);

        FloorShpUtils.setPolygonNum(files, beginNum);

    }


}