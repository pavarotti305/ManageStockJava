package speedymarket;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Vector;


@DatabaseTable(tableName = "type_tva")
public class TVA{

  @DatabaseField(id = true) 
  private Integer code_tva;
  
  @DatabaseField
  private Double taux_tva;

    /**
   * 
   * @element-type Article
   */


  public TVA() {
  }

    public Integer getCode() {
        return code_tva;
    }

    public void setCode(Integer code) {
        this.code_tva = code;
    }

    public Double getTaux() {
        return taux_tva;
    }

    public void setTaux(Double taux) {
        this.taux_tva = taux;
    }

        
    public String toString () { // ajouter pour l'utilisation du model de combobox entity : sert à afficher ce que l'on veut dedans
        return taux_tva.toString();
    }

}