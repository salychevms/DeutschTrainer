package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Models.UserLanguage;
import de.salychevms.deutschtrainer.Repo.UserLanguageRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserLanguageController {
    final UserLanguageRepository userLanguageRepository;

    public UserLanguageController(UserLanguageRepository userLanguageRepository) {
        this.userLanguageRepository = userLanguageRepository;
    }

    public boolean addLanguage(Long id){
        UserLanguage newLang=new UserLanguage();
        return true;
    }
}
