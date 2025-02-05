package com.inje.forseni.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HospitalAlarmResponseDTO {
    private int hospitalAlarmId;
    private String date;
    private int hourTime;
    private int minTime;
    private String hospitalName;
    private String hospitalAlarmDetail;
    private String addMemo;
}
