package hello.inbound;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("hallo")
    String getHello() {
        return "Hello Consumer";
    }
}
