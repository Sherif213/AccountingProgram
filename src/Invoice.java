import java.util.Date;

public class Invoice {
    private int invoiceID;
    private String invoiceNumber;
    private Date invoiceDate;
    private String sellerName;
    private String buyerName;
    private double totalAmount;
    private String currency;
    private Date paymentDueDate;
    private String transactionType;
    private String userDetails;

    public Invoice(int invoiceID, String invoiceNumber, Date invoiceDate, String sellerName, String buyerName,
                   double totalAmount, String currency, Date paymentDueDate, String transactionType, String userDetails) {
        this.invoiceID = invoiceID;
        this.invoiceNumber = invoiceNumber;
        this.invoiceDate = invoiceDate;
        this.sellerName = sellerName;
        this.buyerName = buyerName;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.paymentDueDate = paymentDueDate;
        this.transactionType = transactionType;
        this.userDetails = userDetails;
    }

    public int getInvoiceID() { return invoiceID; }
    public String getInvoiceNumber() { return invoiceNumber; }
    public Date getInvoiceDate() { return invoiceDate; }
    public String getSellerName() { return sellerName; }
    public String getBuyerName() { return buyerName; }
    public double getTotalAmount() { return totalAmount; }
    public String getCurrency() { return currency; }
    public Date getPaymentDueDate() { return paymentDueDate; }
    public String getTransactionType() { return transactionType; }
    public String getUserDetails() { return userDetails; }
}
