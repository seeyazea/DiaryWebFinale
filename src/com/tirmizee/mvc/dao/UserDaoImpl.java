package com.tirmizee.mvc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.tirmizee.db.config.DBUtil;
import com.tirmizee.mvc.model.User;
import com.tirmizee.mvc.model.UserRole;

public class UserDaoImpl implements UserDao {

    private DBUtil dbUtil = new DBUtil();

    // REGISTER ----------------------------------------------------

    @Override
    public void register(User user) {
        String sql = "INSERT INTO users(fullname, email, password_hash) VALUES (?, ?, ?)";

        try (Connection con = dbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getFullname());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // FIND BY EMAIL ----------------------------------------------

    @Override
    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        User user = null;

        try (Connection con = dbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setFullname(rs.getString("fullname"));
                user.setEmail(rs.getString("email"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setCreatedAt(rs.getString("created_at"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    // FIND BY ID -------------------------------------------------

    @Override
    public User findById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        User user = null;

        try (Connection con = dbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setFullname(rs.getString("fullname"));
                user.setEmail(rs.getString("email"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setCreatedAt(rs.getString("created_at"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    // LOGIN CHECK -------------------------------------------------

    @Override
    public boolean isValid(String email, String password) {
        String sql = "SELECT id FROM users WHERE email = ? AND password_hash = ?";

        try (Connection con = dbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // FIND ROLE ---------------------------------------------------

    @Override
    public UserRole findUserByUserAndPass(String email, String password) {

        String sql = "SELECT u.id, u.fullname, u.email, r.role_name " +
                     "FROM users u " +
                     "JOIN roles r ON u.role_id = r.id " +
                     "WHERE u.email = ? AND u.password_hash = ?";

        try (Connection con = dbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                UserRole role = new UserRole();
                role.setId(rs.getInt("id"));
                role.setFullname(rs.getString("fullname"));
                role.setEmail(rs.getString("email"));
                role.setRoleName(rs.getString("role_name"));
                return role;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // UPDATE PROFILE ----------------------------------------------

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET fullname = ?, email = ? WHERE id = ?";

        try (Connection con = dbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getFullname());
            ps.setString(2, user.getEmail());
            ps.setInt(3, user.getId());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // CHANGE PASSWORD ---------------------------------------------

    @Override
    public void updatePassword(int id, String newPassword) {
        String sql = "UPDATE users SET password_hash = ? WHERE id = ?";

        try (Connection con = dbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setInt(2, id);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE ACCOUNT ----------------------------------------------

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection con = dbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
