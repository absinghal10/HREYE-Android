package cbs.hreye.pojo;

public class LeaveListPojo {

    private String Appl_No;
    private String Emp_Code;
    private String FROM_DATE;
    private String TO_DATE;
    private String Leave_type;
    private String Leave_Decs;
    private String To_session;
    private String from_session;
    private String status;
    private String employee_reason;
    private String notified_date;

    public String getNotified_date() {
        return notified_date;
    }

    public void setNotified_date(String notified_date) {
        this.notified_date = notified_date;
    }

    public String getTo_session() {
        return To_session;
    }

    public void setTo_session(String to_session) {
        To_session = to_session;
    }

    public String getFrom_session() {
        return from_session;
    }

    public void setFrom_session(String from_session) {
        this.from_session = from_session;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmployee_reason() {
        return employee_reason;
    }

    public void setEmployee_reason(String employee_reason) {
        this.employee_reason = employee_reason;
    }

    public String getAppl_No() {
        return Appl_No;
    }

    public void setAppl_No(String appl_No) {
        Appl_No = appl_No;
    }

    public String getEmp_Code() {
        return Emp_Code;
    }

    public void setEmp_Code(String emp_Code) {
        Emp_Code = emp_Code;
    }

    public String getFROM_DATE() {
        return FROM_DATE;
    }

    public void setFROM_DATE(String FROM_DATE) {
        this.FROM_DATE = FROM_DATE;
    }

    public String getTO_DATE() {
        return TO_DATE;
    }

    public void setTO_DATE(String TO_DATE) {
        this.TO_DATE = TO_DATE;
    }

    public String getLeave_type() {
        return Leave_type;
    }

    public void setLeave_type(String leave_type) {
        Leave_type = leave_type;
    }

    public String getLeave_Decs() {
        return Leave_Decs;
    }

    public void setLeave_Decs(String leave_Decs) {
        Leave_Decs = leave_Decs;
    }
}
