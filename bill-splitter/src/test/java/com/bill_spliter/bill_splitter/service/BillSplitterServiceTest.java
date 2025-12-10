package com.bill_spliter.bill_splitter.service;

import com.bill_spliter.bill_splitter.dto.BillSplitRequestDto;
import com.bill_spliter.bill_splitter.dto.ItemDto;
import com.bill_spliter.bill_splitter.dto.ParticipantDto;
import com.bill_spliter.bill_splitter.dto.BillSplitResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class BillSplitterServiceTest {

    @DisplayName("splitBill: two participants with common item and fee")
    @Test
    void splitBill_twoParticipantsWithCommonAndFee() {
        BillSplitResponseDto result = getBillSplitResponseDto();

        assertThat(result).isNotNull();
        assertThat(result.getParticipants()).hasSize(2);
        assertThat(result.getCommonItemsTotal()).isEqualByComparingTo("60.00");
        assertThat(result.getServiceFeePercent()).isEqualByComparingTo("10");

        var alice = result.getParticipants().get(0);
        var bob = result.getParticipants().get(1);

        assertThat(alice.getName()).isEqualTo("Alice");
        assertThat(alice.getTotalToPay()).isEqualByComparingTo("44.00");

        assertThat(bob.getName()).isEqualTo("Bob");
        assertThat(bob.getTotalToPay()).isEqualByComparingTo("33.00");

        assertThat(result.getGrandTotal()).isEqualByComparingTo("77.00");
    }

    private static BillSplitResponseDto getBillSplitResponseDto() {
        BillSplitRequestDto request = new BillSplitRequestDto(
                new BigDecimal("10"),
                List.of(
                        new ItemDto("Big pizza", new BigDecimal("60.00"))
                ),
                List.of(
                        new ParticipantDto(
                                "Alice",
                                List.of(new ItemDto("Pasta", new BigDecimal("10.00")))
                        ),
                        new ParticipantDto(
                                "Bob",
                                List.of()
                        )
                )
        );

        BillSplitterService billSplitterService = new BillSplitterService();

        BillSplitResponseDto result = billSplitterService.splitBill(request);
        return result;
    }

    @DisplayName("splitBill: empty participants should throw exception")
    @Test
    void splitBill_emptyParticipants_throwsException() {
        BillSplitRequestDto request = new BillSplitRequestDto(
                new BigDecimal("10"),
                List.of(),
                List.of()
        );

        BillSplitterService billSplitterService = new BillSplitterService();

        assertThrows(IllegalArgumentException.class,
                () -> billSplitterService.splitBill(request));
    }
}
