package com.bill_spliter.bill_splitter.service;
import com.bill_spliter.bill_splitter.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BillSplitterServiceTest {
    private BillSplitterService service;
    @BeforeEach
    void setUp() {
        service = new BillSplitterService();
    }
    @Test
    void splitBill_basic() {
        BillSplitRequestDto request = new BillSplitRequestDto(
                new BigDecimal("10"), // 10% fee
                List.of(
                        new ItemDto("Big pizza", new BigDecimal("60.00")) // common
                ),
                List.of(
                        new ParticipantDto(
                                "Alice",
                                List.of(
                                        new ItemDto("Pasta", new BigDecimal("10.00")),
                                        new ItemDto("Juice", new BigDecimal("5.00"))
                                )
                        ),
                        new ParticipantDto(
                                "Bob",
                                List.of(
                                        new ItemDto("Burger", new BigDecimal("20.00"))
                                )
                        ),
                        new ParticipantDto(
                                "Charlie",
                                List.of()
                        )
                )
        );

        BillSplitResponseDto response = service.splitBill(request);
        ParticipantShareDto alice = response.getParticipants().get(0);
        assertEquals("Alice", alice.getName());
        assertEquals(new BigDecimal("15.00"), alice.getPersonalItemsTotal());
        assertEquals(new BigDecimal("20.00"), alice.getCommonItemsShare());
        assertEquals(new BigDecimal("3.50"), alice.getServiceFee());
        assertEquals(new BigDecimal("38.50"), alice.getTotalToPay());

        ParticipantShareDto bob = response.getParticipants().get(1);
        assertEquals(new BigDecimal("20.00"), bob.getPersonalItemsTotal());
        assertEquals(new BigDecimal("20.00"), bob.getCommonItemsShare());
        assertEquals(new BigDecimal("4.00"), bob.getServiceFee());
        assertEquals(new BigDecimal("44.00"), bob.getTotalToPay());

        ParticipantShareDto charlie = response.getParticipants().get(2);
        assertEquals(new BigDecimal("0.00"), charlie.getPersonalItemsTotal());
        assertEquals(new BigDecimal("20.00"), charlie.getCommonItemsShare());
        assertEquals(new BigDecimal("2.00"), charlie.getServiceFee());
        assertEquals(new BigDecimal("22.00"), charlie.getTotalToPay());

        assertEquals(new BigDecimal("104.50"), response.getGrandTotal());
        assertEquals(new BigDecimal("60.00"), response.getCommonItemsTotal());
        assertEquals(new BigDecimal("10"), response.getServiceFeePercent());
    }

    @Test
    void splitBill_zeroFee() {
        BillSplitRequestDto request = new BillSplitRequestDto(
                BigDecimal.ZERO,
                List.of(),
                List.of(
                        new ParticipantDto(
                                "Alice",
                                List.of(new ItemDto("Pasta", new BigDecimal("10.00")))
                        ),
                        new ParticipantDto(
                                "Bob",
                                List.of(new ItemDto("Burger", new BigDecimal("20.00")))
                        )
                )
        );

        BillSplitResponseDto response = service.splitBill(request);

        ParticipantShareDto alice = response.getParticipants().get(0);
        assertEquals(new BigDecimal("10.00"), alice.getTotalToPay());
        ParticipantShareDto bob = response.getParticipants().get(1);
        assertEquals(new BigDecimal("20.00"), bob.getTotalToPay());

        assertEquals(new BigDecimal("30.00"), response.getGrandTotal());
    }

    @Test
    void splitBill_noCommonItems() {
        BillSplitRequestDto request = new BillSplitRequestDto(
                new BigDecimal("10"),
                List.of(),
                List.of(
                        new ParticipantDto(
                                "Alice",
                                List.of(new ItemDto("Pasta", new BigDecimal("10.00")))
                        )
                )
        );

        BillSplitResponseDto response = service.splitBill(request);

        ParticipantShareDto alice = response.getParticipants().get(0);
        // 10 + 10% = 11
        assertEquals(new BigDecimal("10.00"), alice.getPersonalItemsTotal());
        assertEquals(new BigDecimal("0.00"), alice.getCommonItemsShare());
        assertEquals(new BigDecimal("1.00"), alice.getServiceFee());
        assertEquals(new BigDecimal("11.00"), alice.getTotalToPay());
    }

    @Test
    void splitBill_emptyParticipants_throwsException() {
        BillSplitRequestDto request = new BillSplitRequestDto(
                new BigDecimal("10"),
                List.of(),
                List.of()
        );

        assertThrows(IllegalArgumentException.class,
                () -> service.splitBill(request));
    }
}
