package lreis.bigdata.indoor.vo;

import lreis.bigdata.indoor.TestStatic;
import lreis.bigdata.indoor.dao.IPOIDao;
import lreis.bigdata.indoor.factory.DaoFactory;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Q on 2016/6/21.
 */
public class BuildingTest {

    @Ignore
    @Test
    public void CompareEfficiencyBetweenGridAndSTR() {

        int compareNum = 5000;
        try {
            FileWriter writer = new FileWriter(new File("d:\\big_joy\\compare.txt"));
            writer.write("time,compareNum,grid(ms),STR(ms)\n");
            while (compareNum < TestStatic.positioningPoints.size()) {

                //针对一定数量的pois对比3次
                for (int j = 0; j < 3; j++) {

                    long start = System.currentTimeMillis();

                    for (int i = 0; i < compareNum; i++) {
                        PositioningPoint positioningPoint = TestStatic.positioningPoints.get(i);
                        Building.queryCell(Building.getInstatnce().getFloor(positioningPoint.getFloorNum()),
                                positioningPoint, PositioningPoint
                                        .QueryMethod.Grid);
                    }
                    long stop = System.currentTimeMillis();
                    writer.write(String.format("%s,%s,%s", j, compareNum, stop - start));
                    System.out.println("Grid query time of " + compareNum + " POIs is " + (stop - start));
                    start = System.currentTimeMillis();
                    for (int i = 0; i < compareNum; i++) {
                        PositioningPoint positioningPoint = TestStatic.positioningPoints.get(i);
                        Building.queryCell(Building.getInstatnce().getFloor(positioningPoint.getFloorNum()), positioningPoint, PositioningPoint.QueryMethod.STR);
                    }
                    stop = System.currentTimeMillis();
                    writer.write(String.format(",%s\n", stop - start));
                    System.out.println("STR query time of " + compareNum + " POIs is " + (stop - start));

                }
                compareNum += 5000;
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    @Test
    public void comparePostGISAndHBase() {
        try {


//            FileWriter writer = new FileWriter(new File("d:\\big_joy\\hbase_postgre_compare.txt"));
            FileWriter writer = new FileWriter(new File("/home/zdq/big_joy/hbase_postgre_compare.text"));
            writer.write("times,positioningPoints,postgre_time,hbase_time\n");

            IPOIDao dao = null;

            int startNum = 0;
            int endNum = 10000;
            int groupNum = 1;
            while (endNum < TestStatic.positioningPoints.size()) {


                dao = DaoFactory.getPostgrePOIDao();

                Long timeBegin = System.currentTimeMillis();

                for (int j = startNum; j < endNum; j++) {
                    dao.insertPOI(TestStatic.positioningPoints.get(j));
                }

                Long timeStop = System.currentTimeMillis();
                Long timeSpan = timeStop - timeBegin;

                writer.write(String.format("%s,%s,%s", groupNum, endNum - startNum, timeSpan));
                System.out.println("postgresql_time is " + timeSpan);

                dao = DaoFactory.getHBasePOIDao();
                timeBegin = System.currentTimeMillis();

                for (int j = startNum; j < endNum; j++) {
                    dao.insertPOI(TestStatic.positioningPoints.get(j));
                }

                timeStop = System.currentTimeMillis();
                timeSpan = timeStop - timeBegin;
                System.out.println("hbase_time is " + timeSpan);

                writer.write(String.format(",%s\n", timeSpan));


                startNum = endNum;
                groupNum += 1;
                endNum += 10000 * groupNum;

            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}