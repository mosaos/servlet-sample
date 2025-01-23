package mosaos.servlet_sample.model.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mosaos.servlet_sample.model.entity.Note;
import mosaos.servlet_sample.model.entity.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteDto implements Serializable {
    private Long id;
    private String uuid;
    private Long userId;
    private User user;
    private String title;
    private String content;
    private String tags;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Note toNote() {
        Note note = new Note(id, uuid, userId, title, content, tags, createdAt, updatedAt);
        return note;
    }
    public List<String> getTagList() {
        String[] tagArray = tags.split(",");
        return Arrays.asList(tagArray);
    }
    public boolean isRecentUpdated(int days) {
        return isRecent(updatedAt, days);
    }
    public boolean isRecentCreated(int days) {
        return isRecent(createdAt, days);
    }
    private static boolean isRecent(Timestamp tm, int days) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dateTime = tm.toLocalDateTime();
        long daysBetween = ChronoUnit.DAYS.between(dateTime, now);
        return daysBetween <= days;
    }
}
