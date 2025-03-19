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
public class ChildController {

    private final ChildService childService;
    private final InvoiceService invoiceService;

    public ChildController(ChildService childService, InvoiceService invoiceService) {
        this.childService = childService;
        this.invoiceService = invoiceService;
    }

    @GetMapping("/children")
    public String listChildren(Model model) {
        model.addAttribute("children", childService.getAllChildren());
        model.addAttribute("invoices", invoiceService.getAllInvoices());
        return "children";
    }

    @GetMapping("/children/create")
    public String createChildForm(Model model) {
        Child child = new Child();
        model.addAttribute("child", child);
        return "create_child";
    }

    @GetMapping("/child/edit/{id}")
    public String editChildForm(@PathVariable Long id, Model model) {
        model.addAttribute("child", childService.getChildById(id));
        return "edit_child";
    }

    @PostMapping("/child/edit/{id}")
    public String updateChild(@PathVariable Long id, @ModelAttribute("Child") Child child) {
        Child presentChild = childService.getChildById(id);
        Invoice presentInvoice = invoiceService.getInvoiceById(id);

        presentInvoice.setChildName(presentChild.getName());
        presentInvoice.setSubject1(presentChild.getSubject1());
        presentInvoice.setSubject2(presentChild.getSubject2());

        presentChild.setDate(child.getDate());
        presentChild.setBgNumber(child.getBgNumber());
        presentChild.setName(child.getName());
        presentChild.setPeriod(child.getPeriod());
        presentChild.setRecipient(child.getRecipient());
        presentChild.setHousingBenefitNumber(child.getHousingBenefitNumber());
        presentChild.setLegalRepresentative(child.getLegalRepresentative());
        presentChild.setInvoiceNumber(child.getInvoiceNumber());
        presentChild.setSubject1(child.getSubject1());
        presentChild.setSubject2(child.getSubject2());
        childService.saveChild(presentChild);
        return "redirect:/children";
    }

    @PostMapping("/children")
    public String saveChild(@ModelAttribute("child") Child child) {
        childService.saveChild(child);
        invoiceService.saveInvoice(new Invoice(child.getId(), child.getName(), child.getSubject1(), 0.0, child.getSubject2(), 0.0, 0.0, 0.0, 0.0));
        return "redirect:/children";
    }

    @GetMapping("/child/delete/{id}")
    public String deleteChild(@PathVariable Long id) {
        childService.deleteChildById(id);
        invoiceService.deleteInvoiceById(id);
        return "redirect:/children";
    }
}