package ru.yandex.practicum.filmorate.dal.dao.mappers;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserMapper implements ResultSetExtractor<User> {
    @Override
    public User extractData(final ResultSet rs) throws SQLException, DataAccessException {
        User user = null;
        while (rs.next()) {
            user = new User();
            user.setId(rs.getLong("user_id"));
            user.setEmail(rs.getString("email"));
            user.setLogin(rs.getString("login"));
            user.setName(rs.getString("name"));
            user.setBirthday(rs.getDate("birthday").toLocalDate());
        }
        return user;
    }
}