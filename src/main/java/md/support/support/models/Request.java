package md.support.support.models;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "request")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_created")
    @Temporal(TemporalType.DATE)
    private Date dateCreated = Calendar.getInstance().getTime();

    @Column(name = "date_execution")
    @Temporal(TemporalType.DATE)
    private Date execution;

    @Column(name = "date_end")
    @Temporal(TemporalType.DATE)
    private Date dateEnd;

    @Column(name = "date_close")
    @Temporal(TemporalType.DATE)
    private Date dateClose;

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

    @ManyToMany()
    @JoinTable(name = "worker_request", joinColumns = {@JoinColumn(name = "request_id")},
            inverseJoinColumns = {@JoinColumn(name = "worker_id")})
    private List<Worker> worker;

    @NotBlank(message = "Выберите магазин из списка")
    private String shop;

    @ManyToOne
    @JoinColumn(name = "problem")
    private Problem problem;

    @Column(name = "problem_department_id")
    private Long departmentId;

    @Value("")
    @JoinColumn(name = "comment")
    private String comment;

    public Date getDateClose() {
        return dateClose;
    }

    public void setDateClose(Date dateClose) {
        this.dateClose = dateClose;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public List<Worker> getWorker() {
        return worker;
    }

    public Date getDateEnd() {

        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Date getExecution() {
        return execution;
    }

    public void setExecution(Date execution) {
        this.execution = execution;
    }

    public void setWorker(List<Worker> worker) {
        this.worker = worker;
    }

    public String getDateSort() {
        return dateSort;
    }

    public void setDateSort(String dateSort) {
        this.dateSort = dateSort;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

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

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
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

    public Request(String name, String phone, String message, String shop
            , Problem problem, Date date, Date execution
            , Date dateClose, Date dateEnd, String comment) {
        this.name = name;
        this.phone = phone;
        this.message = message;
        this.shop = shop;
        this.problem = problem;
        this.state = 0;
        this.dateCreated = date;
        this.dateClose = dateClose;
        this.execution = execution;
        this.comment = comment;
    }
}
