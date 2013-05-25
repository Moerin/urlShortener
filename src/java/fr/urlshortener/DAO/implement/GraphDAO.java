/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.urlshortener.DAO.implement;

import fr.urlshortener.DAO.DAO;
import fr.urlshortener.DAO.interfaces.ConnectInterface;
import fr.urlshortener.DAO.interfaces.QueryInterface;
import fr.urlshortener.bean.Data;
import fr.urlshortener.configuration.Configuration;
import fr.urlshortener.populate.Populate;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Sebastien
 */
public class GraphDAO extends DAO<Data> implements ConnectInterface, QueryInterface<Data> {

    // Path de la base de donnée
    private String dbPath;
    // Creation d'une instance Neo4j
    private GraphDatabaseService graphDb;
    // Creation d'une liste d'index
    //List<Index> indexList = new ArrayList<Index>(); TODO : verifier leur utilité
//    private static Map<String, Index> indexMap = new HashMap<String, Index>();
    private Index<Node> nodeIndex;
    // Nom de la propriete du node
    private String key; /* TODO : la valeur doit etre changé
     dans la base de donnée */
    // Nom de l'index du node
    private String index;
    // Log Slf4j
    private Logger logger = LoggerFactory.getLogger(GraphDAO.class);

    /**
     * Constructeur par defaut
     *
     * @param dbPath
     * @param key
     *
     */
    public GraphDAO(Configuration config) {
        if (!config.getPath().isEmpty()) {
            this.dbPath = config.getPath();
            try {
                File filePath = new File(dbPath);
                if (!filePath.exists()) {
                    filePath.mkdirs();
                    logger.info("Database will be created under {} path", this.dbPath);
                }
            } catch (SecurityException se) {
                logger.error("Writing is denied under {} path", this.dbPath);
                logger.info("A default database will be created under {} path", System.getenv("HOME") + File.separator + "defaultBase");
                this.dbPath = System.getenv("HOME") + File.separator + "defaultBase";
                File defaultPath = new File(dbPath);
                defaultPath.mkdirs();
            }
        } else {
            this.dbPath = config.getURI();
        }
    }

    /**
     * Initialise la base de donnée
     */
    public void start() {
        // Démarrage du serveur avec le path en propriété
        this.graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(dbPath);
        System.out.println("Base de donnée initialisé");
        // Recupere les "Fields" du java bean et contruits des index dynamiquement
        Field[] fields = Data.class.getFields();
        for (Field field : fields) {
            graphDb.index().forNodes(field.getName());
            // Si ils n'existent pas on les créent
            if (!graphDb.index().existsForNodes(field.getName())) { // TODO : a tester toutes cette partie!!
                Populate pop = new Populate(graphDb, field.getName());
                pop.populate();
            }
        }
        // Version pour valoriser un string      
        this.nodeIndex = graphDb.index().forNodes(index);
//        String[] str = graphDb.index().nodeIndexNames();
//        // Creation d'une liste d'index
//        for(String indStr : str){
//            //this.indexList.add(new GraphDatabaseFactory().newEmbeddedDatabase(dbPath).index().forNodes(indStr));
//            this.indexMap.put(indStr, new GraphDatabaseFactory().newEmbeddedDatabase(dbPath).index().forNodes(indStr));
//        }
    }

    /**
     * Ferme la base de donnée
     */
    public void stop() {
        // Fermeture de l'instance
        this.graphDb.shutdown();
        System.out.println("Fermeture de la base de donnée");
    }

    /**
     * Recupere la propriete du node
     *
     * @param id
     * @return Data
     */
    public Data find(long id) {
        Data data = new Data();

        Transaction tx = this.graphDb.beginTx();
        try {
            Node node = this.graphDb.getNodeById(id);
            data.setValue(String.valueOf(node.getProperty(key)));
            // Signale que la transaction a reussi
            tx.success();
        } catch (Exception e) { // TODO : identifier les vrai exceptions
            logger.warn("Node : find failed");
            System.err.println("Node : find failed");
            tx.failure(); // A VERIFIER SI CETTE LIGNE DE CODE EST UTILE
        } finally {
            // Cloture la transaction
            tx.finish();
        }
        return data;
    }

