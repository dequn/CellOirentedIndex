package lreis.bigdata.indoor.dao.impl;

import lreis.bigdata.indoor.TestStatic;
import lreis.bigdata.indoor.dao.ICellDao;
import lreis.bigdata.indoor.factory.DaoFactory;
import lreis.bigdata.indoor.vo.Building;
import lreis.bigdata.indoor.vo.PositioningPoint;
import lreis.bigdata.indoor.vo.SemanticCell;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

/**
 * Created by Q on 2016/7/5.
 */
public class PostgreSemanticCellDaoImplTest {

    static ICellDao dao = null;


    @BeforeClass
    public static void before() {
        dao = DaoFactory.getPostgreCellDao();
        TestStatic.BuildingInit();
        TestStatic.readPOIs();
    }


    @After
    public void after() {

    }

    @Test
    public void getPOIsByCell() throws Exception {

        SemanticCell semanticCell = Building.getInstatnce().getCellByNum("201");

        List<PositioningPoint> result = this.dao.getPOIsByCell(semanticCell, 1396281619L, 1396281625L);


    }

    @Test
    public void countMacInCell() throws Exception {
        SemanticCell semanticCell = Building.getInstatnce().getCellByNum("201");
        int num = this.dao.countMacInCell(semanticCell, 1396281619L, 1396281625L);
    }

    @Ignore
    @Test
    public void getPOISBeenToAllCells() throws Exception {


    }

}