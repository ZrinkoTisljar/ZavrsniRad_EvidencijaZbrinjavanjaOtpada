package com.example.wasteapp.report.dto;


import com.example.wasteapp.workorder.WorkOrderStatus;

/**
 * DTO KLASA: WorkOrderStatusReportRow
 * SVRHA: Jedan red izvješća "broj naloga po statusu".
 * */
public class WorkOrderStatusReportRow {

    private WorkOrderStatus status;
    private Long totalCount;

    public WorkOrderStatusReportRow(WorkOrderStatus status, Long totalCount) {
        this.status = status;
        this.totalCount = totalCount;
    }

    public WorkOrderStatus getStatus() {
        return status;
    }

    public Long getTotalCount() {
        return totalCount;
    }

}
