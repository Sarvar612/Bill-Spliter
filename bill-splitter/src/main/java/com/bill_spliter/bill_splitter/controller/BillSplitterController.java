package com.bill_spliter.bill_splitter.controller;

import com.bill_spliter.bill_splitter.dto.*;
import com.bill_spliter.bill_splitter.service.BillSplitterService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillSplitterController {

    private final BillSplitterService billSplitterService;

    public BillSplitterController(BillSplitterService billSplitterService) {
        this.billSplitterService = billSplitterService;
    }

    @PostMapping("/split")
    public BillSplitResponseDto splitBill(@RequestBody BillSplitRequestDto request) {
        return billSplitterService.splitBill(request);
    }


    @GetMapping("/demo")
    public BillSplitResponseDto demo() {
        BillSplitRequestDto request = new BillSplitRequestDto(
                new BigDecimal("10"), // 10% комиссия
                List.of( // общие блюда
                        new ItemDto("Big pizza", new BigDecimal("60.00"))
                ),
                List.of( // участники
                        new ParticipantDto(
                                "Alice",
                                List.of(
                                        new ItemDto("Pasta", new BigDecimal("10.00"))
                                )
                        ),
                        new ParticipantDto(
                                "Bob",
                                List.of()
                        )
                )
        );

        return billSplitterService.splitBill(request);
    }
}

