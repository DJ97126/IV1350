package integration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import dto.ItemDTO;
import dto.ReceiptDTO;

import static utils.StringUtils.formatBigDecimalToColon;

public class Printer {
    public Printer() {}

    /**
     * Prints the receipt to the console. Simulates a printer.
     * 
     * @param receiptDTO
     */
    public void printReceipt(ReceiptDTO receiptDTO) {
        Map<ItemDTO, Integer> quantizedItems = getQuantizedItems(receiptDTO);
        String itemsString = getItemsString(quantizedItems);
        String formattedReceipt = getFormattedReceipt(receiptDTO, itemsString);
        System.out.println(formattedReceipt);
    }

    private Map<ItemDTO, Integer> getQuantizedItems(ReceiptDTO receiptDTO) {
        Map<ItemDTO, Integer> itemQuantities = new HashMap<>();

        for (ItemDTO item : receiptDTO.boughtItems()) {
            itemQuantities.merge(item, 1, Integer::sum);
        }

        return itemQuantities;
    }

    private String getItemsString(Map<ItemDTO, Integer> itemQuantities) {
        StringBuilder itemsStringBuilder = new StringBuilder();

        for (Map.Entry<ItemDTO, Integer> entry : itemQuantities.entrySet()) {
            ItemDTO item = entry.getKey();
            int quantity = entry.getValue();

            BigDecimal totalItemPrice = item.price().multiply(new BigDecimal(quantity));

            String itemName = (item.name().length() > 21)
                    ? item.name().substring(0, 19) + "..."
                    : item.name();
            String itemQuantity = String.valueOf(quantity);
            String itemPrice = formatBigDecimalToColon(item.price());
            String itemTotal = formatBigDecimalToColon(totalItemPrice);

            String formattedString = """
                    %-24s %2s x %7s %10s SEK
                    """.formatted(itemName, itemQuantity, itemPrice, itemTotal);

            itemsStringBuilder.append(formattedString);
        }

        return itemsStringBuilder.toString();
    }

    private String getFormattedReceipt(ReceiptDTO receiptDTO, String itemsString) {
        String time = receiptDTO.saleDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        String total = formatBigDecimalToColon(receiptDTO.totalPrice());
        String vat = formatBigDecimalToColon(receiptDTO.totalVat());
        String paid = formatBigDecimalToColon(receiptDTO.amountPaid());
        String change = formatBigDecimalToColon(receiptDTO.change());

        return """
                ------------------ Begin receipt -------------------
                Time of Sale: %38s

                %s

                Total: %41s SEK
                VAT: %43s SEK

                Cash: %42s SEK
                Change: %40s SEK
                ------------------ End receipt ---------------------
                """.formatted(time, itemsString, total, vat, paid, change);
    }

    /* Only for debug perposes during development */
    public static void main(String[] args) {
        Printer printer = new Printer();

        ItemDTO[] boughtItems = new ItemDTO[12];
        for (int i = 0; i < 11; i++) {
            boughtItems[i] = new ItemDTO("test1", "test1",
                    new BigDecimal("12"), new BigDecimal("0.456"),
                    "testDesc");
        }
        boughtItems[11] = new ItemDTO("test2", "test kinda long name item",
                new BigDecimal("4567"), new BigDecimal("0.123"),
                "testDesc2");

        LocalDateTime saleDateTime = LocalDateTime.parse("2024-02-12T16:05");
        BigDecimal totalPrice = new BigDecimal("74.7");
        BigDecimal totalVat = new BigDecimal("4.23");
        BigDecimal amountPaid = new BigDecimal("100.0");
        BigDecimal change = new BigDecimal("25.3");
        ReceiptDTO receipt = new ReceiptDTO(saleDateTime, boughtItems, totalPrice, totalVat, amountPaid, change);

        printer.printReceipt(receipt);
    }
}
