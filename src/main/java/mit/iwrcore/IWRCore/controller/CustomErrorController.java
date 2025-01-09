package mit.iwrcore.IWRCore.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        // 404 오류가 발생하면 /main 페이지로 리디렉션
        return "redirect:/main";
    }
}
