package lreis.bigdata.indoor.dao.impl;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import lreis.bigdata.indoor.dao.ISemanticInfoDao;
import org.bson.Document;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.*;

/**
 * Created by dq on 10/6/16.
 */
public class SemanticInfoDaoImpl implements ISemanticInfoDao {

    public static final String SEM_CELL_ID_FIELD = "sem_cell";


    private MongoCollection collection;

    public SemanticInfoDaoImpl(MongoCollection collection) {
        this.collection = collection;
    }


    @Override

    public Document getDocumentByName(String name) {


        FindIterable iterable = collection.find((or(regex("name", name, "i"), regex("name_cn", name, "i"), regex("name_en", name, "i"))));

        final Document[] doc = {null};
        iterable.forEach(new Block<Document>() {

            @Override
            public void apply(Document document) {
                doc[0] = document;
            }
        });

        return doc[0];
    }

    @Override
    public Document getDocumentsBySemCellId(String semCellId) {
        FindIterable iterable = collection.find(eq("sem_cell", semCellId));

        final Document[] doc = {null};

        iterable.forEach(new Block<Document>() {

            @Override
            public void apply(Document document) {
                doc[0] = document;
            }
        });

        return doc[0];
    }

    @Override
    public void update(String name, String semCellId) {

        Bson query = or(regex("name", name, "i"), regex("name_cn", name, "i"), regex("name_en", name, "i"));

        Bson up = new Document("$set", new Document(SEM_CELL_ID_FIELD, semCellId));
        collection.updateOne(query, up);

    }
}
