package com.bill_spliter.bill_splitter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ParticipantShareDto {
    private String name;
    private BigDecimal personalItemsTotal;
    private BigDecimal commonItemsShare;
    private BigDecimal serviceFee;
    private BigDecimal totalToPay;
}
