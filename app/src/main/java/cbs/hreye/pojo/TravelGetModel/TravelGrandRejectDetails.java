package cbs.hreye.pojo.TravelGetModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TravelGrandRejectDetails {
        @SerializedName("GrantRejectResult")
        @Expose
        private GrantRejectResult grantRejectResult;

        public GrantRejectResult getGrantRejectResult() {
            return grantRejectResult;
        }

        public void setGrantRejectResult(GrantRejectResult grantRejectResult) {
            this.grantRejectResult = grantRejectResult;
        }
}
