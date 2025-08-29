
package hibernate;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "product")
public class Product implements Serializable{
    
    @Id
    @Column(name = "id")
    private int id;
    
     @Column(name = "title" , nullable = false)
     private String title;
     
     @Column(name = "description" , nullable = false)
     private String description;
     
     @ManyToOne
     @JoinColumn(name = "category_id")
     private Category category;
     
     @ManyToOne
     @JoinColumn(name = "item_condition_id")
     private Condition condition;
     
     @ManyToOne
     @JoinColumn(name = "item_availability_id")
     private Avilability availability;
     
      public Product() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Avilability getAvailability() {
        return availability;
    }

    public void setAvailability(Avilability availability) {
        this.availability = availability;
    }
}
