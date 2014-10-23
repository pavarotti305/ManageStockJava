package speedymarket;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Vector;

@DatabaseTable(tableName = "marque")
public class Marque {

  @DatabaseField(id = true)  
  private Integer code_marque;
  
  @DatabaseField
  private String libelle_marque;

    /**
   * 
   * @element-type Article
   */


  public Marque() {
  }

    public Integer getCode() {
        return code_marque;
    }

    public void setCode(Integer code) {
        this.code_marque = code;
    }

    public String getLibelle() {
        return libelle_marque;
    }

    public void setLibelle(String libelle) {
        this.libelle_marque = libelle;
    }

    public String toString () { // ajouter pour l'utilisation du model de combobox entity : sert à afficher ce que l'on veut dedans
        return libelle_marque.toString();
    }

}