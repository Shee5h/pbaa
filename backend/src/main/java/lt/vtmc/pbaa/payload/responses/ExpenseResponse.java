package lt.vtmc.pbaa.payload.responses;

public class ExpenseResponse {

    private Long expenseId;

    private String expenseName;

    private Long categoryId;

    private String date;

    private String amount;

    public ExpenseResponse() {
    }

    public ExpenseResponse(Long expenseId, String expenseName, Long categoryId, String date, String amount) {
        this.expenseId = expenseId;
        this.expenseName = expenseName;
        this.categoryId = categoryId;
        this.date = date;
        this.amount = amount;
    }

    public Long getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(Long expenseId) {
        this.expenseId = expenseId;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "ExpenseResponse{" +
                "expenseId='" + expenseId + '\'' +
                ", expenseName='" + expenseName + '\'' +
                ", expenseCategory='" + categoryId + '\'' +
                ", date='" + date + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
