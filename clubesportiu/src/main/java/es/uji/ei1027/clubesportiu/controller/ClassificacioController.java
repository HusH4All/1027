package es.uji.ei1027.clubesportiu.controller;

import es.uji.ei1027.clubesportiu.dao.ClassificacioDao;
import es.uji.ei1027.clubesportiu.model.Classificacio;
import es.uji.ei1027.clubesportiu.model.Nadador;
import es.uji.ei1027.clubesportiu.services.ClassificacioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriUtils;

@Controller
@RequestMapping("/classificacio")
public class ClassificacioController {

    private ClassificacioDao classificacioDao;

    @Autowired
    public void setClassificacioDao(ClassificacioDao classificacioDao){
        this.classificacioDao = classificacioDao;
    }
    @RequestMapping("/list")
    public String listClassificacions(Model model) {
        model.addAttribute("classificacions", classificacioDao.getClassificacions());
        return "classificacio/list";
    }

    @RequestMapping(value="/add")
    public String addClassificacio(Model model) {
        model.addAttribute("classificacio", new Classificacio());
        return "classificacio/add";
    }
    @RequestMapping(value="/add", method=RequestMethod.POST)
    public String processAddClassif(
            @ModelAttribute("classificacio") Classificacio classificacio,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "classificacio/add";
        try {
            classificacioDao.addClassificacio(classificacio);
        } catch (DuplicateKeyException e) {
            throw new ClubesportiuException(
                    "Ja existeix una classificacio d'aquest nadador en "
                            +classificacio.getNomProva(), "CPduplicada");
        } catch (DataAccessException e) {
            throw new ClubesportiuException(
                    "Error en l'accés a la base de dades", "ErrorAccedintDades");
        }
        return "redirect:list";
    }


    @RequestMapping(value="/update/{nom}/{nomProva}", method = RequestMethod.GET)
    public String editClassificacio(Model model, @PathVariable String nom, @PathVariable String nomProva) {
        model.addAttribute("classificacio", classificacioDao.getClassificacio(nom, nomProva));
        return "classificacio/update";
    }
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(
            @ModelAttribute("classificacio") Classificacio classificacio,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "classificacio/update";
        classificacioDao.updateClassificacio(classificacio);
        return "redirect:list";
    }
    @RequestMapping(value = "/delete/{nom}/{nomProva}")
    public String processDeleteClassif(@PathVariable String nom,
                                       @PathVariable String nomProva) {
        classificacioDao.deleteClassificacio(nom, nomProva);
        return "redirect:../../list";
    }

    private ClassificacioService classificacioService;

    @Autowired
    public void setClassificacioService(ClassificacioService classificacioService) {
        this.classificacioService = classificacioService;
    }

    @RequestMapping("/perpais/{nom}")
    public String listClsfPerPais(Model model, @PathVariable String nom) {
        model.addAttribute("classificacions",
                classificacioService.getClassificationByCountry(nom));
        model.addAttribute("nom", nom);
        return "classificacio/perpais";
    }

    @RequestMapping("/perprova/{pais}")
    public String listClsfPerProva(Model model, @PathVariable String pais) {
        model.addAttribute("classificacions",
                classificacioService.getNadadorsPerProva(pais));
        model.addAttribute("pais", pais);
        return "classificacio/perprova";
    }

    @RequestMapping(value="/addPerProva/{nom}")
    public String addClassifPerProva(Model model,
                                     @PathVariable String nom) {
        Classificacio classificacio = new Classificacio();
        classificacio.setNomProva(nom);
        model.addAttribute("novaclassificacio", classificacio);
        model.addAttribute("classificacions",
                classificacioDao.getClassificacioProva(nom));
        model.addAttribute("nadadors",
                classificacioService.getNadadorsElegiblesPerProva(nom));
        return "classificacio/addPerProva";
    }

    @RequestMapping(value="/addPerProva", method=RequestMethod.POST)
    public String processAddSubmitPerProva(
            @ModelAttribute("classificacio") Classificacio classificacio,
            BindingResult bindingResult) {
        // Here we should include the validation
        // ...
        if (bindingResult.hasErrors())
            return "classificacio/addPerProva";
        classificacioDao.addClassificacio(classificacio);
        String nameUri="redirect:addPerProva/" + classificacio.getNomProva();
        nameUri = UriUtils.encodePath(nameUri, "UTF-8");
        return nameUri;
    }

}
