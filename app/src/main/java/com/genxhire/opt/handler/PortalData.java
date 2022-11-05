package com.genxhire.opt.handler;

public class PortalData {

    String portalId, portalAccountId, otp;


    public PortalData() {
    }

    public String getPortalId() {
        return portalId;
    }

    public void setPortalId(String portalId) {
        this.portalId = portalId;
    }

    public PortalData(String portalID, String portalAccountId, String otp) {
        this.portalId = portalID;
        this.portalAccountId = portalAccountId;
        this.otp = otp;
    }

    public String getPortalAccountId() {
        return portalAccountId;
    }

    public void setPortalAccountId(String portalAccountId) {
        this.portalAccountId = portalAccountId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
