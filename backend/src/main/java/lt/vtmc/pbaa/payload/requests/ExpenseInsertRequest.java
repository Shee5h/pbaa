package lt.vtmc.pbaa.payload.requests;

import javax.validation.constraints.NotBlank;

public class ExpenseInsertRequest {

    @NotBlank
    private String expenseName;

    @NotBlank
    private Long categoryId;

    @NotBlank
    private String date;

    @NotBlank
    private String amount;

    public ExpenseInsertRequest() {
    }

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

    @Override
    public String toString() {
        return "ExpenseInsertRequest{" +
                "expenseName='" + expenseName + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", date='" + date + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
