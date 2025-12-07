package com.bill_spliter.bill_splitter.service;

import com.bill_spliter.bill_splitter.dto.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class BillSplitterService {

    public BillSplitResponseDto splitBill(BillSplitRequestDto request) {
        List<ParticipantDto> participants =
                request.getParticipants() != null ? request.getParticipants() : Collections.emptyList();

        if (participants.isEmpty()) {
            throw new IllegalArgumentException("Participants list cannot be empty");
        }

        BigDecimal serviceFeePercent =
                request.getServiceFeePercent() != null ? request.getServiceFeePercent() : BigDecimal.ZERO;

        List<ItemDto> commonItems =
                request.getCommonItems() != null ? request.getCommonItems() : Collections.emptyList();

        BigDecimal commonTotal = commonItems.stream()
                .map(ItemDto::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int peopleCount = participants.size();

        BigDecimal commonPerPerson = commonTotal
                .divide(BigDecimal.valueOf(peopleCount), 2, RoundingMode.HALF_UP);

        List<ParticipantShareDto> shares = new ArrayList<>();
        BigDecimal grandTotal = BigDecimal.ZERO;

        for (ParticipantDto participant : participants) {
            List<ItemDto> personalItems =
                    participant.getItems() != null ? participant.getItems() : Collections.emptyList();

            BigDecimal personalTotal = personalItems.stream()
                    .map(ItemDto::getPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal subtotal = personalTotal.add(commonPerPerson);

            BigDecimal fee = subtotal
                    .multiply(serviceFeePercent)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            BigDecimal total = subtotal.add(fee);

            grandTotal = grandTotal.add(total);

            shares.add(new ParticipantShareDto(
                    participant.getName(),
                    personalTotal,
                    commonPerPerson,
                    fee,
                    total
            ));
        }

        return new BillSplitResponseDto(
                shares,
                grandTotal,
                commonTotal,
                serviceFeePercent
        );
    }
}
