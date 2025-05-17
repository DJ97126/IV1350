package dto;

import java.util.ArrayList;

import model.Amount;

public record DiscountDTO(ArrayList<ItemDTO> boughtIitems, Amount totalPrice, int customerId) {}
