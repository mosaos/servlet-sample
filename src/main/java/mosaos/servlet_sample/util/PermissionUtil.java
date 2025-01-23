package mosaos.servlet_sample.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import mosaos.servlet_sample.model.dto.NoteDto;
import mosaos.servlet_sample.model.entity.Note;
import mosaos.servlet_sample.model.entity.User;

public class PermissionUtil {

    /**
     * Get ( logged in ) current user.
     * @param req HttpServletRequest
     * @return current user
     */
    public static User getCurrentUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) return null;
        User currentUser = (User) session.getAttribute("user");
        return currentUser;
    }

    /**
     * Check if logged in user isAdmin.
     * @param req HttpServletRequest
     * @return return true if isAdmin
     */
    public static boolean isAdmin(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) return false;
        User operator = (User) session.getAttribute("user");
        return (operator.getIsAdmin());
    }

    /**
     * Check if operation is permitted.
     * @param req HttpServletRequest
     * @param user target user
     * @return return true if is permitted.
     */
    public static boolean isPermit(HttpServletRequest req, User user) {
        HttpSession session = req.getSession(false);
        if (session == null) return false;
        User operator = (User) session.getAttribute("user");
        if (operator.getIsAdmin() || operator.getId().equals(user.getId())) {
            return true;
        }
        return false;
    }

    /**
     * Check if operation is permitted.
     * @param req HttpServletRequest
     * @param user target user
     * @return return true if is permitted.
     */
    public static boolean isPermit(HttpServletRequest req, Note note) {
        HttpSession session = req.getSession(false);
        if (session == null) return false;
        User operator = (User) session.getAttribute("user");
        if (operator.getIsAdmin() || operator.getId().equals(note.getUserId())) {
            return true;
        }
        return false;
    }

    /**
     * Check if operation is permitted.
     * @param req HttpServletRequest
     * @param user target user
     * @return return true if is permitted.
     */
    public static boolean isPermit(HttpServletRequest req, NoteDto noteDto) {
        HttpSession session = req.getSession(false);
        if (session == null) return false;
        User operator = (User) session.getAttribute("user");
        if (operator.getIsAdmin() || operator.getId().equals(noteDto.getUserId())) {
            return true;
        }
        return false;
    }

}
