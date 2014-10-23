package speedymarket;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;



@DatabaseTable(tableName = "article")
public class Article {
    public static final String MARQUE_FIELD_NAME = "code_marque";
    public static final String TVA_FIELD_NAME = "code_tva";
    public static final String CATEGORIE_FIELD_NAME = "code_categorie";
    public static final String IMAGE_FIELD_NAME = "code_image";
    
    @DatabaseField(id = true)
    private Integer code_article;
    
    @DatabaseField
    private String libelle_article;
    
    @DatabaseField
    private String description_courte_article;
    
    @DatabaseField
    private String description_longue_article;
    
    @DatabaseField
    private Integer quantite_article_en_stock;
    
    @DatabaseField
    private Double prix_ht_article;
    
    @DatabaseField
    private Boolean visibilite_article;
    
    @DatabaseField(foreign = true, columnName = MARQUE_FIELD_NAME)
    public Marque marque;
    
    @DatabaseField(foreign = true, columnName = TVA_FIELD_NAME)
    public TVA tva;
    
    @DatabaseField(foreign = true, columnName = CATEGORIE_FIELD_NAME)
    public Categorie categorie;
    
    @DatabaseField(foreign = true, columnName = IMAGE_FIELD_NAME)
    public Image image;

    public Article() {
    }

    public Integer getCode() {
        return code_article;
    }

    public void setCode(Integer code) {
        this.code_article = code;
    }

    public String getLibelle() {
        return libelle_article;
    }

    public void setLibelle(String libelle) {
        this.libelle_article = libelle;
    }

    public String getDescriptionCourte() {
        return description_courte_article;
    }

    public void setDescriptionCourte(String descriptionCourte) {
        this.description_courte_article = descriptionCourte;
    }

    public String getDescriptionLongue() {
        return description_longue_article;
    }

    public void setDescriptionLongue(String descriptionLongue) {
        this.description_longue_article = descriptionLongue;
    }

    public Integer getQuantite() {
        return quantite_article_en_stock;
    }

    public void setQuantite(Integer quantite) {
        this.quantite_article_en_stock = quantite;
    }

    public Double getPrixHT() {
        return prix_ht_article;
    }

    public void setPrixHT(Double prixHT) {
        this.prix_ht_article = prixHT;
    }

    public Boolean isVisibilite() {
        return visibilite_article;
    }

    public void setVisibilite(Boolean visibilite) {
        this.visibilite_article = visibilite;
    }

    public Marque getMarque() {
        return marque;
    }

    public void setMarque(Marque myMarque) {
        this.marque = myMarque;
    }

    public TVA getTVA() {
        return tva;
    }

    public void setTVA(TVA myTVA) {
        this.tva = myTVA;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie myCategorie) {
        this.categorie = myCategorie;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image myImage) {
        this.image = myImage;
    }

}
