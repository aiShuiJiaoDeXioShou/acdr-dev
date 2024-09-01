package cn.iocoder.yudao.module.master.community.controller;

import cn.iocoder.yudao.module.utils.LoginUtil;
import cn.hutool.core.map.MapUtil;
import cn.iocoder.yudao.module.common.response.GlobalResponse;
import cn.iocoder.yudao.module.master.community.entity.*;
import cn.iocoder.yudao.module.master.community.mapper.*;
import cn.iocoder.yudao.module.master.user.entity.Users;
import cn.iocoder.yudao.module.master.user.mapper.UsersMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 林河
 * @since 2024-08-12
 */
@Api(tags = "社区管理")
@RestController
@RequestMapping("/pet/posts")
@Validated
public class CommunityController {

    @Resource
    private CommentsMapper commentsMapper;
    @Resource
    private FollowsMapper followsMapper;
    @Resource
    private PhotosMapper photosMapper;
    @Resource
    private PostsMapper postsMapper;
    @Resource
    private LikesMapper likesMapper;
    @Resource
    private FavoritesMapper favoritesMapper;
    @Resource
    private UsersMapper usersMapper;

    /**
     * 发帖
     *
     * @param post 帖子对象，包含标题、内容、类型、地点等信息
     * @return 返回成功创建的帖子对象
     *
     * @apiNote 此接口用于创建新的帖子，要求用户必须登录。创建时会自动设置帖子发布者的ID和创建时间。
     */
    @PostMapping("/create")
    public GlobalResponse<Posts> createPost(@Valid @RequestBody Posts post) {
        post.setUserId(LoginUtil.getLoginIdAsLong());
        post.setCreatedTime(LocalDateTime.now());
        postsMapper.insert(post);
        return GlobalResponse.success(post);
    }

    /**
     * 查看帖子详情
     *
     * @param id 帖子ID，从路径参数中获取
     * @return 返回帖子详情，包括帖子内容、评论、图片、点赞、关注、收藏数量
     *
     * @apiNote 此接口用于获取指定帖子的详情，任何用户都可以访问。返回的数据包含帖子内容及其相关的所有信息。
     */
    @GetMapping("/{id}")
    public GlobalResponse<Map<String, Object>> getPost(@PathVariable Long id) {
        // 获取帖子信息
        Posts post = postsMapper.selectById(id);
        if (post == null) {
            return GlobalResponse.failure("帖子不存在");
        }

        // 获取帖子下的所有评论
        List<Comments> comments = commentsMapper.selectList(new QueryWrapper<Comments>().eq("post_id", id));

        // 获取帖子下的所有图片
        List<Photos> photos = photosMapper.selectList(new QueryWrapper<Photos>().eq("post_id", id));

        // 获取点赞数量
        long likeCount = likesMapper.selectCount(new QueryWrapper<Likes>().eq("post_id", id));

        // 获取关注数量
        long followCount = followsMapper.selectCount(new QueryWrapper<Follows>().eq("following_id", post.getUserId()));

        // 获取收藏数量
        long favoriteCount = favoritesMapper.selectCount(new QueryWrapper<Favorites>().eq("post_id", id));

        // 使用 Hutool 的 MapUtil 构建返回的 Map
        Map<String, Object> response = MapUtil.builder(new HashMap<String, Object>())
                .put("post", post)
                .put("comments", comments)
                .put("photos", photos)
                .put("likeCount", likeCount)
                .put("followCount", followCount)
                .put("favoriteCount", favoriteCount)
                .build();

        return GlobalResponse.success(response);
    }

    /**
     * 删除帖子
     *
     * @param id 帖子ID，从路径参数中获取
     * @return 返回删除操作的结果
     *
     * @apiNote 此接口用于删除指定帖子，要求用户必须登录且只能删除自己发布的帖子。
     * @throws GlobalResponse.failure 当帖子不存在或当前登录用户无权删除帖子时抛出
     */
    @DeleteMapping("/{id}")
    public GlobalResponse<String> deletePost(@PathVariable Long id) {
        Posts post = postsMapper.selectById(id);
        if (post == null || !post.getUserId().equals(LoginUtil.getLoginIdAsLong())) {
            return GlobalResponse.failure("无权删除帖子或帖子不存在");
        }
        postsMapper.deleteById(id);
        return GlobalResponse.success("删除成功");
    }

