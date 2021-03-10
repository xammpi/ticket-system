package md.support.support.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "request")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "date_created")
    @Temporal(TemporalType.DATE)
    private Date dateCreated = Calendar.getInstance().getTime();

    private int state;

    @NotBlank(message = "Укажите Фамилию и Имя")
    private String name;

    @NotBlank(message = "Укажите контактный телефон")
    private String phone;

    @NotBlank(message = "Опишите проблему")
    @Column(length = 2000)
    private String message;

    @Transient
    private String dateSort;

    public String getDateSort() {
        return dateSort;
    }

    public void setDateSort(String dateSort) {
        this.dateSort = dateSort;
    }

    @NotBlank(message = "Выберите магазин из списка")
    private String shop;

    @NotBlank(message = "Выберите проблему из списка")
    private String problem;

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Request() {

    }

    public Request(@NotBlank(message = "Выберите магазин из списка") String shop) {
        this.shop = shop;
    }

    public Request(String name, String phone, String message, String shop, String problem, Date date) {
        this.name = name;
        this.phone = phone;
        this.message = message;
        this.shop = shop;
        this.problem = problem;
        this.state = 0;
        this.dateCreated = date;
    }
}
