package model;

import java.util.Date;

public class Creditcard {

    int id;
    Date expiryDate;
    int cvv;
    String cardNumber;

    public Creditcard(int id, Date expiryDate, int cvv, String cardNumber) {
        this.id = id;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.cardNumber = cardNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
}
