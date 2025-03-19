package de.yvanzambou.nachhilfe_einstein.controller;

import de.yvanzambou.nachhilfe_einstein.entity.Child;
import de.yvanzambou.nachhilfe_einstein.entity.Invoice;
import de.yvanzambou.nachhilfe_einstein.service.ChildService;
import de.yvanzambou.nachhilfe_einstein.service.InvoiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final ChildService childService;

    public InvoiceController(InvoiceService invoiceService, ChildService childService) {
        this.invoiceService = invoiceService;
        this.childService = childService;
    }

    @GetMapping("/children/invoices")
    public String getAllInvoices(Model model) {
        model.addAttribute("invoices", invoiceService.getAllInvoices());
        return "invoices";
    }

    @GetMapping("/child/invoice/{id}")
    public String getInvoicesById(@PathVariable Long id, Model model) {;
        model.addAttribute("invoices", invoiceService.getInvoiceById(id));
        return "invoices";
    }

    @GetMapping("/invoice/edit/{id}")
    public String editChildForm(@PathVariable Long id, Model model) {
        model.addAttribute("invoice", invoiceService.getInvoiceById(id));
        return "edit_invoice";
    }

    @PostMapping("/invoice/edit/{id}")
    public String updateInvoice(@PathVariable Long id, @ModelAttribute("Invoice") Invoice invoice) {
        Invoice presentInvoice = invoiceService.getInvoiceById(id);
        Child presentChild = childService.getChildById(id);

        presentChild.setName(invoice.getChildName());
        presentChild.setSubject1(invoice.getSubject1());
        presentChild.setSubject2(invoice.getSubject2());

        presentInvoice.setChildName(invoice.getChildName());
        presentInvoice.setSubject1(invoice.getSubject1());
        presentInvoice.setLessonUnit1(invoice.getLessonUnit1());
        presentInvoice.setSubject2(invoice.getSubject2());
        presentInvoice.setLessonUnit2(invoice.getLessonUnit2());
        presentInvoice.setTotalLessonUnit(invoice.getTotalLessonUnit());
        presentInvoice.setLessonUnitPrice(invoice.getLessonUnitPrice());
        presentInvoice.setTotalAmount(invoice.getTotalAmount());
        childService.saveChild(presentChild);
        invoiceService.saveInvoice(presentInvoice);
return "redirect:/children/invoices";
    }
}
