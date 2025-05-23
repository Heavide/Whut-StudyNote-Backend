package com.example.studynotebackend.mapper;

import com.example.studynotebackend.domain.Review;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ReviewMapper {

    @Select("SELECT r.id, r.note_id AS noteId, r.user_id AS userId, u.username, r.rating, r.comment, r.created_at AS createdAt "
            + "FROM review r JOIN user_account u ON r.user_id = u.id WHERE r.note_id = #{noteId} ORDER BY r.created_at DESC")
    List<Review> findByNoteId(@Param("noteId") Long noteId);

    @Insert("INSERT INTO review(note_id, user_id, rating, comment) "
            + "VALUES(#{noteId}, #{userId}, #{rating}, #{comment})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @SelectKey(
            statement = "SELECT created_at FROM review WHERE id = LAST_INSERT_ID()",
            keyProperty = "createdAt",
            before = false,
            resultType = LocalDateTime.class
    )
    int insert(Review review);
}
