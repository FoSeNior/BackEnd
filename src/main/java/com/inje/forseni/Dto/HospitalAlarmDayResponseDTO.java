package com.inje.forseni.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class HospitalAlarmDayResponseDTO {
    private boolean success;
    private String message;
    private Data data;

    @Getter
    @AllArgsConstructor
    public static class Data {
        private int dailyHospitalAlarmCount;
        private List<HospitalAlarmResponseDTO> alarms;
    }
}
