package lt.vtmc.pbaa.models;

import java.math.BigDecimal;

public class ExpenseUnitStatistic {

    private ExpensesCategory category;
    private BigDecimal amount;
    private BigDecimal limit;

    public ExpenseUnitStatistic(ExpensesCategory category, BigDecimal amount, BigDecimal limit) {
        this.category = category;
        this.amount = amount;
        this.limit = limit;
    }

    public ExpenseUnitStatistic() {
    }

    public ExpensesCategory getCategory() {
        return category;
    }

    public void setCategory(ExpensesCategory category) {
        this.category = category;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "ExpenseUnitStatistic{" +
                "category=" + category +
                ", amount=" + amount +
                ", limit=" + limit +
                '}';
    }
}
