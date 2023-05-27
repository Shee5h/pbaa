package lt.vtmc.pbaa.payload.responses;

public class IncomeResponse {

    private String incomeId;

    private String incomeName;

    private String date;

    private String amount;

    public IncomeResponse() {
    }

    public IncomeResponse(String incomeId, String incomeName, String date, String amount) {
        this.incomeId = incomeId;
        this.incomeName = incomeName;
        this.date = date;
        this.amount = amount;
    }

    public String getIncomeId() {
        return incomeId;
    }

    public void setIncomeId(String incomeId) {
        this.incomeId = incomeId;
    }

    public String getIncomeName() {
        return incomeName;
    }

    public void setIncomeName(String incomeName) {
        this.incomeName = incomeName;
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
        return "IncomeResponse{" +
                "incomeId='" + incomeId + '\'' +
                ", incomeName='" + incomeName + '\'' +
                ", date='" + date + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
