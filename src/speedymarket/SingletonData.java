package speedymarket;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;

public class SingletonData {

    private String url;
    private String utilisateur;
    private String mdp;
    private static SingletonData objetSingletonData;
    private ConnectionSource connexion;
    private Dao<Article, Integer> articleDAO;
    private Dao<Marque, Integer> marqueDAO;
    private Dao<TVA, Integer> TvaDAO;
    private Dao<Categorie, Integer> CategorieDAO;
    private Dao<Image, Integer> ImageDAO;

    private SingletonData() throws SQLException {
        url = "jdbc:mysql://192.168.29.10/speedymarketclass";
        connexion = new JdbcConnectionSource(url);
        ((JdbcConnectionSource) connexion).setUsername("root");
        ((JdbcConnectionSource) connexion).setPassword("Materobi");
        articleDAO = DaoManager.createDao(connexion, Article.class);
        marqueDAO = DaoManager.createDao(connexion, Marque.class);
        TvaDAO = DaoManager.createDao(connexion, TVA.class);
        CategorieDAO = DaoManager.createDao(connexion, Categorie.class);
        ImageDAO = DaoManager.createDao(connexion, Image.class);
    }

    public static SingletonData getSingletonData() throws SQLException {  //permet de creer un objet singleton pour se connecter et le creer si il n'existe pas
        if(objetSingletonData == null)
            objetSingletonData = new SingletonData();
        return objetSingletonData;
    }

    public Dao<Article, Integer> getArticleDAO() throws SQLException {
        return objetSingletonData.articleDAO;
    }

    public Dao<Marque, Integer> getMarqueDAO() {
        return objetSingletonData.marqueDAO;
    }

    public Dao<TVA, Integer> getTvaDAO() {
        return objetSingletonData.TvaDAO;
    }

    public Dao<Categorie, Integer> getCategorieDAO() {
        return objetSingletonData.CategorieDAO;
    }

    public Dao<Image, Integer> getImageDAO() {
        return objetSingletonData.ImageDAO;
    }

}
