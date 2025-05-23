package com.example.studynotebackend.mapper;

import com.example.studynotebackend.domain.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface NoteMapper {

    @SelectProvider(type = NoteMapper.class, method = "buildFindPageSql")
    List<Note> findPage(
            @Param("offset")  int offset,
            @Param("limit")   int limit,
            @Param("keyword") String keyword
    );

    @SelectProvider(type = NoteMapper.class, method = "buildCountSql")
    int count(@Param("keyword") String keyword);

    @Select("SELECT id, user_id AS userId, title, content, created_at AS createdAt, updated_at AS updatedAt "
            + "FROM note WHERE id = #{id}")
    Note findById(@Param("id") Long id);

    @Insert("INSERT INTO note(user_id, title, content) VALUES(#{userId}, #{title}, #{content})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Note note);

    @Update("UPDATE note SET title = #{title}, content = #{content}, updated_at = NOW() WHERE id = #{id}")
    int update(Note note);

    @Delete("DELETE FROM note WHERE id = #{id}")
    int delete(@Param("id") Long id);


    static String buildFindPageSql(Map<String, Object> params) {
        Integer offset  = (Integer) params.get("offset");
        Integer limit   = (Integer) params.get("limit");
        String  keyword = (String)  params.get("keyword");

        StringBuilder sql = new StringBuilder()
                .append("SELECT id, user_id AS userId, title, content, created_at AS createdAt, updated_at AS updatedAt ")
                .append("FROM note");

        if (keyword != null && !keyword.isBlank()) {
            sql.append(" WHERE title LIKE CONCAT('%', #{keyword}, '%')")
                    .append(" OR content LIKE CONCAT('%', #{keyword}, '%')");
        }

        sql.append(" ORDER BY created_at DESC")
                .append(" LIMIT ").append(offset).append(", ").append(limit);

        return sql.toString();
    }

    static String buildCountSql(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder()
                .append("SELECT COUNT(*) FROM note");

        String keyword = (String) params.get("keyword");
        if (keyword != null && !keyword.isBlank()) {
            sql.append(" WHERE title LIKE CONCAT('%', #{keyword}, '%')")
                    .append(" OR content LIKE CONCAT('%', #{keyword}, '%')");
        }

        return sql.toString();
    }
}
