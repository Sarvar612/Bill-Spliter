package com.bill_spliter.bill_splitter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class BillSplitResponseDto {
    private List<ParticipantShareDto> participants;
    private BigDecimal grandTotal;
    private BigDecimal commonItemsTotal;
    private BigDecimal serviceFeePercent;
}
