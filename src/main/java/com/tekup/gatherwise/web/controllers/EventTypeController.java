package com.tekup.gatherwise.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.tekup.gatherwise.business.services.EventTypeService;
import com.tekup.gatherwise.dao.entities.EventType;

import jakarta.validation.Valid;

import java.util.List;

@Controller
@RequestMapping("/event-types")
public class EventTypeController {

    @Autowired
    private EventTypeService eventTypeService;

    @GetMapping("")
    public String listEventTypes(Model model) {
        List<EventType> eventTypes = eventTypeService.getAllEventTypes();
        model.addAttribute("eventTypes", eventTypes);
        return "eventType/eventTypeList"; // Nom du fichier HTML/Thymeleaf pour afficher la liste des types d'événements
    }

    @GetMapping("/create")
    public String showFormAddEventType(Model model) {
        EventType eventType = new EventType();
        model.addAttribute("eventType", eventType);
        return "eventType/eventTypeAddForm"; // Nom du fichier HTML/Thymeleaf pour le formulaire d'ajout de type d'événement
    }

    @PostMapping("/create")
    public String saveEventType(@Valid @ModelAttribute("eventType") EventType eventType,
                                BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return "eventType/eventTypeAddForm";
        }
        eventTypeService.addEventType(eventType);
        return "redirect:/event-types"; // Rediriger vers la liste des types d'événements après l'ajout
    }

    @GetMapping("/{id}/edit")
    public String updateEventType(@PathVariable Long id, Model model) {
        EventType eventType = eventTypeService.getEventTypeById(id);
        model.addAttribute("eventType", eventType);
        return "eventType/eventTypeEditForm"; // Nom du fichier HTML/Thymeleaf pour le formulaire de mise à jour de type d'événement
    }

    @PostMapping("/{id}/edit")
    public String updateEventType(@PathVariable Long id,
                                  @Valid @ModelAttribute("eventType") EventType eventType,
                                  BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return "eventType/eventTypeEditForm";
        }
        eventTypeService.updateEventType(id, eventType);
        return "redirect:/eventTypes"; // Rediriger vers la liste des types d'événements après la mise à jour
    }

    @PostMapping("/{id}/delete")
    public String deleteEventType(@PathVariable Long id) {
        eventTypeService.deleteEventType(id);
        return "redirect:/eventTypes"; // Rediriger vers la liste des types d'événements après la suppression
    }
}