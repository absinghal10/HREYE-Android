package cbs.hreye.pojo;

import com.google.gson.annotations.SerializedName;

public class ProjectResponseRootDataModel {
    @SerializedName("CoustomerResult")
    CustomerResult CoustomerResult;

    public void setCoustomerResult(CustomerResult CoustomerResult) {
        this.CoustomerResult = CoustomerResult;
    }

    public CustomerResult getCoustomerResult() {
        return CoustomerResult;
    }

}
