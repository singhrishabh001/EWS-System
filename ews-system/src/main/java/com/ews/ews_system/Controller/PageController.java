package com.ews.ews_system.Controller;

//import org.springframework.boot.security.autoconfigure.SecurityProperties;
import com.ews.ews_system.Model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class PageController {
    @GetMapping("/")
    public String home(HttpSession session , Model model){

        User user =(User) session.getAttribute("user");
        model.addAttribute("user",user);
        return "index";


    }
    @GetMapping("/login-section")
    public String loginSelection(){
        return "login-selection";
    }

    @GetMapping("user-login")
    public String userLogin(){
        return "user-login";
    }

    @GetMapping("admin-login")
    public String adminLogin(){
        return "admin-login";
    }

    @GetMapping("/register")
    public String register(){
        return"register";
    }

    @GetMapping("/apply")
    public String applyForm(HttpSession session){
        if (session.getAttribute("user")==null) {
           return "redirect:/user-login";
        }
        return "apply-form";
    }

    @GetMapping("/logoutUser")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }


}

