package lreis.bigdata.indoor.vo;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.index.strtree.STRtree;
import lreis.bigdata.indoor.index.GridIndex;
import lreis.bigdata.indoor.utils.FloorShpUtils;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.simple.SimpleFeature;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dq on 6/14/16.
 */
public class Floor {

    private String floorShp = null;
    private String floorNum = null;
    private List<Cell> cells = null;
    private STRtree strTree = null;// spatial index of cells
    private HashMap<String, ArrayList<Cell>> name2CellMap = null;// use for get cells by name.
    private HashMap<Integer,Cell> num2CellMap = null;// user fot get cell by cell num.
    private GridIndex gridIndex = null;//grid index


      public Floor(String floorNum, String floorShp) {
        this.floorNum = floorNum;
        this.floorShp = floorShp;
        this.init();
    }

    public void init(){
        this.buildIndex();
    }

    public String getFloorNum() {
        return floorNum;
    }

    public String getFloorShp() {
        return floorShp;
    }

    public List<Cell> getCells() {
        if (this.floorShp == null) {
            return null;
        }
        if (this.cells == null) {
            this.readCells();
        }
        return this.cells;
    }


    public STRtree getStrTree() {

        if (this.floorShp == null) {
            return null;
        }
        if (this.strTree == null) {
            this.buildIndex();
        }

        return this.strTree;
    }

    public Cell queryInSTR(Point p){
        List<Cell> list = this.strTree.query(p.getEnvelopeInternal());
        for (Cell c : list) {
            if (c.getGeom().contains(p)) {
                return c;
            }
        }
        return null;
    }

    public Cell queryInGrid(Point p){
        List<Cell> list = this.gridIndex.query(p);
        if (list.size() == 1) {
            return list.get(0);
        }
        else{
            return null;
        }
    }


    public List<Cell> getCellsByName(String name){
        if (this.floorShp == null) {
            return  null;
        }
        if (this.name2CellMap == null) {
            this.buildIndex();
        }
        return this.name2CellMap.get(name);
    }




    private void readCells() {
        this.cells = new ArrayList<Cell>();
        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
        try {
            ShapefileDataStore sds = (ShapefileDataStore) dataStoreFactory.createDataStore(new File(this.floorShp).toURI().toURL());


            sds.setCharset(Charset.forName("UTF-8"));
            SimpleFeatureSource featureSource = sds.getFeatureSource();
            SimpleFeatureIterator iterator = featureSource.getFeatures().features();


            String floorNum = FloorShpUtils.getFloorNum(this.floorShp);
            while (iterator.hasNext()) {

                SimpleFeature feature = iterator.next();

                Geometry geom = (Geometry) feature.getDefaultGeometry();
                Integer nodeNum = ((Long) feature.getAttribute("poi_no")).intValue();
                String name = (String) feature.getAttribute("name_CHN");
                String category = feature.getAttribute("style").toString();

                Cell cell = new Cell();
                cell.setFloorNum(floorNum);
                cell.setName(name);
                cell.setNodeNum(nodeNum);
                cell.setGeom(geom);
                cell.setCategory(category);

                this.cells.add(cell);

            }

            iterator.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 建立空间索引，名字到Cell的映射
     */
    private void buildIndex() {

        if (floorShp == null) return;
        if (this.cells == null) this.readCells();





        // init three indices
        this.strTree = new STRtree();
        this.name2CellMap = new HashMap<String, ArrayList<Cell>>();
        this.num2CellMap = new HashMap<Integer, Cell>();

        for (Cell cell : this.cells) {

            //spatial index
            Envelope envelope = cell.getGeom().getEnvelopeInternal();
            this.strTree.insert(envelope, cell);

            //name index
            if (name2CellMap.get(cell.getName()) == null) {
                name2CellMap.put(cell.getName(), new ArrayList<Cell>());
            }
            name2CellMap.get(cell.getName()).add(cell);

            //num index
            num2CellMap.put(cell.getNodeNum(), cell);
        }


        this.gridIndex = new GridIndex(this);
    }




}

