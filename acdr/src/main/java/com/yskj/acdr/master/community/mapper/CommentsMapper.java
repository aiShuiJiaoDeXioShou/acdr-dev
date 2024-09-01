package com.yskj.acdr.master.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yskj.acdr.master.community.entity.Comments;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 林河
 * @since 2024-08-12
 */
@Mapper
public interface CommentsMapper extends BaseMapper<Comments> {

    @Select("""
                WITH RECURSIVE comment_tree AS (
                    SELECT id, post_id, user_id, content, created_time, be_comments_id
                    FROM acdr_comments
                    WHERE id = #{id}
                    UNION ALL
                    SELECT c.id, c.post_id, c.user_id, c.content, c.created_time, c.be_comments_id
                    FROM acdr_comments c
                    INNER JOIN comment_tree ct ON c.be_comments_id = ct.id
                )
                SELECT * FROM comment_tree;
            """)
    List<Comments> getCommentTree(@Param("id") Long id);

    @Select("""
                WITH RECURSIVE comment_tree AS (
                    SELECT id, post_id, user_id, content, created_time, be_comments_id
                    FROM acdr_comments
                    WHERE id = #{id}
                    UNION ALL
                    SELECT c.id, c.post_id, c.user_id, c.content, c.created_time, c.be_comments_id
                    FROM acdr_comments c
                    INNER JOIN comment_tree ct ON c.be_comments_id = ct.id
                )
                SELECT *
                FROM comment_tree
                ORDER BY created_time ASC
                LIMIT #{limit} OFFSET #{offset};
            """)
    List<Comments> getCommentTreeWithPagination(@Param("id") Long id, @Param("limit") int limit, @Param("offset") int offset);

}
