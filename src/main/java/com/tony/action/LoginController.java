package com.tony.action;

import com.alibaba.fastjson.JSON;
import com.tony.POJO.AccessTokeRet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.Null;
import java.util.Base64;
import java.util.List;

/**
 * Created by chnho02796 on 2017/11/6.
 */
@RestController
public class LoginController {

    @Autowired
    private RestTemplate restTemplate;

    private HttpHeaders getHeaders(){
        String plainCredentials="acme:acmesecret";
        String base64Credentials = Base64.getEncoder().encodeToString(plainCredentials.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Credentials);
        return headers;
    }

    private HttpHeaders getHeaders2(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        return headers;
    }

    @RequestMapping("/callback")
    public String homePage(@RequestParam String code){
        System.out.println("code = " + code);
        //http://localhost:8080/oauth/token?grant_type=authorization_code&client_id=acme&redirect_uri=http://localhost:9000/callback&code=m28UuO
        String tokenString = "http://localhost:8080/oauth/token?grant_type=authorization_code&client_id=acme&redirect_uri=http://localhost:9000/callback&code="+code;
        System.out.println("tokenString = " + tokenString);
        HttpEntity<String> request = new HttpEntity<String>(getHeaders());
        ResponseEntity<String> response = restTemplate.exchange(tokenString, HttpMethod.POST, request, String.class);
        System.out.println("tokenStringrep = " + response.getBody());
        List<AccessTokeRet> ret = JSON.parseArray("["+response.getBody()+"]",AccessTokeRet.class);
        System.out.println("ret = " + ret.get(0).getAccess_token());

//        String resourceUrl = "http://localhost:8080/user?access_token="+ret.get(0).getAccess_token();
        String resourceUrl = "http://localhost:8080/user";
        System.out.println("resourceUrl = " + resourceUrl);
        HttpEntity<String> request2 = new HttpEntity<String>(getHeaders2(ret.get(0).getAccess_token()));
//        ResponseEntity<String> response2 = restTemplate.exchange(resourceUrl, HttpMethod.GET, request2, String.class);
//        System.out.println("ret2 = " + response2.getBody());
//        return response2.getBody();
        return "111";
    }


}
