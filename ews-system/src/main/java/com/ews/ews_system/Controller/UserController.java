package com.ews.ews_system.Controller;

import com.ews.ews_system.Model.User;
import com.ews.ews_system.Service.UserService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/registers")
    public String register(User user){
        userService.saveUser(user);
        return "user-login";
    }

    @PostMapping("/userlogins")
    public String loginUser(@RequestParam String email ,
                            @RequestParam String password ,
                            HttpSession session,
                            Model model){
        User user = userService.login(email , password);
        if(user!= null){
            session.setAttribute("user", user);
            return "redirect:/";
        }
        model.addAttribute("error","Wrong Email or Password <br/>If You do not have an account, </br>Please Create an Account first." );
        return "user-login";
    }

}
