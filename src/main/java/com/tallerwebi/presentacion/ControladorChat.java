package com.tallerwebi.presentacion;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorChat {

    @RequestMapping(path = "/sala-chat", method = RequestMethod.GET)
    public ModelAndView irAHome() {
        return new ModelAndView("sala-chat");
    }
}
