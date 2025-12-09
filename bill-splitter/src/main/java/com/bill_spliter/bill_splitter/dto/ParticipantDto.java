package com.bill_spliter.bill_splitter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantDto {
    private String name;
    private List<ItemDto> items;
}
