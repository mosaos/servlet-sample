package mosaos.servlet_sample.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.extern.slf4j.Slf4j;
import mosaos.servlet_sample.db.DatabaseManager;
import mosaos.servlet_sample.exception.ApplicationException;
import mosaos.servlet_sample.model.entity.User;
import mosaos.servlet_sample.util.BCryptUtil;

@Slf4j
public class UserService {

    public static final int PAGE_SIZE = 20;

    private DatabaseManager dbManager = DatabaseManager.getInstance();

    /**
     * 認証する.
     * @param user_id ユーザID 
     * @param password パスワード
     * @return 認証OKならUser, NGならNullを返す
     */
    public User authenticate(String user_id, String password) {
        try (Connection con = dbManager.getConnection();
                PreparedStatement ps = selectUserByUserId(con, user_id);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String hashedPassword = rs.getString(4);
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                if (encoder.matches(password, hashedPassword)) {
                    return rsToUser(rs);
                }
            }
        } catch (Exception e) {
            throw new ApplicationException("login id=" + user_id + " authentication failed.", e);
        }
        return null;
    }

    public int create(final User user) {
        String sql = "INSERT INTO users (user_id, password, user_name, email, is_admin) VALUES (?,?,?,?,?);";
        try (Connection con = dbManager.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, user.getUserId());
                ps.setString(2, BCryptUtil.encode(user.getPassword()));
                ps.setString(3, user.getUserName());
                ps.setString(4, user.getEmail());
                ps.setBoolean(5, user.getIsAdmin());
                int count = ps.executeUpdate();
                con.commit();
                return count;
            } catch (Exception e) {
                con.rollback();
                log.error("create({}) failed.", user, e);
                return 0;
            }
        } catch (Exception e) {
            log.error("create({}) failed.", user, e);
            return 0;
        }
    }

    public int update(User user) {
        String sql = "UPDATE users SET user_name=?, email=?, is_admin=? WHERE id=?";
        try (Connection con = dbManager.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, user.getUserName());
                ps.setString(2, user.getEmail());
                ps.setBoolean(3, user.getIsAdmin());
                ps.setLong(4, user.getId());
                int count = ps.executeUpdate();
                con.commit();
                return count;
            } catch (Exception e) {
                con.rollback();
                log.error("update({}) failed.", user, e);
                return 0;
            }
        } catch (Exception e) {
            log.error("update({}) failed.", user, e);
            return 0;
        }
    }

    /**
     * uuid で検索する.
     * @param uuid UUID
     * @return User
     */
    public User getUserByUuid(String uuid) {
        try (Connection con = dbManager.getConnection();
                PreparedStatement ps = selectUserByUuid(con, uuid);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rsToUser(rs);
            }
        } catch (Exception e) {
            throw new ApplicationException("getUser(id=" + uuid + ") failed.", e);
        }
        return null;
    }

    /**
     * ユーザ一覧を返す.
     * @param pageNumber ページ番号
     * @return ユーザ一覧
     */
    public List<User> getUsers(int pageNumber) {
        int offset = (pageNumber - 1) * PAGE_SIZE;
        List<User> userList = new ArrayList<User>();
        try (Connection con = dbManager.getConnection();
                PreparedStatement ps = selectUsers(con, offset, PAGE_SIZE);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                userList.add(rsToUser(rs));
            }
            return userList;
        } catch (Exception e) {
            log.error("getUserList({}) failed.", pageNumber, e);
            return null;
        }
    }

    public long getTotalCount() {
        String query = "SELECT COUNT(*) AS total FROM users";
        try (Connection con = dbManager.getConnection();
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getLong("total");
            }
        } catch (Exception e) {
            log.error("getTotalCount failed.", e);
        }
        return 0;
    }

    /**
     * ResultSet を Userにして返す.
     * ResultSet は全フィールドを返している必要がある
     * @param rs ResultSet
     * @return User
     * @throws SQLException
     */
    private User rsToUser(final ResultSet rs) throws SQLException {
        User user = new User(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4),
                rs.getString(5), rs.getString(6), rs.getBoolean(7),
                rs.getTimestamp(8), rs.getTimestamp(9));
        return user;
    }

    private PreparedStatement selectUserByUuid(Connection con, String uuid) throws SQLException {
        String sql = "SELECT id, BIN_TO_UUID(uuid) as uuid, user_id, password, user_name, email, is_admin, created_at, updated_at FROM users WHERE BIN_TO_UUID(uuid)=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, uuid);
        return ps;
    }

    private PreparedStatement selectUserByUserId(Connection con, String userId) throws SQLException {
        String sql = "SELECT id, BIN_TO_UUID(uuid) as uuid, user_id, password, user_name, email, is_admin, created_at, updated_at FROM users WHERE user_id=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, userId);
        return ps;
    }

    private PreparedStatement selectUsers(Connection con, int offset, int limit) throws SQLException {
        String sql = "SELECT id, BIN_TO_UUID(uuid) as uuid, user_id, password, user_name, email, is_admin, created_at, updated_at FROM users ORDER BY id LIMIT ? OFFSET ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, limit);
        ps.setInt(2, offset);
        return ps;
    }
}