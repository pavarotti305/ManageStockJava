package speedymarket;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Vector;

@DatabaseTable(tableName = "image")
public class Image {
    
  @DatabaseField(id = true)  
  private Integer code_image;
  
  @DatabaseField
  private String url_image;

    /**
   * 
   * @element-type Article
   */


  public Image() {
  }

    public Integer getCode() {
        return code_image;
    }

    public void setCode(Integer code) {
        this.code_image = code;
    }

    public String getUrl() {
        return url_image;
    }

    public void setUrl(String url) {
        this.url_image = url;
    }


    
}