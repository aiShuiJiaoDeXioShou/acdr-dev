package com.yskj.acdr.master.community.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yskj.acdr.common.response.GlobalResponse;
import com.yskj.acdr.master.community.entity.*;
import com.yskj.acdr.master.community.mapper.*;
import com.yskj.acdr.master.community.service.PostsService;
import com.yskj.acdr.master.user.entity.Users;
import com.yskj.acdr.master.user.mapper.UsersMapper;
import io.swagger.annotations.Api;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
@RequestMapping("/posts")
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
    private PostsService postsService;

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
     * @apiNote 此接口用于创建新的帖子，要求用户必须登录。创建时会自动设置帖子发布者的ID和创建时间。
     */
    @PostMapping("/create")
    @Transactional
    public GlobalResponse<Posts> createPost(@Valid @RequestBody Posts post) {
        post.setUserId(StpUtil.getLoginIdAsLong());
        post.setCreatedTime(LocalDateTime.now());
        postsMapper.insert(post);
        for (String url : post.getImages()) {
            photosMapper.insert(
                    new Photos().setPhotoUrl(url)
                            .setPostId(post.getId())
            );
        }
        return GlobalResponse.success(post);
    }

    private Map<String, Object> assemblePost(Posts p, Users users) {
        // 获取该帖子所有的点赞、喜欢、和评论数量
        Long likes = likesMapper.selectCount(new LambdaQueryWrapper<Likes>().eq(Likes::getPostId, p.getId()));
        Long comments = commentsMapper.selectCount(new LambdaQueryWrapper<Comments>().eq(Comments::getPostId, p.getId()));
        Long favorites = favoritesMapper.selectCount(new LambdaQueryWrapper<Favorites>().eq(Favorites::getPostId, p.getId()));
        // 进行装配
        Map<String, Object> map = BeanUtil.beanToMap(p);
        map.put("username", users.getName());
        map.put("avatar", users.getAvatar());
        map.put("likes", likes);
        map.put("comments", comments);
        map.put("favorites", favorites);
        List<String> photos = photosMapper.selectList(
                new LambdaQueryWrapper<Photos>()
                        .select(Photos::getPhotoUrl)
                        .eq(Photos::getPostId, p.getId())
        ).stream().map(Photos::getPhotoUrl).toList();
        map.put("images", photos);
        // 判断该users 是否被关注
        if(!users.getId().equals(StpUtil.getLoginIdAsLong())) {
            map.put("isFollowed", followsMapper.selectOne(new LambdaQueryWrapper<Follows>()
                    .eq(Follows::getFollowerId, StpUtil.getLoginIdAsLong())
                    .eq(Follows::getFollowingId, users.getId())) != null);
        }
        return map;
    }

    /**
     * 向目标用户推送帖子
     */
    @GetMapping("/list")
    public GlobalResponse<Map<Object, Object>> list(GlobalResponse<Posts> page) {
        // 获取推送的博主和关注的博主
        List<Long> followedUserIds = followsMapper.selectList(
                        new QueryWrapper<Follows>().eq("follower_id", StpUtil.getLoginIdAsLong()))
                .stream()
                .map(Follows::getFollowingId)
                .toList();
        // 通过该id 获取该博主所有的帖子
        List<Map<String, Object>> posts = new ArrayList<>();
        for (Long userId : followedUserIds) {
            LambdaQueryWrapper<Posts> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Posts::getUserId, userId);
            List<Posts> list = postsMapper.selectList(wrapper);
            // 获取该用户的数据
            Users users = usersMapper.selectById(userId);
            List<Map<String, Object>> mlist = list.stream()
                    .map(p -> this.assemblePost(p, users))
                    .toList();
            posts.addAll(mlist);
        }
        // 获取推送的帖子，目前是把所有帖子的前十条都发送到前端去
        GlobalResponse<Posts> response = postsService.lambdaQuery().page(page);
        List<Posts> records = response.getRecords();
        List<Map<String, Object>> collect = records.stream().map(p -> {
            Users users = usersMapper.selectById(p.getUserId());
            return this.assemblePost(p, users);
        }).toList();
        return GlobalResponse.success(
                MapUtil.builder()
                        .put("posts", collect)
                        .put("followedPosts", posts)
                        .map()
        );
    }


    /**
     * 查看帖子详情
     *
     * @param id 帖子ID，从路径参数中获取
     * @return 返回帖子详情，包括帖子内容、评论、图片、点赞、关注、收藏数量
     * @apiNote 此接口用于获取指定帖子的详情，任何用户都可以访问。返回的数据包含帖子内容及其相关的所有信息。
     */
    @GetMapping("/{id}")
    public GlobalResponse<Map<String, Object>> getPost(@PathVariable Long id) {
        // 获取帖子信息
        Posts post = postsMapper.selectById(id);
        if (post == null) {
            return GlobalResponse.failure("帖子不存在");
        }
        Long userId = post.getUserId();
        Users users = usersMapper.selectById(userId);
        return GlobalResponse.success(this.assemblePost(post, users));
    }

    /**
     * 删除帖子
     *
     * @param id 帖子ID，从路径参数中获取
     * @return 返回删除操作的结果
     * @throws GlobalResponse.failure 当帖子不存在或当前登录用户无权删除帖子时抛出
     * @apiNote 此接口用于删除指定帖子，要求用户必须登录且只能删除自己发布的帖子。
     */
    @DeleteMapping("/{id}")
    public GlobalResponse<String> deletePost(@PathVariable Long id) {
        Posts post = postsMapper.selectById(id);
        if (post == null || !post.getUserId().equals(StpUtil.getLoginIdAsLong())) {
            return GlobalResponse.failure("无权删除帖子或帖子不存在");
        }
        postsMapper.deleteById(id);
        return GlobalResponse.success("删除成功");
    }

    /**
     * 添加评论
     *
     * @param postId  帖子ID，从路径参数中获取
     * @param comment 评论对象，包含评论内容
     * @return 返回成功添加的评论对象
     * @throws GlobalResponse.failure 当帖子不存在时抛出
     * @apiNote 此接口用于在指定帖子下添加评论，要求用户必须登录。添加时会自动设置评论的发布者ID和创建时间。
     */
    @PostMapping("/comments/{postId}")
    @Transactional
    public GlobalResponse<Comments> addComment(@PathVariable Long postId, @Valid @RequestBody Comments comment) {
        Posts post = postsMapper.selectById(postId);
        if (post == null) {
            return GlobalResponse.failure("帖子不存在");
        }
        comment.setPostId(postId);
        comment.setUserId(StpUtil.getLoginIdAsLong());
        comment.setCreatedTime(LocalDateTime.now());
        commentsMapper.insert(comment);
        return GlobalResponse.success(comment);
    }

    // 评论信息的装配函数
    private Map<String, Object> assembleComments(Comments comments) {
        Map<String, Object> map = BeanUtil.beanToMap(comments);
        // 添加评论用户信息
        Users users = usersMapper.selectById(comments.getUserId());
        map.put("userName", users.getName());
        map.put("avatar", users.getAvatar());
        // 添加该评论的点赞信息
        Long likes = likesMapper.selectCount(new LambdaQueryWrapper<Likes>().eq(Likes::getCommentId, comments.getId()));
        // 被那些用户点赞了
        map.put("likes", likes);
        // 追加子评论信息
        map.put("isChildren", comments.getBeCommentsId() != null);
        if (comments.getBeCommentsId() != null) {
            // 装配被评论UserName信息
            Comments c = commentsMapper.selectById(comments.getBeCommentsId());
            Users u = usersMapper.selectById(c.getUserId());
            map.put("userName", users.getName() + "@" + u.getName());
        }
        return map;
    }


    /**
     * @param postId     获取评论的ID
     *                   获取帖子的评论列表(所有的评论列表)
     * @author 林河
     */
    @GetMapping("/commentsList")
    public GlobalResponse<Map<String, Object>> getComments(
            GlobalResponse<Comments> page,
            @RequestParam Long postId) {

        // 获取帖子下的所有评论
        GlobalResponse<Comments> commentsRes = commentsMapper.selectPage(page, new LambdaQueryWrapper<Comments>()
                .eq(Comments::getPostId, postId));

        var mapList = new ArrayList<Map<String, Object>>();
        for (Comments comment : commentsRes.getRecords()) {
            // 对评论进行装配
            Map<String, Object> commentMap = assembleComments(comment);
            mapList.add(commentMap);
        }
        GlobalResponse<Map<String, Object>> response = GlobalResponse.success("获取成功!");
        BeanUtil.copyProperties(commentsRes, response);
        response.setRecords(mapList);
        return response;
    }


    /**
     * @param followingId 关注对象ID
     *                    关注用户
     * @author 林河
     */
    @PostMapping("/follow")
    public GlobalResponse<String> followUser(@RequestParam Long followingId) {
        Long followerId = StpUtil.getLoginIdAsLong();

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
     * @param followingId 关注对象ID
     *                    关注用户
     * @author 林河
     */
    @PostMapping("/unfollow")
    public GlobalResponse<String> unfollowUser(@RequestParam Long followingId) {
        Long followerId = StpUtil.getLoginIdAsLong();

        followsMapper.delete(new LambdaQueryWrapper<Follows>().eq(Follows::getFollowerId, followerId)
                .eq(Follows::getFollowingId, followingId));

        return GlobalResponse.success("取消关注成功");
    }

    /**
     * @param postId    帖子ID（如果是对帖子的点赞）
     * @param commentId 评论ID（如果是对评论的点赞）
     * @return 点赞操作的结果
     * @author 林河
     * 用户对帖子或评论点赞
     */
    @PostMapping("/like")
    public GlobalResponse<String> like(@RequestParam(required = false) Long postId,
                                       @RequestParam(required = false) Long commentId) {
        Long userId = StpUtil.getLoginIdAsLong();
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
     * @param postId    帖子ID（如果是对帖子的取消点赞）
     * @param commentId 评论ID（如果是对评论的取消点赞）
     * @return 取消点赞操作的结果
     * @author 林河
     * 用户取消对帖子或评论的点赞
     */
    @DeleteMapping("/unlike")
    public GlobalResponse<String> unlike(@RequestParam(required = false) Long postId,
                                         @RequestParam(required = false) Long commentId) {
        Long userId = StpUtil.getLoginIdAsLong();

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
     * @param postId 帖子ID
     * @return 收藏操作的结果
     * @author 林河
     * 用户收藏帖子
     */
    @PostMapping("/favorite")
    public GlobalResponse<String> favorite(@RequestParam Long postId) {
        Long userId = StpUtil.getLoginIdAsLong();

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
     * @param postId 帖子ID
     * @return 取消收藏操作的结果
     * @author 林河
     * 用户取消收藏帖子
     */
    @DeleteMapping("/unfavorite")
    public GlobalResponse<String> unfavorite(@RequestParam Long postId) {
        Long userId = StpUtil.getLoginIdAsLong();

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
     * 获取目标用户对帖子的状态
     */
    @GetMapping("/actions")
    public GlobalResponse<Map<Object, Object>> actions(@RequestParam Long postId) {
        if (postId == null || postId == -1) return GlobalResponse.failure("postId不能为空");
        long userId = StpUtil.getLoginIdAsLong();
        var lWp = new LambdaQueryWrapper<Likes>();
        lWp.eq(Likes::getUserId, userId);
        lWp.eq(Likes::getPostId, postId);
        Likes likes = likesMapper.selectOne(lWp);
        var fWp = new LambdaQueryWrapper<Favorites>();
        fWp.eq(Favorites::getUserId, userId);
        fWp.eq(Favorites::getPostId, postId);
        Favorites favorites = favoritesMapper.selectOne(fWp);
        LambdaQueryWrapper<Comments> cWp = new LambdaQueryWrapper<>();
        cWp.eq(Comments::getUserId, userId)
                .eq(Comments::getBeCommentsId, postId);
        Comments comments = commentsMapper.selectOne(cWp);

        // 获取帖子目前的数据
        GlobalResponse<Map<String, Object>> post = this.getPost(postId);
        // 判断用户是否like，收藏
        return GlobalResponse.success(
                MapUtil.builder().put("likes", likes != null)
                        .put("favorites", favorites != null)
                        .put("comments", comments != null)
                        .put("post", post.getData())
                        .map());
    }


    /**
     * 上传图片（添加到帖子的相册中）
     *
     * @param postId 帖子ID，从路径参数中获取
     * @param photo  图片信息，包含图片URL等信息
     * @return 返回上传的图片信息或失败原因
     * @throws GlobalResponse.failure 当帖子不存在或用户无权上传图片时抛出
     * @apiNote 此接口用于上传图片到指定帖子的相册中，只有帖子所有者可以上传图片。
     */
    @PostMapping("/{postId}/photos")
    public GlobalResponse<Photos> uploadPhoto(@PathVariable Long postId, @RequestBody Photos photo) {
        // 获取帖子信息
        Posts post = postsMapper.selectById(postId);

        // 验证帖子是否存在且用户是否有权限上传图片
        if (post == null || !post.getUserId().equals(StpUtil.getLoginIdAsLong())) {
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
     * @author 林河
     */
    @GetMapping("/followed-users")
    public GlobalResponse<List<Map<String, Object>>> getFollowedUsers() {
        Long userId = StpUtil.getLoginIdAsLong();

        // 查询所有当前用户关注的用户ID
        List<Long> followedUserIds = followsMapper.selectList(
                        new QueryWrapper<Follows>().eq("follower_id", userId))
                .stream()
                .map(Follows::getFollowingId)
                .collect(Collectors.toList());
        if (followedUserIds.isEmpty()) {
            return GlobalResponse.success(List.of());
        }

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

