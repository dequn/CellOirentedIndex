package lreis.bigdata.indoor.dao;

import lreis.bigdata.indoor.factory.DaoFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dq on 5/3/16.
 */
public class ICellDaoTest {

    private ICellDao dao;

    @Before
    public void setUp() throws Exception {
        dao = DaoFactory.getCellDao();
    }

    @Ignore
    @Test
    public void getPOIsByCellName() throws Exception {


        dao.getPOIsByCellName("味千拉面", 1396281619L, 1396281625L);

    }


    @Ignore
    @Test
    public void countMacInCell() throws Exception {

        System.out.println(dao.countMacInCell("味千拉面", 13962816193L, 13962816253L));
    }


    @Test
    public void getPOISBeenToCells() throws Exception {

        List<Integer> list = new ArrayList<Integer>();
        list.add(88);
        list.add(22);

        this.dao.getPOISBeenToCells(list, 1396322620L, 1396322720L);


    }
}