    /**
     * 添加评论
     *
     * @param postId 帖子ID，从路径参数中获取
     * @param comment 评论对象，包含评论内容
     * @return 返回成功添加的评论对象
     *
     * @apiNote 此接口用于在指定帖子下添加评论，要求用户必须登录。添加时会自动设置评论的发布者ID和创建时间。
     * @throws GlobalResponse.failure 当帖子不存在时抛出
     */
    @PostMapping("/{postId}/comments")
    public GlobalResponse<Comments> addComment(@PathVariable Long postId, @Valid @RequestBody Comments comment) {
        Posts post = postsMapper.selectById(postId);
        if (post == null) {
            return GlobalResponse.failure("帖子不存在");
        }
        comment.setPostId(postId);
        comment.setUserId(LoginUtil.getLoginIdAsLong());
        comment.setCreatedTime(LocalDateTime.now());
        commentsMapper.insert(comment);
        return GlobalResponse.success(comment);
    }


    /**
     * @author 林河
     * @param postId 获取评论的ID
     * 获取帖子的评论列表
     */
    @GetMapping("/{postId}/comments")
    public GlobalResponse<List<Comments>> getComments(@PathVariable Long postId) {
        List<Comments> comments = commentsMapper.selectList(new QueryWrapper<Comments>().eq("post_id", postId));
        return GlobalResponse.success(comments);
    }

    /**
     * @author 林河
     * @param followingId 关注对象ID
     * 关注用户
     */
    @PostMapping("/follow")
    public GlobalResponse<String> followUser(@RequestParam Long followingId) {
        Long followerId = LoginUtil.getLoginIdAsLong();

        // 检查是否已经关注过这个用户
        QueryWrapper<Follows> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("follower_id", followerId)
                .eq("following_id", followingId);
        Follows existingFollow = followsMapper.selectOne(queryWrapper);

        if (existingFollow != null) {
            return GlobalResponse.failure("你已经关注过该用户");
        }

        // 如果没有关注过，则执行关注操作
        Follows follow = new Follows();
        follow.setFollowerId(followerId);
        follow.setFollowingId(followingId);
        follow.setCreatedTime(LocalDateTime.now());
        followsMapper.insert(follow);

        return GlobalResponse.success("关注成功");
    }

    /**
     * @author 林河
     * 用户对帖子或评论点赞
     *
     * @param postId    帖子ID（如果是对帖子的点赞）
     * @param commentId 评论ID（如果是对评论的点赞）
     * @return 点赞操作的结果
     */
    @PostMapping("/like")
    public GlobalResponse<String> like(@RequestParam(required = false) Long postId,
                                       @RequestParam(required = false) Long commentId) {
        Long userId = LoginUtil.getLoginIdAsLong();

        // 确保用户不能对同一帖子或评论重复点赞
        QueryWrapper<Likes> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq(postId != null, "post_id", postId)
                .eq(commentId != null, "comment_id", commentId);
        Likes existingLike = likesMapper.selectOne(queryWrapper);

        if (existingLike != null) {
            return GlobalResponse.failure("你已经点赞过该内容");
        }

        // 执行点赞操作
        Likes like = new Likes();
        like.setUserId(userId);
        like.setPostId(postId);
        like.setCommentId(commentId);
        like.setCreatedTime(LocalDateTime.now());
        likesMapper.insert(like);

        return GlobalResponse.success("点赞成功");
    }

    /**
     * @author 林河
     * 用户取消对帖子或评论的点赞
     *
     * @param postId    帖子ID（如果是对帖子的取消点赞）
     * @param commentId 评论ID（如果是对评论的取消点赞）
     * @return 取消点赞操作的结果
     */
    @DeleteMapping("/unlike")
    public GlobalResponse<String> unlike(@RequestParam(required = false) Long postId,
                                         @RequestParam(required = false) Long commentId) {
        Long userId = LoginUtil.getLoginIdAsLong();

        QueryWrapper<Likes> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq(postId != null, "post_id", postId)
                .eq(commentId != null, "comment_id", commentId);

        int deleted = likesMapper.delete(queryWrapper);
        if (deleted > 0) {
            return GlobalResponse.success("取消点赞成功");
        } else {
            return GlobalResponse.failure("取消点赞失败，可能你并没有点赞该内容");
        }
    }

