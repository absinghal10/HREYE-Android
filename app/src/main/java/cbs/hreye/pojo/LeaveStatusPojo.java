package cbs.hreye.pojo;

public class LeaveStatusPojo{

    private String Availed;
    private String Balance;
    private String Entitled;
    private String Leave_type;
    private String Leave_Decs;
    private String Opening_bal;



    public String getAvailed() {
        return Availed;
    }

    public void setAvailed(String availed) {
        Availed = availed;
    }

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }

    public String getEntitled() {
        return Entitled;
    }

    public void setEntitled(String entitled) {
        Entitled = entitled;
    }

    public String getLeave_type() {
        return Leave_type;
    }

    public void setLeave_type(String leave_type) {
        Leave_type = leave_type;
    }

    public String getOpening_bal() {
        return Opening_bal;
    }

    public void setOpening_bal(String opening_bal) {
        Opening_bal = opening_bal;
    }


    public String getLeave_Decs() {
        return Leave_Decs;
    }

    public void setLeave_Decs(String leave_Decs) {
        Leave_Decs = leave_Decs;
    }

    @Override
    public String toString() {
        return Leave_Decs;
    }

}