    public Data create(Data obj) {
        Data data = new Data();

        Transaction tx = graphDb.beginTx();
        try {
            Node newNode = graphDb.createNode();
            newNode.setProperty(key, obj.getValue());
            data.setId(newNode.getId());
            data.setValue(String.valueOf(newNode.getProperty(key)));
            nodeIndex.add(newNode, key, newNode.getProperty(key));
            tx.success();
        } catch (Exception e) { // TODO : identifier les vrai exceptions
            logger.warn("Node : Creation failed");
            System.err.println("Node : Creation failed");
            // Signale que la transaction a ete un echec
            tx.failure(); // A VERIFIER SI CETTE LIGNE DE CODE EST UTILE
        } finally {
            // Cloture la transaction
            tx.finish();
        }
        return data;
    }

    public void update(Data obj) {

        Transaction tx = graphDb.beginTx();
        try {
            Node newNode = graphDb.getNodeById(obj.getId());
            newNode.setProperty(key, obj.getValue());
            tx.success();
        } catch (Exception e) { // TODO : identifier les vrai exceptions
            logger.warn("Node : Update failed");
            System.err.println("Node : Update failed");
            // Signale que la transaction a ete un echec
            tx.failure(); // A VERIFIER SI CETTE LIGNE DE CODE EST UTILE
        } finally {
            // Cloture la transaction
            tx.finish();
        }
    }

    public void delete(Data obj) {

        Transaction tx = graphDb.beginTx();
        try {
            // Recherche du node par id et supression
            if (obj.getId() != 0) {
                Node newNode = graphDb.getNodeById(obj.getId());
                newNode.delete();
            } else {
                // recherche du node par value et suppression
                IndexHits<Node> hits = nodeIndex.get(key, obj.getValue());
                Node newNode = hits.getSingle();
                newNode.delete();
            }
            tx.success();
        } catch (Exception e) { // TODO : identifier les vrai exceptions
            logger.warn("Node : Delete failed");
            System.err.println("Node : Delete failed");
            // Signale que la transaction a ete un echec
            tx.failure(); // A VERIFIER SI CETTE LIGNE DE CODE EST UTILE
        } finally {
            // Cloture la transaction
            tx.finish();
        }
    }

    /**
     * Recherche l'id de la donnée, utilise un index pour trouver la donnée, ne
     * fonctionne pas pour l'instant sans que la base de donnée posséde un index
     *
     * @param obj1
     * @param obj2
     * @return Data
     */
    public Data querySingle(String obj1, String obj2) {
        Data data = new Data();

        Transaction tx = graphDb.beginTx();
        // C'est une chaine
        try {
            IndexHits<Node> hits = nodeIndex.get(obj1, obj2);
            Node node = hits.getSingle();
            data.setId(node.getId());
            // Signale que la transaction a reussi
            tx.success();
        } catch (Exception e) {
            logger.warn("Query : querySingle failed");
            System.err.println("Query : querySingle failed");
            // Signale que la transaction a été un echec
            tx.failure(); // A VERIFIER SI CETTE LIGNE DE CODE EST UTILE
        } finally {
            // Cloture la transaction
            tx.finish();
        }
        return data;
    }

    /**
     *
     * @param obj1
     * @param obj2
     * @return
     */
    public List<Data> queryList(String obj1, String obj2) {
        // ArrayList synchronized pour eviter les problemes en cas de multithreading 
        List<Data> listData = Collections.synchronizedList(new ArrayList<Data>());

        Transaction tx = graphDb.beginTx();

        IndexHits<Node> hits = nodeIndex.get(obj1, obj2);
        try {
            for (Node node : hits) {
                Data data = new Data();
                data.setValue((String) node.getProperty(key));
                listData.add(data);
            }
            // Signale que la transaction a reussi
            tx.success();
        } catch (Exception e) {
            logger.warn("Query : queryList failed");
            System.err.println("Query : queryList failed");
            // Signale que la transaction a été un echec
            tx.failure(); // A VERIFIER SI CETTE LIGNE DE CODE EST UTILE
        } finally {
            // Cloture la transaction
            hits.close();
            tx.finish();
        }
        return listData;
    }
    //    public void restart() { Comment cela fonctionne t'il?
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
}