    /**
     * @author 林河
     * 用户收藏帖子
     *
     * @param postId 帖子ID
     * @return 收藏操作的结果
     */
    @PostMapping("/favorite")
    public GlobalResponse<String> favorite(@RequestParam Long postId) {
        Long userId = LoginUtil.getLoginIdAsLong();

        // 确保用户不能重复收藏同一个帖子
        QueryWrapper<Favorites> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("post_id", postId);
        Favorites existingFavorite = favoritesMapper.selectOne(queryWrapper);

        if (existingFavorite != null) {
            return GlobalResponse.failure("你已经收藏过该帖子");
        }

        // 执行收藏操作
        Favorites favorite = new Favorites();
        favorite.setUserId(userId);
        favorite.setPostId(postId);
        favorite.setCreatedTime(LocalDateTime.now());
        favoritesMapper.insert(favorite);

        return GlobalResponse.success("收藏成功");
    }

    /**
     * @author 林河
     * 用户取消收藏帖子
     *
     * @param postId 帖子ID
     * @return 取消收藏操作的结果
     */
    @DeleteMapping("/unfavorite")
    public GlobalResponse<String> unfavorite(@RequestParam Long postId) {
        Long userId = LoginUtil.getLoginIdAsLong();

        QueryWrapper<Favorites> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("post_id", postId);

        int deleted = favoritesMapper.delete(queryWrapper);
        if (deleted > 0) {
            return GlobalResponse.success("取消收藏成功");
        } else {
            return GlobalResponse.failure("取消收藏失败，可能你并没有收藏该帖子");
        }
    }


    /**
     * 上传图片（添加到帖子的相册中）
     *
     * @param postId 帖子ID，从路径参数中获取
     * @param photo  图片信息，包含图片URL等信息
     * @return 返回上传的图片信息或失败原因
     *
     * @apiNote 此接口用于上传图片到指定帖子的相册中，只有帖子所有者可以上传图片。
     * @throws GlobalResponse.failure 当帖子不存在或用户无权上传图片时抛出
     */
    @PostMapping("/{postId}/photos")
    public GlobalResponse<Photos> uploadPhoto(@PathVariable Long postId, @RequestBody Photos photo) {
        // 获取帖子信息
        Posts post = postsMapper.selectById(postId);

        // 验证帖子是否存在且用户是否有权限上传图片
        if (post == null || !post.getUserId().equals(LoginUtil.getLoginIdAsLong())) {
            return GlobalResponse.failure("无权上传图片或帖子不存在");
        }

        // 设置图片所属的帖子ID和创建时间
        photo.setPostId(postId);
        photo.setCreatedTime(LocalDateTime.now());

        // 将图片信息插入数据库
        photosMapper.insert(photo);

        // 返回上传成功的图片信息
        return GlobalResponse.success(photo);
    }

    /**
     * 获取帖子的相册图片
     *
     * @param postId 帖子ID，从路径参数中获取
     * @return 返回帖子的所有图片列表
     *
     * @apiNote 此接口用于获取指定帖子的所有相册图片信息，任何用户都可以访问。
     */
    @GetMapping("/{postId}/photos")
    public GlobalResponse<List<Photos>> getPhotos(@PathVariable Long postId) {
        // 根据帖子ID查询所有关联的图片
        List<Photos> photos = photosMapper.selectList(new QueryWrapper<Photos>().eq("post_id", postId));

        // 返回图片列表
        return GlobalResponse.success(photos);
    }


    /**
     * 获取当前用户所有关注的用户，隐藏这些用户的私密信息
     *
     * @return 返回当前用户关注的用户列表，隐藏私密信息
     *
     * @author 林河
     */
    @GetMapping("/followed-users")
    public GlobalResponse<List<Map<String, Object>>> getFollowedUsers() {
        Long userId = LoginUtil.getLoginIdAsLong();

        // 查询所有当前用户关注的用户ID
        List<Long> followedUserIds = followsMapper.selectList(
                        new QueryWrapper<Follows>().eq("follower_id", userId))
                .stream()
                .map(Follows::getFollowingId)
                .collect(Collectors.toList());

        // 根据用户ID列表查询用户信息
        List<Users> followedUsers = usersMapper.selectBatchIds(followedUserIds);

        // 构建返回的数据，隐藏私密信息
        List<Map<String, Object>> result = followedUsers.stream()
                .map(user -> MapUtil.builder(new HashMap<String, Object>())
                        .put("id", user.getId())
                        .put("name", user.getName())
                        .put("avatar", user.getAvatar())
                        .put("nickname", user.getNickname())
                        .put("sex", user.getSex())
                        .build())
                .collect(Collectors.toList());

        return GlobalResponse.success(result);
    }
}

