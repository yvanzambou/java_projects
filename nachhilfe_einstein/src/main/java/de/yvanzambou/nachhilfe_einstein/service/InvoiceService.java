package de.yvanzambou.nachhilfe_einstein.service;

import de.yvanzambou.nachhilfe_einstein.entity.Invoice;

import java.util.List;

public interface InvoiceService {

    List<Invoice> getAllInvoices();

    void saveInvoice(Invoice invoice);

    Invoice getInvoiceById(Long id);

    void deleteInvoiceById(Long id);
}
