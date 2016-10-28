package lreis.bigdata.indoor.main;

import lreis.bigdata.indoor.utils.RecordUtils;
import lreis.bigdata.indoor.vo.Building;
import lreis.bigdata.indoor.vo.PositioningPoint;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Scanner;

/**
 * Created by hadoop on 10/26/16.
 */
public class WhyLengthError {


    public static void main(String[] args) {


        try {
            Scanner scanner = new Scanner(new File("error_inspect/LENGTH_ERROR-m-00000"));
            while (scanner.hasNext()) {
                String line = scanner.nextLine();

                String items[] = line.split(",");

                String mac = items[0];
                String floorNum = items[1];
                Float x = Float.parseFloat(items[2]) / 1000;
                Float y = Float.parseFloat(items[3]) / -1000;
                long time = RecordUtils.calcTimeStamp(items[4]);
                PositioningPoint pp = new PositioningPoint(mac, time, x, y, floorNum);

                String id = PositioningPoint.calRowkey(pp, PositioningPoint.QueryMethod.STR);
                System.out.println(id);

            }
            scanner.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Building.getInstatnce();

    }
}
