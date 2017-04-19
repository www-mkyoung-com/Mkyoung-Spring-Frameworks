In this article, we will show you three ways to get the current logged in username in Spring Security.

## 1\. SecurityContextHolder + Authentication.getName()

    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.ModelMap;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestMethod;

    @Controller
    public class LoginController {

      @RequestMapping(value="/login", method = RequestMethod.GET)
      public String printUser(ModelMap model) {

          Authentication auth = SecurityContextHolder.getContext().getAuthentication();
          String name = auth.getName(); //get logged in username

          model.addAttribute("username", name);
          return "hello";

      }
      //...

## 2\. SecurityContextHolder + User.getUsername()

    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.core.userdetails.User;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.ModelMap;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestMethod;

    @Controller
    public class LoginController {

      @RequestMapping(value="/login", method = RequestMethod.GET)
      public String printUser(ModelMap model) {

          User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
          String name = user.getUsername(); //get logged in username

          model.addAttribute("username", name);
          return "hello";

      }
      //...

## 3\. UsernamePasswordAuthenticationToken

This is more elegant solution, in runtime, Spring will injects `UsernamePasswordAuthenticationToken` into the `Principal`interface.

    import java.security.Principal;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.ModelMap;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestMethod;

    @Controller
    public class LoginController {

      @RequestMapping(value="/login", method = RequestMethod.GET)
      public String printWelcome(ModelMap model, Principal principal ) {

          String name = principal.getName(); //get logged in username
          model.addAttribute("username", name);
          return "hello";

      }
      //...

[http://www.mkyong.com/spring-security/get-current-logged-in-username-in-spring-security/](http://www.mkyong.com/spring-security/get-current-logged-in-username-in-spring-security/)
