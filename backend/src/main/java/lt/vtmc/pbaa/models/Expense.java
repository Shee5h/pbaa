package lt.vtmc.pbaa.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "categories_id")
    private ExpensesCategory expensesCategory;

    @NotBlank
    private String expenseName;

    @NotBlank
    private LocalDate date;

    @NotBlank
    private BigDecimal amount;

    public Expense() {
    }

    public Expense(User user, ExpensesCategory expensesCategory, String expenseName, LocalDate date, BigDecimal amount) {
        this.user = user;
        this.expensesCategory = expensesCategory;
        this.expenseName = expenseName;
        this.date = date;
        this.amount = amount;
    }

    public void setExpensesCategory(ExpensesCategory expensesCategory) {
        this.expensesCategory = expensesCategory;
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public ExpensesCategory getExpensesCategory() {
        return expensesCategory;
    }
}
