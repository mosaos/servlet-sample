package mosaos.servlet_sample.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import mosaos.servlet_sample.db.DatabaseManager;
import mosaos.servlet_sample.exception.ApplicationException;
import mosaos.servlet_sample.model.dto.NoteDto;
import mosaos.servlet_sample.model.entity.Note;
import mosaos.servlet_sample.model.entity.User;
import mosaos.servlet_sample.util.StringUtil;

@Slf4j
public class NoteService {

    public static final int PAGE_SIZE = 10;

    private DatabaseManager dbManager = DatabaseManager.getInstance();

    /**
     * Insert Note record.
     * @param note Note
     * @return inserted record count
     */
    public int create(final Note note) {
        String sql = "INSERT INTO notes (user_id, title, content, tags) VALUES(?,?,?,?);";
        try (Connection con = dbManager.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setLong(1, note.getUserId());
                ps.setString(2, note.getTitle());
                ps.setString(3, note.getContent());
                ps.setString(4, note.getTags());
                int count = ps.executeUpdate();
                con.commit();
                return count;
            } catch (Exception e) {
                con.rollback();
                log.error("create({}) failed.", note, e);
                return 0;
            }
        } catch (Exception e) {
            log.error("create({}) failed.", note, e);
            return 0;
        }
    }

    /**
     * Update Note record.
     * @param note Note
     * @return updated record count
     */
    public int update(Note note) {
        String sql = "UPDATE notes SET title=?, content=?, tags=? WHERE id=?";
        try (Connection con = dbManager.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, note.getTitle());
                ps.setString(2, note.getContent());
                ps.setString(3, note.getTags());
                ps.setLong(4, note.getId());
                int count = ps.executeUpdate();
                con.commit();
                return count;
            } catch (Exception e) {
                con.rollback();
                log.error("update({}) failed.", note, e);
                return 0;
            }
        } catch (Exception e) {
            log.error("update({}) failed.", note, e);
            return 0;
        }
    }

    /**
     * Search a note with uuid.
     * @param uuid UUID
     * @return User
     */
    public NoteDto getNoteByUuid(final String uuid) {
        try (Connection con = dbManager.getConnection();
                PreparedStatement ps = selectNoteByUuid(con, uuid);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rsToNoteDto(rs);
            }
        } catch (Exception e) {
            throw new ApplicationException("getNote(uuid=" + uuid + ") failed.", e);
        }
        return null;
    }

    /**
     * Get note list.
     * @param pageNumber page number
     * @return note list
     */
    public List<NoteDto> getNotes(int pageNumber) {
        int offset = (pageNumber - 1) * PAGE_SIZE;
        List<NoteDto> noteList = new ArrayList<NoteDto>();
        try (Connection con = dbManager.getConnection();
                PreparedStatement ps = selectNotes(con, offset, PAGE_SIZE);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                noteList.add(rsToNoteDto(rs));
            }
            return noteList;
        } catch (Exception e) {
            log.error("getNoteList({}) failed.", pageNumber, e);
            return null;
        }
    }

    public List<NoteDto> searchNotes(String keyword, String tag, int pageNumber) {
        int offset = (pageNumber - 1) * PAGE_SIZE;
        List<NoteDto> noteList = new ArrayList<NoteDto>();
        try (Connection con = dbManager.getConnection();
                PreparedStatement ps = selectNotesByQuery(con, keyword, tag, offset, PAGE_SIZE);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                noteList.add(rsToNoteDto(rs));
            }
            return noteList;
        } catch (Exception e) {
            log.error("getNoteList({}) failed.", pageNumber, e);
            return null;
        }
    }

    /**
     * Get count of all notes.
     * @return
     */
    public long getTotalCount() {
        String query = "SELECT COUNT(*) AS total FROM notes";
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
     * Convert from ResultSet to User.
     * ResultSet must have contain all fields of a record.
     * @param rs ResultSet
     * @return User
     * @throws SQLException
     */
    private NoteDto rsToNoteDto(final ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("user_id"));
        user.setUserId(rs.getString("user_account"));
        user.setUserName(rs.getString("user_name"));
        NoteDto noteDto = new NoteDto(rs.getLong("id"), rs.getString("uuid"), rs.getLong("user_id"), user,
                rs.getString("title"), rs.getString("content"), rs.getString("tags"),
                rs.getTimestamp("created_at"), rs.getTimestamp("updated_at"));
        return noteDto;
    }

    private PreparedStatement selectNoteByUuid(final Connection con, final String uuid) throws SQLException {
        String sql = "SELECT notes.id, BIN_TO_UUID(notes.uuid) as uuid, notes.user_id, title, content, tags, notes.created_at, notes.updated_at, users.user_id as user_account, users.user_name FROM notes INNER JOIN users ON notes.user_id=users.id WHERE BIN_TO_UUID(notes.uuid)=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, uuid);
        return ps;
    }

    private PreparedStatement selectNoteByNoteId(final Connection con, final String noteId) throws SQLException {
        String sql = "SELECT notes.id, BIN_TO_UUID(notes.uuid) as uuid, notes.user_id, title, content, tags, notes.created_at, notes.updated_at, users.user_id as user_account, users.user_name FROM notes INNER JOIN users ON notes.user_id=users.id WHERE notes.id=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, noteId);
        return ps;
    }

    private PreparedStatement selectNotes(final Connection con, final int offset, final int limit) throws SQLException {
        String sql = "SELECT notes.id, BIN_TO_UUID(notes.uuid) as uuid, notes.user_id, title, content, tags, notes.created_at, notes.updated_at, users.user_id as user_account, users.user_name FROM notes INNER JOIN users ON notes.user_id=users.id ORDER BY notes.created_at DESC LIMIT ? OFFSET ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, limit);
        ps.setInt(2, offset);
        return ps;
    }

    private PreparedStatement selectNotesByQuery(final Connection con, final String keyword, final String tag,
            final int offset, final int limit) throws SQLException {
        String sql = "SELECT notes.id, BIN_TO_UUID(notes.uuid) as uuid, notes.user_id, title, content, tags, notes.created_at, notes.updated_at, users.user_id as user_account, users.user_name FROM notes INNER JOIN users ON notes.user_id=users.id WHERE content LIKE ? AND tags LIKE ? ORDER BY id LIMIT ? OFFSET ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, "%" + keyword + "%");
        ps.setString(2, "%" + tag + "%");
        ps.setInt(3, limit);
        ps.setInt(4, offset);
        return ps;
    }

    /**
     * Summarize content of List
     * @return
     */
    public List<NoteDto> summarize(List<NoteDto> noteList) {
        List<NoteDto> summarizedList = new ArrayList<NoteDto>();
        for (NoteDto noteDto : noteList) {
            noteDto.setContent(StringUtil.getFirstNLines(noteDto.getContent(), 10));
            summarizedList.add(noteDto);
        }
        return summarizedList;
    }
}