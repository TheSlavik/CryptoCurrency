package com.project.CryptoCurrency.DAO;

import com.project.CryptoCurrency.entity.Currency;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class NotificationDAO {

    private final JdbcTemplate jdbcTemplate;

    public NotificationDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> getNotifications() {
        return jdbcTemplate.queryForList("select * from (select currencies.symbol, notifications.username, " +
                "round(currencies.price/(notifications.current_price/100)-100, 2) as percent from notifications " +
                "join currencies on notifications.symbol=currencies.symbol) as a where abs(percent)>=1");
    }

    public void createNotification(String username, Currency currency) {
        jdbcTemplate.update("insert into notifications (current_price, symbol, username) values(?, ?, ?)", currency.getPrice(), currency.getSymbol(), username);
    }

    public void deleteNotification(String username, String symbol) {
        jdbcTemplate.update("delete from notifications where username=? and symbol=?", username, symbol);
    }
}
