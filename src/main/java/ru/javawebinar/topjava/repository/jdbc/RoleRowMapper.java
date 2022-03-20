package ru.javawebinar.topjava.repository.jdbc;

import org.hibernate.cache.spi.access.CachedDomainDataAccess;
import org.springframework.jdbc.core.RowMapper;
import ru.javawebinar.topjava.model.Role;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RoleRowMapper implements RowMapper<Role> {
    @Override
    public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
//        Map <Integer, List<Role>> map = new ConcurrentHashMap<>();
//        int id = rs.getInt("user_id");
//        Role role = Role.valueOf(rs.getString("role"));
//        map.computeIfPresent(id, (integer, roles) -> map.get(id).add(role), map.put(id, role));
        return (Role) rs.getObject("role");
    }
}
