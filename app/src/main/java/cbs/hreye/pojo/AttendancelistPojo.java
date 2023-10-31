package cbs.hreye.pojo;

public class AttendancelistPojo implements Comparable<AttendancelistPojo>  {

    private String IN_TIME;
    private String IN_TIME_LOCATION;
    private String OUT_TIME;
    private String OUT_TIME_LOCATION;

    public String getIN_TIME() {
        return IN_TIME;
    }

    public void setIN_TIME(String IN_TIME) {
        this.IN_TIME = IN_TIME;
    }

    public String getIN_TIME_LOCATION() {
        return IN_TIME_LOCATION;
    }

    public void setIN_TIME_LOCATION(String IN_TIME_LOCATION) {
        this.IN_TIME_LOCATION = IN_TIME_LOCATION;
    }

    public String getOUT_TIME() {
        return OUT_TIME;
    }

    public void setOUT_TIME(String OUT_TIME) {
        this.OUT_TIME = OUT_TIME;
    }

    public String getOUT_TIME_LOCATION() {
        return OUT_TIME_LOCATION;
    }

    public void setOUT_TIME_LOCATION(String OUT_TIME_LOCATION) {
        this.OUT_TIME_LOCATION = OUT_TIME_LOCATION;
    }

    @Override
    public int compareTo(AttendancelistPojo attendancelistPojo) {
        return getIN_TIME().compareTo(attendancelistPojo.getIN_TIME());
    }
}
