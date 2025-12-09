package com.bill_spliter.bill_splitter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillSplitRequestDto {

    // Процент комиссии (напр. 10 = 10%)
    private BigDecimal serviceFeePercent;

    // Общие блюда (делятся поровну)
    private List<ItemDto> commonItems;

    // Участники и их личные блюда
    private List<ParticipantDto> participants;
}
