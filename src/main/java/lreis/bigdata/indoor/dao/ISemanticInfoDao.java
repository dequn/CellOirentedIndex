package lreis.bigdata.indoor.dao;

import org.bson.Document;

/**
 * Created by dq on 10/6/16.
 */
public interface ISemanticInfoDao {


    Document getDocumentByName(String name);

    Document getDocumentsBySemCellId(String semCellId);

    void update(String name, String semCellId);


}
