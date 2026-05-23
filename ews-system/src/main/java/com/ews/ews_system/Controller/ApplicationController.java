package com.ews.ews_system.Controller;

import com.ews.ews_system.Model.EWSApplication;
import com.ews.ews_system.Model.User;
import com.ews.ews_system.Service.ApplicationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

//    ye method apply form ke sare  data ko  submit ke utha ke
//    EWSApplication ke entity me dal raha hai  but file aise add nahi hota
//    es liye MultiPart ke object me inputfile le raha

    @PostMapping("/saveApplication")
    public String saveApplication(EWSApplication application,
                                  MultipartFile photoFile,
                                  MultipartFile incomeFile,
                                  HttpSession session) throws IOException{

//        phoroName me photoFile ka actual name nikal ke  uuid class ke randomuuid
//         method ka use kar ke file ka name unique bana store kar raha hai
//        kyoki kisi  user ka file name same n rahe
        String photoName = UUID.randomUUID() + "_" + photoFile.getOriginalFilename();
        String incomeName =UUID.randomUUID() + "_" + incomeFile.getOriginalFilename();

//        File type ka photoFolder create kar ke usme jaha photo save hoga uska
//        path store kar rha hai
        File photoFolder =new File(System.getProperty("user.dir") + "/uploads/photos/");
        File incomeFolder =new File(System.getProperty("user.dir") + "/uploads/income/");
        if (!photoFolder.exists()){
            photoFolder.mkdir();
        }
        if (!incomeFolder.exists()){
            incomeFolder.mkdir();
        }

//yaha jo user ne photoFileile bheji hai use folder path ke andar
// actual file name ko add kae ke save kar de raha hai
        photoFile.transferTo(new File(photoFolder,photoName));
        incomeFile.transferTo(new File(incomeFolder,incomeName));

// then file ke actual name ko database me save ho rha hai jo use karna ho path dal
// ke + file name se access kar sakta hu
        application.setPhoto(photoName);
        application.setIncomeCertificate(incomeName);
        application.setStatus("Pending");
        User user =(User)session.getAttribute("user");
        application.setUser(user);
        applicationService.saveApplication(application);
        return "redirect:/";

    }

    @GetMapping("/track-status")
    public String trackStatus(HttpSession session,
                              Model model){
        User user = (User)session.getAttribute("user");
        if (user==null){
            return "user-login";
        }
       List<EWSApplication> applications = applicationService.getApplicationByUser(user);
        System.out.println(applications.size());
        model.addAttribute("applications", applications);
        return "track-status";
    }

}
//src/main/resources/static/uploads/income/