package cbs.hreye.pojo;

import com.google.gson.annotations.SerializedName;

public class AssociateRootDataModel {


    @SerializedName("AssosciateResult")
    AssociateResult associateResult;

    public AssociateResult getAssociateResult() {
        return associateResult;
    }

    public void setAssociateResult(AssociateResult associateResult) {
        this.associateResult = associateResult;
    }


}
