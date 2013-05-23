/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.urlshortener.DAO.implement;

import fr.urlshortener.DAO.DAO;
import fr.urlshortener.DAO.pattern.ConnectInterface;
import fr.urlshortener.DAO.pattern.QueryInterface;
import fr.urlshortener.bean.Data;
import java.util.Set;

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
    private GraphDatabaseService graphDb = null;
    // Creation de l'index de node
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
     * @param index
     */
    public GraphDAO(final String dbPath, final String key) {
        this.dbPath = dbPath;
        this.key = key;
    }

    /**
     * Constructeur avec Index
     *
     * @param dbPath
     * @param key
     * @param index
     */
    public GraphDAO(final String dbPath, final String key, final String index) {
        this.dbPath = dbPath;
        this.key = key;
        this.index = index;
    }

    /**
     * Recupere la propriete du node
     *
     * @param id
     * @return Data
     */
    public Data find(long id) {
        Data data = new Data();

        Transaction tx = graphDb.beginTx();
        try {
            Node node = graphDb.getNodeById(id);
            data.setValue(String.valueOf(node.getProperty(key)));
            // Signale que la transaction a reussi
            tx.success();
        } catch (Exception e) {
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
        } catch (Exception e) {
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
        } catch (Exception e) {
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
        } catch (Exception e) {
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
     * Initialise la base de donnée
     */
    public void start() {
        // Démarrage du serveur avec le path en propriété
        this.graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(dbPath);
        System.out.println("Base de donnée initialisé");
        // Recuperation d'un node index avec "nodes" comme nom (deja present dans la base de donnée)
        this.nodeIndex = graphDb.index().forNodes(index);
        
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
     * Recherche l'id de la donnée, utilise un index pour trouver la donnée ne
     * fonctionne pas pour l'instant sans que la base de donnée posséde un index
     *
     * @param obj1
     * @param obj2
     * @return Data
     */
    public Data querySingle(Object obj1, Object obj2) {
       Data data = new Data();

        Transaction tx = graphDb.beginTx();
        // C'est une chaine
        try {
            IndexHits<Node> hits = nodeIndex.get((String) obj1, (String) obj2);
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

    public Set<Data> querySet(Object obj1, Object obj2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

//    public void restart() { Comment cela fonctionne t'il?
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
    
}