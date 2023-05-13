package com.project.CryptoCurrency.DAO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.CryptoCurrency.entity.Currency;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class CurrencyDAO {

    private final JdbcTemplate jdbcTemplate;

    public CurrencyDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void updateCurrencies(String currencies) {
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(currencies);
            for (JsonNode node : jsonNode) {
                String id = node.get("id").asText();
                double priceUsd = node.get("price_usd").asDouble();
                String symbol = node.get("symbol").asText();
                jdbcTemplate.update("replace into currencies values(?, ?, ?)", id, priceUsd, symbol);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getCurrencies() {
        List<String> currencies = new ArrayList<>();
        jdbcTemplate.queryForList("select symbol from currencies")
                .forEach(x -> currencies.add(x.get("symbol").toString()));
        return currencies;
    }

    public Currency getCurrency(String symbol) {
        Map<String, Object> stringObjectMap = jdbcTemplate.queryForMap("select * from currencies where symbol = ?", symbol);
        Currency currency = new Currency();
        currency.setId((Integer) stringObjectMap.get("id"));
        currency.setPrice((Double) stringObjectMap.get("price"));
        currency.setSymbol((String) stringObjectMap.get("symbol"));
        return currency;
    }
}
