package ru.otus.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DBServiceClient;

import java.util.List;

@Controller
@RequestMapping("/admin/clients")
@RequiredArgsConstructor
public class ClientAdminController {

    private final DBServiceClient dbServiceClient;

    @GetMapping
    public String listClients(Model model) {
        List<Client> clients = dbServiceClient.findAll();
        model.addAttribute("clients", clients);
        return "client-list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("client", new Client(null, ""));
        return "client-form";
    }

    @PostMapping
    public String createClient(@ModelAttribute Client client) {
        dbServiceClient.saveClient(client);
        return "redirect:/admin/clients";
    }

    @GetMapping("/{id}")
    public String getClient(@PathVariable Long id, Model model) {
        var clientOpt = dbServiceClient.getClient(id);
        if (clientOpt.isEmpty()) {
            return "redirect:/admin/clients?error=not_found";
        }
        model.addAttribute("client", clientOpt.get());
        return "client-details";
    }
}
