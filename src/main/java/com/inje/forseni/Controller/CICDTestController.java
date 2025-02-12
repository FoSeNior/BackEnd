package com.inje.forseni.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CICDTestController {
    @GetMapping("hello")
    public String helloWorld(){
        return "CICD동작 확인";
    }
}
