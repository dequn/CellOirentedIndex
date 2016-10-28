package lreis.bigdata.indoor.dao.proxy;

import lreis.bigdata.indoor.dao.ISemanticInfoDao;
import lreis.bigdata.indoor.dao.impl.SemanticInfoDaoImpl;
import lreis.bigdata.indoor.dbc.MongoConn;
import org.bson.Document;

/**
 * Created by dq on 10/6/16.
 */
public class SemanticInfoDaoProxy implements ISemanticInfoDao {


    private ISemanticInfoDao dao;

    public SemanticInfoDaoProxy() {
        dao = new SemanticInfoDaoImpl(MongoConn.getShopSemanticCollection());
    }


    @Override

    public Document getDocumentByName(String name) {
        return this.dao.getDocumentByName(name);
    }

    @Override
    public Document getDocumentsBySemCellId(String semCellId) {
        return this.dao.getDocumentsBySemCellId(semCellId);
    }

    @Override
    public void update(String name, String semCellId) {
        this.dao.update(name, semCellId);

    }
}
