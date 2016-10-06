package lreis.bigdata.indoor.vo;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.index.strtree.STRtree;
import lreis.bigdata.indoor.index.GridIndex;
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

    private List<SemanticCell> semanticCells = null;

    private STRtree strTree = null;// spatial index of semanticCells

    private HashMap<String, ArrayList<SemanticCell>> name2CellMap = null;// use for get semanticCells by name.
    private HashMap<String, SemanticCell> num2CellMap = null;// user fot get cell by cell num.

    private GridIndex gridIndex = null;//grid index


    public Floor(String floorNum, String floorShp) {
        this.floorNum = floorNum;
        this.floorShp = floorShp;
        this.init();
    }

    public void init() {
        this.buildIndex();
    }

    public String getFloorNum() {
        return floorNum;
    }

    public String getFloorShp() {
        return floorShp;
    }

    public List<SemanticCell> getSemanticCells() {
        if (this.floorShp == null) {
            return null;
        }
        if (this.semanticCells == null) {
            this.readCells();
        }
        return this.semanticCells;
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

    public SemanticCell queryInSTR(Point p) {
        List<SemanticCell> list = this.strTree.query(p.getEnvelopeInternal());
        for (SemanticCell c : list) {
            if (c.getGeom().contains(p)) {
                return c;
            }
        }
        return null;
    }

    public SemanticCell queryInGrid(Point p) {
        List<SemanticCell> list = this.gridIndex.query(p);
        if (list.size() == 1) {
            return list.get(0);
        } else {
            return null;
        }
    }


    public List<SemanticCell> getCellsByName(String name) {
        if (this.floorShp == null) {
            return null;
        }
        if (this.name2CellMap == null) {
            this.buildIndex();
        }
        return this.name2CellMap.get(name);
    }


    private void readCells() {
        this.semanticCells = new ArrayList<SemanticCell>();
        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
        try {
            ShapefileDataStore sds = (ShapefileDataStore) dataStoreFactory.createDataStore(new File(this.floorShp).toURI().toURL());


            sds.setCharset(Charset.forName("UTF-8"));
            SimpleFeatureSource featureSource = sds.getFeatureSource();
            SimpleFeatureIterator iterator = featureSource.getFeatures().features();


            while (iterator.hasNext()) {

                SimpleFeature feature = iterator.next();

                Geometry geom = (Geometry) feature.getDefaultGeometry();

                String nodeNum = (String.valueOf( feature.getAttribute("poi_no")));

                String name = (String) feature.getAttribute("name_CHN");

                String category = feature.getAttribute("style").toString();

                SemanticCell semanticCell = new SemanticCell();
                semanticCell.setFloorNum(floorNum);
                semanticCell.setName(name);
                semanticCell.setNodeNum(nodeNum);
                semanticCell.setGeom(geom);
                semanticCell.setCategory(category);

                this.semanticCells.add(semanticCell);

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
        if (this.semanticCells == null) this.readCells();


        // init three indices
        this.strTree = new STRtree();
        this.name2CellMap = new HashMap<String, ArrayList<SemanticCell>>();
        this.num2CellMap = new HashMap<>();

        for (SemanticCell semanticCell : this.semanticCells) {

            //spatial index
            Envelope envelope = semanticCell.getGeom().getEnvelopeInternal();
            this.strTree.insert(envelope, semanticCell);

            //name index
            if (name2CellMap.get(semanticCell.getName()) == null) {
                name2CellMap.put(semanticCell.getName(), new ArrayList<SemanticCell>());
            }
            name2CellMap.get(semanticCell.getName()).add(semanticCell);

            //num index
            num2CellMap.put(semanticCell.getNodeNum(), semanticCell);
        }


        this.gridIndex = new GridIndex(this);
    }


}

