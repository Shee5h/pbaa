package lt.vtmc.pbaa.payload.requests;

import javax.validation.constraints.NotBlank;

public class ExpenseUpdateRequest {

    @NotBlank
    private Long expenseId;

    @NotBlank
    private String expenseName;

    @NotBlank
    private Long categoryId;

    @NotBlank
    private String date;

    @NotBlank
    private String amount;

    public String getExpenseName() {
        return expenseName;
    }

    public String getDate() {
        return date;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getAmount() {
        return amount;
    }

    public Long getExpenseId() {
        return expenseId;
    }

    @Override
    public String toString() {
        return "ExpenseUpdateRequest{" +
                "expenseId='" + expenseId + '\'' +
                ", expenseName='" + expenseName + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", date='" + date + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
