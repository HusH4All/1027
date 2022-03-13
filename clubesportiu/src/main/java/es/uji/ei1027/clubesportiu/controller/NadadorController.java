package es.uji.ei1027.clubesportiu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.uji.ei1027.clubesportiu.dao.NadadorDao;
import es.uji.ei1027.clubesportiu.model.Nadador;

import java.util.Arrays;
import java.util.List;


@Controller
@RequestMapping("/nadador")
public class NadadorController {

    private NadadorDao nadadorDao;

    @Autowired
    public void setNadadorDao(NadadorDao nadadorDao) {
        this.nadadorDao=nadadorDao;
    }

    @RequestMapping("/list")
    public String listNadadors(Model model) {
        model.addAttribute("nadadors", nadadorDao.getNadadors());
        return "nadador/list";
    }
    @RequestMapping(value="/add")
    public String addNadador(Model model) {
        model.addAttribute("nadador", new Nadador());
        return "nadador/add";
    }
    @RequestMapping(value="/add", method=RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("nadador") Nadador nadador,
                                   BindingResult bindingResult) {
        NadadorValidator nadadorValidator = new NadadorValidator();
        nadadorValidator.validate(nadador, bindingResult);
        if (bindingResult.hasErrors())
            return "nadador/add";
        nadadorDao.addNadador(nadador);
        return "redirect:list";
    }
    @RequestMapping(value="/update/{nom}", method = RequestMethod.GET)
    public String editNadador(Model model, @PathVariable String nom) {
        model.addAttribute("nadador", nadadorDao.getNadador(nom));
        List<String> genderList = Arrays.asList("Femeni", "Masculi", "Altre");
        model.addAttribute("genderList", genderList);
        return "nadador/update";
    }
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(
            @ModelAttribute("nadador") Nadador nadador,
            BindingResult bindingResult) {
        NadadorValidator nadadorValidator = new NadadorValidator();
        nadadorValidator.validate(nadador, bindingResult);
        if (bindingResult.hasErrors())
            return "nadador/update";
        nadadorDao.updateNadador(nadador);
        return "redirect:list";
    }
    @RequestMapping(value="/delete/{nom}")
    public String processDelete(@PathVariable String nom) {
        nadadorDao.deleteNadador(nom);
        return "redirect:../list";
    }
}
