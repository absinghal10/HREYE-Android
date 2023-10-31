package cbs.hreye.pojo;

public class ActivitiesPojo{
    private String DOC_STATUS;
    private String REPORTING_DATE;
    private String TRANSACTION_NO;

    public ActivitiesPojo(String TRANSACTION_NO, String REPORTING_DATE,String DOC_STATUS) {
        // TODO Auto-generated constructor stub
        this.TRANSACTION_NO = TRANSACTION_NO;
        this.REPORTING_DATE = REPORTING_DATE;
        this.DOC_STATUS = DOC_STATUS;
    }

    public ActivitiesPojo() {
    }

    public String getDOC_STATUS() {
        return DOC_STATUS;
    }

    public void setDOC_STATUS(String DOC_STATUS) {
        this.DOC_STATUS = DOC_STATUS;
    }

    public String getREPORTING_DATE() {
        return REPORTING_DATE;
    }

    public void setREPORTING_DATE(String REPORTING_DATE) {
        this.REPORTING_DATE = REPORTING_DATE;
    }

    public String getTRANSACTION_NO() {
        return TRANSACTION_NO;
    }

    public void setTRANSACTION_NO(String TRANSACTION_NO) {
        this.TRANSACTION_NO = TRANSACTION_NO;
    }
}
