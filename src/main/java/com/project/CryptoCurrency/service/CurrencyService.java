package com.project.CryptoCurrency.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.CryptoCurrency.DAO.CurrencyDAO;
import com.project.CryptoCurrency.DAO.NotificationDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class CurrencyService {

    private final RestTemplate restTemplate;
    private final CurrencyDAO currencyDAO;
    private final NotificationDAO notificationDAO;
    @Value("classpath:config.json")
    private Resource config;

    public CurrencyService(RestTemplateBuilder restTemplateBuilder, CurrencyDAO currencyDAO, NotificationDAO notificationDAO) {
        this.restTemplate = restTemplateBuilder.build();
        this.currencyDAO = currencyDAO;
        this.notificationDAO = notificationDAO;
    }

    @Scheduled(cron = "0 * * * * *")
    public void getCurrencies() {
        try {
            StringBuilder url = new StringBuilder("https://api.coinlore.net/api/ticker/?id=");
            String s = new String(Files.readAllBytes(config.getFile().toPath()));
            JsonNode jsonNode = new ObjectMapper().readTree(s).get("currencies");
            for (JsonNode node : jsonNode) {
                url.append(node.get("id").asText());
                url.append(",");
            }
            url.deleteCharAt(url.lastIndexOf(","));
            String response = restTemplate.getForObject(url.toString(), String.class);
            currencyDAO.updateCurrencies(response);
            Logger logger = Logger.getLogger("Notification");
            for (Map<String, Object> map : notificationDAO.getNotifications()) {
                logger.warning(String.format("Currency %s has changed for the user %s by %s%%", map.get("symbol"), map.get("username"), map.get("percent")));
                notificationDAO.deleteNotification(map.get("username").toString(), map.get("symbol").toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
