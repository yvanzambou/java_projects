package de.yvanzambou.nachhilfe_einstein.entity;

import de.yvanzambou.nachhilfe_einstein.util.Utility;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="child")
public class Child {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "child_name")
    private String name;

    @Column(name = "legal_representative")
    private String legalRepresentative;

    @Column(name = "bg_number")
    private String bgNumber;

    @Column(name = "housing_benefit_number")
    private String housingBenefitNumber;

    @Column(name = "subject_1")
    private String subject1;

    @Column(name = "subject_2")
    private String subject2;

    @Column(name = "recipient")
    private String recipient;

    @Column(name = "_date")
    private String date;

    @Column(name = "invoice_number")
    private String invoiceNumber;

    @Column(name = "period")
    private String period;

    @Column(name = "total_amount")
    private double totalAmount;

    public Child() {}

    public Child(String name, String legalRepresentative, String bgNumber, String housingBenefitNumber, String subject1, String subject2, String recipient, String date, String invoiceNumber, String period, double totalAmount) {
        this.name = name;
        this.legalRepresentative = legalRepresentative;
        this.bgNumber = bgNumber;
        this.housingBenefitNumber = housingBenefitNumber;
        this.subject1 = subject1;
        this.subject2 = subject2;
        this.recipient = recipient;
        this.date = date;
        this.invoiceNumber = invoiceNumber;
        this.period = period;
        this.totalAmount = totalAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLegalRepresentative() {
        return legalRepresentative;
    }

    public void setLegalRepresentative(String legalRepresentative) {
        this.legalRepresentative = legalRepresentative;
    }

    public String getBgNumber() {
        return bgNumber;
    }

    public void setBgNumber(String bgNumber) {
        this.bgNumber = bgNumber;
    }

    public String getHousingBenefitNumber() {
        return housingBenefitNumber;
    }

    public void setHousingBenefitNumber(String housingBenefitNumber) {
        this.housingBenefitNumber = housingBenefitNumber;
    }

    public String getSubject1() {
        return subject1;
    }

    public void setSubject1(String subject1) {
        this.subject1 = subject1;
    }

    public String getSubject2() {
        return subject2;
    }

    public void setSubject2(String subject2) {
        this.subject2 = subject2;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getTotalAmountFormat() {
        return Utility.getPriceFormat(totalAmount);
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
