package de.yvanzambou.nachhilfe_einstein.entity;

import de.yvanzambou.nachhilfe_einstein.util.Utility;
import jakarta.persistence.*;

@Entity
@Table(name = "invoice")
public class Invoice {

    @Id
    @Column(name = "child_id")
    private Long id;

    @Column(name = "child_name")
    private String childName;

    @Column(name = "subject_1")
    private String subject1;

    @Column(name = "lesson_unit_1")
    private double lessonUnit1;

    @Column(name = "subject_2")
    private String subject2;

    @Column(name = "lesson_unit_2")
    private double lessonUnit2;

    @Column(name = "total_lesson_unit")
    private double totalLessonUnit;

    @Column(name = "lesson_unit_price")
    private double lessonUnitPrice;

    @Column(name = "total_amount")
    private double totalAmount;

    public Invoice() {}

    public Invoice(Long id, String childName, String subject1, double lessonUnit1, String subject2, double lessonUnit2, double totalLessonUnit, double lessonUnitPrice, double totalAmount) {
        this.id = id;
        this.childName = childName;
        this.subject1 = subject1;
        this.lessonUnit1 = lessonUnit1;
        this.subject2 = subject2;
        this.lessonUnit2 = lessonUnit2;
        this.totalLessonUnit = totalLessonUnit;
        this.lessonUnitPrice = lessonUnitPrice;
        this.totalAmount = totalAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getSubject1() {
        return subject1;
    }

    public void setSubject1(String subject1) {
        this.subject1 = subject1;
    }

    public double getLessonUnit1() {
        return lessonUnit1;
    }

    public void setLessonUnit1(double lessonUnit1) {
        this.lessonUnit1 = lessonUnit1;
    }

    public String getSubject2() {
        return subject2;
    }

    public void setSubject2(String subject2) {
        this.subject2 = subject2;
    }

    public double getLessonUnit2() {
        return lessonUnit2;
    }

    public void setLessonUnit2(double lessonUnit2) {
        this.lessonUnit2 = lessonUnit2;
    }

    public double getTotalLessonUnit() {
        return lessonUnit1 + lessonUnit2;
    }

    public void setTotalLessonUnit(double totalLessonUnit) {
        this.totalLessonUnit = totalLessonUnit;
    }

    public double getLessonUnitPrice() {
        return lessonUnitPrice;
    }

    public void setLessonUnitPrice(double lessonUnitPrice) {
        this.lessonUnitPrice = lessonUnitPrice;
    }

    public String getTotalAmountFormat() {
        return Utility.getPriceFormat(totalAmount);
    }

    public double getTotalAmount() {
        return getLessonUnitPrice() * getTotalLessonUnit();
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
