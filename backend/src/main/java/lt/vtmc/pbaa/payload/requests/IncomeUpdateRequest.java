package lt.vtmc.pbaa.payload.requests;

import javax.validation.constraints.NotBlank;


public class IncomeUpdateRequest {

    @NotBlank
    private String incomeId;

    @NotBlank
    private String incomeName;

    @NotBlank
    private String date;

    @NotBlank
    private String amount;

    public String getIncomeName() {
        return incomeName;
    }

    public String getDate() {
        return date;
    }


    public String getAmount() {
        return amount;
    }

    public String getIncomeId() {
        return incomeId;
    }

    @Override
    public String toString() {
        return "IncomeUpdateRequest{" +
                "incomeId='" + incomeId + '\'' +
                ", incomeName='" + incomeName + '\'' +
                ", date='" + date + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
