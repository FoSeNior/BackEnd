package com.inje.forseni.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MyHospitalRequestDTO {
    private String date;
    private int hourTime;
    private int minTime;
    private String hospitalAlarmDetail;
    private String addMemo;
    private int userId;
    private int hospitalId;
}
