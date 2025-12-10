package com.bill_spliter.bill_splitter.controller;

import com.bill_spliter.bill_splitter.dto.BillSplitRequestDto;
import com.bill_spliter.bill_splitter.dto.BillSplitResponseDto;
import com.bill_spliter.bill_splitter.dto.ParticipantShareDto;
import com.bill_spliter.bill_splitter.service.BillSplitterService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BillSplitterControllerTest {

    @Mock
    BillSplitterService billSplitterService;

    @InjectMocks
    BillSplitterController billSplitterController;

    @DisplayName("splitBill: controller returns response from service")
    @Test
    void splitBill_returnsServiceResponse() {
        BillSplitRequestDto request = new BillSplitRequestDto();

        BillSplitResponseDto serviceResponse = new BillSplitResponseDto(
                List.of(
                        new ParticipantShareDto(
                                "Alice",
                                new BigDecimal("10.00"),
                                new BigDecimal("20.00"),
                                new BigDecimal("3.00"),
                                new BigDecimal("33.00")
                        )
                ),
                new BigDecimal("33.00"),
                new BigDecimal("20.00"),
                new BigDecimal("10")
        );

        when(billSplitterService.splitBill(request))
                .thenReturn(serviceResponse);

        BillSplitResponseDto result = billSplitterController.splitBill(request);

        assertThat(result)
                .isNotNull()
                .isEqualTo(serviceResponse);

        verify(billSplitterService, times(1)).splitBill(request);
        verifyNoMoreInteractions(billSplitterService);
    }
}
