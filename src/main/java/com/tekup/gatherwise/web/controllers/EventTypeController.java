package com.tekup.gatherwise.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.tekup.gatherwise.business.services.EventTypeService;
import com.tekup.gatherwise.dao.entities.EventType;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.model.IModel;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;



@Controller
@RequestMapping("/event-types")
public class EventTypeController {


    private static final Logger logger = LoggerFactory.getLogger(EventController.class);
    private static final String uploadDirectory = System.getProperty("user.dir") + "/src/main/resources/static/images";

    @Autowired
    private EventTypeService eventTypeService;

    @GetMapping("")
    public String listEventTypes(Model model) {
        List<EventType> eventTypes = eventTypeService.getAllEventTypes();
        model.addAttribute("eventTypes", eventTypes);
        return "eventType/eventType-list"; // Nom du fichier HTML/Thymeleaf pour afficher la liste des types d'événements
    }

    @GetMapping("/create")
    public String showFormAddEventType(Model model) {
        EventType eventType = new EventType();
        model.addAttribute("eventType", eventType);
        return "eventType/add-eventType"; // Nom du fichier HTML/Thymeleaf pour le formulaire d'ajout de type d'événement
    }

    @RequestMapping(path="/create", method=RequestMethod.POST)
    public String saveEventType(@Valid @ModelAttribute("eventType") EventType eventType,
                                BindingResult bindingResult,
                                Model model,
                                @RequestParam("iconFile") MultipartFile file) {
        if(bindingResult.hasErrors()){
            return "eventType/add-eventType";
        }
        if (!file.isEmpty()) {
            StringBuilder fileName = new StringBuilder();
            fileName.append(file.getOriginalFilename());
            Path newFilePath = Paths.get(uploadDirectory, fileName.toString());

            try {
                Files.write(newFilePath, file.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }

            this.eventTypeService
                    .addEventType(new EventType(null, eventType.getTypeName(), eventType.getDescription(), fileName.toString()));
        } else {

            this.eventTypeService.addEventType(new EventType(null, eventType.getTypeName(), eventType.getDescription(), null));
        }
        eventTypeService.addEventType(eventType);
        return "redirect:/event-types"; // Rediriger vers la liste des types d'événements après l'ajout
    }

    @GetMapping("/{id}/edit")
    public String showEditEventTypeForm(@PathVariable Long id, Model model) {
        EventType eventType = eventTypeService.getEventTypeById(id);
        model.addAttribute("eventType", eventType);
        return "eventType/edit-eventType";
    }

    @RequestMapping(path = "/{id}/edit", method = RequestMethod.POST)
    public String updateEventType(@PathVariable Long id,
                                  @Valid @ModelAttribute("eventType") EventType eventType,
                                  BindingResult bindingResult,
                                  Model model,
                                  @RequestParam MultipartFile iconFile) {
        if (bindingResult.hasErrors()) {
            return "eventType/edit-eventType";
        }

        EventType existingEventType = eventTypeService.getEventTypeById(id);
        existingEventType.setTypeName(eventType.getTypeName());
        existingEventType.setDescription(eventType.getDescription());

        if (!iconFile.isEmpty()) {
            // upload icon
            StringBuilder fileName = new StringBuilder();
            Path newFilePath = Paths.get(uploadDirectory, iconFile.getOriginalFilename());
            fileName.append(iconFile.getOriginalFilename());
            try {
                Files.write(newFilePath, iconFile.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // delete old icon
            if (existingEventType.getIcon() != null) {
                Path filePath = Paths.get(uploadDirectory, existingEventType.getIcon());
                try {
                    Files.deleteIfExists(filePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            existingEventType.setIcon(fileName.toString());
        }

        eventTypeService.updateEventType(id, existingEventType);
        return "redirect:/event-types";
    }

    @PostMapping("/{id}/delete")
    public String deleteEventType(@PathVariable Long id) {
        eventTypeService.deleteEventType(id);
        return "redirect:/event-types"; // Rediriger vers la liste des types d'événements après la suppression
    }
}