package net.plang.HoWooAccount.base.to;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class DetailCodeBean extends BaseBean {
    private String divisionCodeNo;
    private String detailCode;
    private String detailCodeName;
    private String description;

    public String getDivisionCodeNo() {
        return divisionCodeNo;
    }

    public void setDivisionCodeNo(String divisionCodeNo) {
        this.divisionCodeNo = divisionCodeNo;
    }

    public String getDetailCode() {
        return detailCode;
    }

    public void setDetailCode(String detailCode) {
        this.detailCode = detailCode;
    }

    public String getDetailCodeName() {
        return detailCodeName;
    }

    public void setDetailCodeName(String detailCodeName) {
        this.detailCodeName = detailCodeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
