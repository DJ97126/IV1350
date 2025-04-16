package integration;

import model.ReceiptDTO;

public class Printer {
    public Printer() {}

    public void printReceipt(ReceiptDTO receiptDTO) {
        System.out.println("""
                ------------------ Begin receipt -------------------
                Time of Sale: [Time of Sale]

                [Item Name] [Item Quantity] x [Item Price] [Item Total Price] SEK

                Total: [Total Price] SEK
                VAT: [Total VAT]

                Cash: [Cash paid] SEK
                Change: [Change] SEK
                ------------------ End receipt ---------------------
                """);
    }
}
