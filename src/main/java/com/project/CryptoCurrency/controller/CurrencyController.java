package com.project.CryptoCurrency.controller;

import com.project.CryptoCurrency.DAO.CurrencyDAO;
import com.project.CryptoCurrency.DAO.NotificationDAO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/currencies")
public class CurrencyController {

    private final CurrencyDAO currencyDAO;
    private final NotificationDAO notificationDAO;

    public CurrencyController(CurrencyDAO currencyDAO, NotificationDAO notificationDAO) {
        this.currencyDAO = currencyDAO;
        this.notificationDAO = notificationDAO;
    }

    @GetMapping
    public String getCurrencies(Model model) {
        model.addAttribute("list", currencyDAO.getCurrencies());
        return "currencies";
    }

    @GetMapping("/{symbol}")
    public String show(@PathVariable("symbol") String symbol, Model model) {
        model.addAttribute("currency", currencyDAO.getCurrency(symbol));
        return "currency";
    }

    @PostMapping("/notify")
    public String notify(@RequestParam String username, @RequestParam String symbol) {
        try {
            notificationDAO.createNotification(username, currencyDAO.getCurrency(symbol));
            return "redirect:/currencies/success";
        } catch (Exception e) {
            return "redirect:/currencies/error";
        }
    }

    @GetMapping("/success")
    public String success() {
        return "success";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }
}
