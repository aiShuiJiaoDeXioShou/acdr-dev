package cn.iocoder.yudao.module.master.community.service.impl;

import cn.iocoder.yudao.module.master.community.entity.Posts;
import cn.iocoder.yudao.module.master.community.mapper.PostsMapper;
import cn.iocoder.yudao.module.master.community.service.IPostsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 林河
 * @since 2024-08-12
 */
@Service
public class PostsServiceImpl extends ServiceImpl<PostsMapper, Posts> implements IPostsService {

}
