package com.sqli.testapp.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDto {
    private int id;
    @Digits(fraction = 2, integer = 15, message = "Invalid amount number")
    @NotNull(message = "Amount is required")
    private Float amount;
    @Digits(fraction = 0, integer = 100, message = "Invalid id")
    @NotNull(message = "client id is required")
    private Integer clientId;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    public OrderDto(int id, Float amount, Integer clientId) {
        this.id = id;
        this.amount = amount;
        this.clientId = clientId;
    }

}
