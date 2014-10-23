package speedymarket;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "categorie")
public class Categorie {
    
    public static final String CATEGORIEPARENTE_FIELD_NAME = "code_categorie_parente";
    public static final String LIBELLE_CATEGORIE_FIELD_NAME = "libelle_categorie";
    
    @DatabaseField(id = true)
    private Integer code_categorie;
    
    @DatabaseField
    private String libelle_categorie;
    
    @DatabaseField(foreign = true, columnName = CATEGORIEPARENTE_FIELD_NAME)
    private Categorie code_categorie_parente;

    public Categorie() {
    }

    public Integer getCode() {
        return code_categorie;
    }

    public void setCode(Integer code) {
        this.code_categorie = code;
    }

    public String getLibelle() {
        return libelle_categorie;
    }

    public void setLibelle(String libelle) {
        this.libelle_categorie = libelle;
    }

    public Categorie getCategorieParente() {
        return code_categorie_parente;
    }

    public void setCategorieParente(Categorie CategorieParente) {
        this.code_categorie_parente = CategorieParente;
    }

        
    public String toString () { // ajouter pour l'utilisation du model de combobox entity : sert à afficher ce que l'on veut dedans
        return libelle_categorie.toString();
    }
  


}