package com.project.leee.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicationDTO {
    private Integer medicineId;
    private Integer uId;


    @JsonProperty("pillAlarmDetail")
    private String medicineName;

    @JsonProperty("medicineDate")
    private String medicationDate;

    @JsonProperty("hourTime")
    private Integer hourTime;

    @JsonProperty("minTime")
    private Integer minTime;

    @JsonProperty("addMemo")
    private String medicineMemo;

    @JsonProperty("startDay")
    private String startDay;

    @JsonProperty("endDay")
    private String endDay;

}
