package lreis.bigdata.indoor.main;

import lreis.bigdata.indoor.utils.FloorShpUtils;
import lreis.bigdata.indoor.vo.Building;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dq on 10/26/16.
 */
public class SetShpPolygonNum {


    public static void main(String[] args) {


//        List<String> files = new ArrayList<>();
//        List<Integer> beginNums = new ArrayList<>();
//
//
//        for (String shp : Building.floorShps) {
//            beginNums.add(Integer.parseInt(shp.substring(7, 12)) * 1000);
//            String path = Building.class.getResource("/" + shp).getPath();
//            files.add(path);
//        }
//
//        FloorShpUtils.setPolygonNum(files, beginNums);


        Building b = Building.getInstatnce();

    }


}
