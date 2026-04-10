package rent.custome.demo.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        log.error("Loi khong xu ly duoc: {}", e.getMessage(), e);
        model.addAttribute("error", e.getMessage());
        return "error";
    }
}
