package cbs.hreye.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CustomerResult {

    @SerializedName("ADetail")
    List<ProjectPojo> projectList;


    public CustomerResult(List<ProjectPojo> projectList) {
        this.projectList = projectList;
    }

    public List<ProjectPojo> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<ProjectPojo> projectList) {
        this.projectList = projectList;
    }
}


