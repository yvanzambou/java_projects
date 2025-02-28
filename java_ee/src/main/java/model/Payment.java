package model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Payment {

    int id;
    boolean invoice;
    @Nullable Creditcard creditcard; // ist null, falls invoice true ist.

    public Payment(int id, @NotNull Creditcard creditcard) {
        this.id = id;
        this.invoice = false;
        this.creditcard = creditcard;
    }

    public Payment(int id) {
        this.id = id;
        this.invoice = true;
        this.creditcard = null;
    }

    public int getId() {
        return id;
    }

    public boolean isInvoice() {
        return invoice;
    }

    public @Nullable Creditcard getCreditcard() {
        return creditcard;
    }
}
