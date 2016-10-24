package lreis.bigdata.indoor.utils;

import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;


/**
 * Created by dq on 4/5/16.
 */

public class FloorShpUtils {


    /**
     * 根据文件名获得楼层号
     *
     * @param floorShpFileName
     * @return 楼层号
     * @throws Exception
     */
    public static String getFloorNum(String floorShpFileName) throws Exception {

        String floorNum;

        int dbfIdx = floorShpFileName.indexOf("dbf");
        if (dbfIdx > 0) {
            floorNum = floorShpFileName.substring(dbfIdx - 2, dbfIdx - 1);
        } else {
            throw new Exception("Floor No. error1");
        }
        return floorNum;
    }


    /**
     * 只需要运行一次就可以了,设置多边形编号,
     *
     * @param floorFiles
     */
    public static void setPolygonNum(List<String> floorFiles,List<Integer> beginNum) {
        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();


        try {
            for (int i= 0; i<floorFiles.size();i++) {

                String file = floorFiles.get(i);
                ShapefileDataStore sds = (ShapefileDataStore) dataStoreFactory.createDataStore(new File(file).toURI().toURL());
                sds.setCharset(Charset.forName("UTF-8"));


                FeatureWriter<SimpleFeatureType, SimpleFeature> writer = sds.getFeatureWriter(sds.getTypeNames()[0], Transaction.AUTO_COMMIT);

                int codeNum = beginNum.get(i);

                while (writer.hasNext()) {
                    System.out.println(codeNum);
                    SimpleFeature feature = writer.next();
                    feature.setAttribute("poi_no", codeNum);
                    codeNum++;
                    writer.write();
                }

                writer.close();
                sds.dispose();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
