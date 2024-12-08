package com.tekup.gatherwise.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tekup.gatherwise.web.models.User;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class GatherWiseController {
  @GetMapping({"/","/home"})
  public String getHomePage() {
      return "home";
  }
  

   @RequestMapping("/gatherwise")
    private String greeting(@RequestParam(value="name",required = false, defaultValue = "World") String name,
                            @RequestParam int age,
                            Model model){
         String [] names=new String[]{"demo1","demo2","demo3"};
         model.addAttribute("names", names);
        model.addAttribute("user", new User("Demo","admin"));
        model.addAttribute("name", name);
        model.addAttribute("age", age);
        return "gatherwise";
    }


    
}