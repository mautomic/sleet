package service;

import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.Scanner;

public abstract class Service {

    RestTemplate restTemplate;
    final String API_URL = "https://api.tdameritrade.com/v1/marketdata";
    String API_KEY = "";

    {
        try {
            Scanner sc = new Scanner(new File(getClass().getClassLoader().getResource("credentials.cfg").getFile()));
            API_KEY = sc.next();
            System.out.println("key is " + API_KEY);

        } catch(Exception e) {
            System.out.println("No credentials config found");
        }
    }
}
