package lt.vtmc.pbaa.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;


@Entity
@Table(name="limits")
public class ExpenseLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "categories_id")
    private ExpensesCategory expensesCategory;

    @NotBlank
    private BigDecimal amount;

    public ExpenseLimit() {
    }

    public ExpenseLimit(User user, ExpensesCategory expensesCategory, BigDecimal amount) {
        this.user = user;
        this.expensesCategory = expensesCategory;
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
