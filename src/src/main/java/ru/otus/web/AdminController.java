package ru.otus.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Manager;
import ru.otus.crm.repository.ClientRepository;
import ru.otus.crm.repository.ManagerRepository;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ClientRepository clientRepository;
    private final ManagerRepository managerRepository;

    @GetMapping("/clients")
    public String listClients(Model model) {
        model.addAttribute("clients", ((Iterable<Client>) clientRepository.findAll()).spliterator()
                .getExactSizeIfKnown() > 0 ?
                java.util.stream.StreamSupport.stream(clientRepository.findAll().spliterator(), false).toList() :
                java.util.List.of());
        return "clients/list";
    }

    @GetMapping("/clients/new")
    public String showCreateClientForm(Model model) {
        model.addAttribute("client", new Client());
        return "clients/form";
    }

    @PostMapping("/clients")
    public String createClient(@ModelAttribute Client client) {
        clientRepository.save(client); // ID будет заполнен внутри объекта после вставки
        return "redirect:/admin/clients";
    }

    @GetMapping("/managers")
    public String listManagers(Model model) {
        model.addAttribute("managers", ((Iterable<Manager>) managerRepository.findAll()).spliterator()
                .getExactSizeIfKnown() > 0 ?
                java.util.stream.StreamSupport.stream(managerRepository.findAll().spliterator(), false).toList() :
                java.util.List.of());
        return "managers/list";
    }

    @GetMapping("/managers/new")
    public String showCreateManagerForm(Model model) {
        model.addAttribute("manager", new Manager());
        return "managers/form";
    }

    @PostMapping("/managers")
    public String createManager(@ModelAttribute Manager manager) {
        managerRepository.save(manager);
        return "redirect:/admin/managers";
    }
}