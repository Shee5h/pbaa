package lt.vtmc.pbaa.payload.requests;

import javax.validation.constraints.NotBlank;

public class IncomeInsertRequest {

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

    @Override
    public String toString() {
        return "IncomeInsertRequest{" +
                ", incomeName='" + incomeName + '\'' +
                ", date='" + date + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
