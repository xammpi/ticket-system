package md.support.support.models;

import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;

@Entity
@Table(name = "shop")
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(name = "count_request_completed")
    @Value("0")
    private int count;

    @Column(name = "shop_number")
    private int number;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Shop() {

    }

    public Shop(Long id, String name, int count, int number) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.number = number;
    }

    @Override
    public String toString() {
        return name;
    }
}
