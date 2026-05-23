package com.ews.ews_system.Controller;

import com.ews.ews_system.Model.Admin;
import com.ews.ews_system.Model.EWSApplication;
import com.ews.ews_system.Service.AdminService;
import com.ews.ews_system.Service.ApplicationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private ApplicationService applicationService;

//    Admin ka ligin check kar ragha hai admin-login.html se data fetch kar raha hai..
//    Admin service ke method call kar raha hai vo admin entity return kar rha hai then
//    Admin milne pe session create ho raha hai..
//    model act as a container jo html page pe admin ko send kar raha hai vaha
//    ham Admin ke entity use kar sakte but yaha error message send kar rahe hai

    @PostMapping("/adminlogins")
    public String loginAdmin(@RequestParam String email,
                             @RequestParam String password,
                             HttpSession session,
                             Model model){
//        System.out.println("yaha tak aaya hu.......");

        Admin admin = adminService.login(email, password);
        if (admin!= null ){
            session.setAttribute("admin",admin);
            return "redirect:/admin-dashboard";
        }
        model.addAttribute("error","Wrong email or password");
        return "admin-login";
    }



//    ye method upar wale method ke return admin-dashboard ko catch kar raha
//    aur user form ke data ko database se lake dashboad pe show kar raha hai
//    pending  satus ko...

    @GetMapping("/admin-dashboard")
    public String dashBoard(HttpSession session, Model model){

        if (session.getAttribute("admin")==null){
            return "admin-login";
        }
//        System.out.println("yaha pe hu.....");

        List<EWSApplication> applications = applicationService.getPendingApplication();
        model.addAttribute("applications",applications);
        return "admin-dashboard";
    }
//
//    ye method  upar wala method jo  user form data show kar raha uska id utha ke
//    view-application page pe sari detail show kar raha hai ews-entity
//    ke data ko model me dala ke sari data ko  view page pe show kar raha hai

    @GetMapping("/view-application/{id}")
    public String viewApplication(@PathVariable Long id, Model model){
       EWSApplication app= applicationService.getApplicationById(id);
        System.out.println("admin view pe click kiya");
        model.addAttribute("app",app);
        return "application-view";
    }

    @GetMapping("/approve-application/{id}")
    public String approveApplication(@PathVariable Long id) throws Exception{
        applicationService.approveApplication(id);
        return "redirect:/admin-dashboard";
    }

    @PostMapping("/reject-application")
    public String rejectApplication(Long id, String reason){
        applicationService.rejectApplication(id, reason);
        return "admin-dashboard";
    }

//Approve page open

    @GetMapping("/approve-page")
    public String approvedPage(HttpSession session, Model model){

        if (session.getAttribute("admin")==null){
            return "admin-login";
        }
        List<EWSApplication> applications = adminService.getApprodeApplication();
        model.addAttribute("applications",applications);
        return "approved-page";
    }

//    Rejected page open

    @GetMapping("/reject-page")
    public String rejectedPage(HttpSession session, Model model){

        if (session.getAttribute("admin")==null){
            return "admin-login";
        }
        List<EWSApplication> applications = adminService.getRejectApplication();
        model.addAttribute("applications",applications);
        return "rejected-page";
    }

}
