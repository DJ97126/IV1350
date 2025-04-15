package model;

import java.time.LocalTime;

import integration.ItemDTO;


public record SaleDTO(int id, LocalTime dateTime, ItemDTO[] boughtItems, float totalPrice, float totalVat, float amountPaid, float change) {}
