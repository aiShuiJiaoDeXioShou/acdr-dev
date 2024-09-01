<template>
  <view class="user-post relative">
    <!-- 这里是关注功能 -->
    <view v-if="post.isFollowed != undefined" class="absolute top-2 right-2 space-x-2">
      <!-- 实心按钮 - 关注+ -->
      <button
        @click="follow"
        v-if="!post.isFollowed"
        class="bg-amber-500 text-white rounded w-16 h-8 text-size-14px"
      >
        关注+
      </button>
      <!-- 空心按钮 - 取消关注 -->
      <button
        @click="unfollow"
        v-else
        class="border border-amber-500 text-amber-500 rounded"
      >
        取消关注
      </button>
    </view>

    <view class="user-info">
      <image :src="imgUrl(post.avatar)" alt="avatar" class="avatar"></image>
      <view class="user-details">
        <view class="user-name">{{ post.username }}</view>
        <view class="location">{{ post.location }}</view>
      </view>
    </view>
    <view class="time">{{ post.time }}</view>
    <view class="images-grid">
      <wd-img
        :radius="8"
        mode="center"
        :enable-preview="true"
        v-for="(image, index) in post.images"
        :key="index"
        :src="imgUrl(image)"
        :alt="image"
        class="post-image"
      />
    </view>
    <view class="post-content">
      <view>{{ post.content }}</view>
    </view>
    <view class="actions">
      <view class="action-item">
        <image
          v-if="!action.likes"
          @click="like"
          class="icon"
          :src="imgUrl('@/static/space/thumbs.svg')"
          mode="scaleToFill"
        />
        <image
          v-else
          @click="unlike"
          class="icon"
          :src="imgUrl('@/static/space/favorites.png')"
          mode="scaleToFill"
        />
        <text>{{ post.likes || "0" }}</text>
      </view>
      <view class="action-item">
        <image
          v-if="!action.favorites"
          @click="favorite"
          class="icon"
          :src="imgUrl('@/static/space/like.svg')"
          mode="scaleToFill"
        />
        <image
          v-else
          @click="unfavorite"
          class="icon"
          :src="imgUrl('@/static/space/likes-on.png')"
          mode="scaleToFill"
        />
        <text>{{ post.favorites || "0" }}</text>
      </view>
      <view class="action-item">
        <image
          @click="popAction"
          class="icon"
          :src="imgUrl('@/static/space/comment.svg')"
          mode="scaleToFill"
        />
        <text>{{ post.comments || "0" }}</text>
      </view>
    </view>
    <!-- 这里是评论区 -->
    <view class="bg-gray-100" v-if="isPop">
      <view v-if="comments.total > 0">
        <view
          v-for="(comment, index) in comments.records"
          @click="beComment(comment)"
          :key="comment.id"
          class="w-full mx-auto bg-white rounded-lg p-1 flex items-start"
        >
          <image
            :src="imgUrl(comment.avatar)"
            class="w-12 h-12 rounded-full mr-4"
            mode="aspectFill"
          />
          <view class="flex-grow">
            <text class="font-bold text-lg txt w-40">{{ comment.userName }}</text>
            <view class="text-sm text-gray-800 mt-2">
              {{ comment.content }}
            </view>
            <view class="text-xs text-gray-500 mt-2 flex items-center">
              <text>{{ comment.createdTime }}</text>
              <text class="mx-2">•</text>
              <text>{{ "国内" }}</text>
            </view>
          </view>
        </view>
        <view
          v-if="comments.total > 10"
          class="text-blue-500"
          @click="commentsList(-1, -1)"
        >
          加载更多...
        </view>
      </view>
    </view>
    <view v-if="isPop" class="flex items-center relative">
      <textarea
        class="flex-grow p-2 border border-gray-300 rounded-lg"
        placeholder="评论..."
        rows="3"
        v-model="contentText"
      />
      <view class="absolute bottom-0 left-0 text-gray-500">
        {{
          beCommentId
            ? "@" + beCommentId.userName
            : beCommentId.userNamebeCommentId.userName
        }}
      </view>
      <button
        class="bg-blue-500 text-white rounded-lg absolute right-0 bottom-0"
        @click="sendComment"
      >
        发送
      </button>
    </view>
  </view>
</template>

<script setup>
import { imgUrl, toast } from "@/utils/commUtils";
import { http, httpGet, httpPost } from "@/utils/http";
import { defineProps } from "vue";

// 接收 props
const props = defineProps({
  post: Object,
});

const action = ref({});
const isPop = ref(false);
const comments = ref({});
const beCommentId = ref(null);
const contentText = ref("");

const beComment = (comment) => {
  beCommentId.value = comment;
};
const popAction = async () => {
  isPop.value = !isPop.value;
  // 查询评论数据
  if (isPop.value) {
    await commentsList();
  }
};

// 获取评论列表
const commentsList = async (current = 1, size = 10) => {
  try {
    const res = await httpGet(
      `/posts/commentsList?postId=${props.post.id}&current=${current}&size=${size}`
    );
    if (res.code == 200) {
      comments.value = res;
    } else {
      toast(res.message);
    }
  } catch (e) {
    toast(e.data?.message || "评论获取失败");
  }
};

// 发表评论
const sendComment = async () => {
  try {
    const res = await httpPost("/posts/comments/" + props.post.id, {
      postId: props.post.id,
      content: contentText.value,
      beCommentsId: beCommentId.value?.id || null,
    });
    if (res.code == 200) {
      toast("评论成功");
      // 刷新评论数据
      await commentsList();
      beCommentId.value = null;
      contentText.value = "";
    } else {
      toast(res.message);
    }
  } catch (e) {
    toast(e.data?.message || "评论失败");
  }
};

// 判断该帖子的状态，是否点赞，是否评论，是否收藏
const isAction = async () => {
  try {
    const res = await httpGet(`/posts/actions?postId=${props.post.id}`);
    if (res.code == 200) {
      action.value = res.data;
      props.post.favorites = res.data.post.favorites;
      props.post.comments = res.data.post.comments;
      props.post.likes = res.data.post.likes;
      props.post.isFollowed = res.data.post.isFollowed;
    } else {
      toast(res.message);
    }
  } catch (e) {
    console.log(e);
  }
};

// 点赞
const like = async () => {
  try {
    const res = await httpPost("/posts/like", {}, { postId: props.post.id });
    if (res.code == 200) {
      // toast("点赞成功");
      await isAction();
    } else {
      toast(res.message);
    }
  } catch (e) {
    console.log(e);
    toast(e.data?.message || "点赞失败");
  }
};

// 取消点赞
const unlike = async () => {
  try {
    const res = await http({
      url: "/posts/unlike?postId=" + props.post.id,
      method: "DELETE",
    });
    if (res.code == 200) {
      // toast("取消点赞成功");
      await isAction();
    } else {
      toast(res.message);
    }
  } catch (e) {
    toast(e.data?.message || "取消点赞失败");
  }
};

// 收藏
const favorite = async () => {
  try {
    const res = await httpPost("/posts/favorite", {}, { postId: props.post.id });
    if (res.code == 200) {
      // toast("收藏成功");
      await isAction();
    } else {
      toast(res.message);
    }
  } catch (e) {
    toast(e.data?.message || "收藏失败");
  }
};

// 取消收藏
const unfavorite = async () => {
  try {
    const res = await http({
      url: "/unfavorite?postId=" + props.post.id,
      method: "DELETE",
    });
    if (res.code == 200) {
      // toast("取消收藏成功");
      await isAction();
    } else {
      toast(res.message);
    }
  } catch (e) {
    toast(e.data?.message || "取消收藏失败");
  }
};

// 编写关注和取消关注
const follow = async () => {
  try {
    const res = await httpPost("/posts/follow", {}, { followingId: props.post.userId });
    if (res.code == 200) {
      // toast("关注成功");
      await isAction();
    } else {
      toast(res.message);
    }
  } catch (e) {
    toast(e.data?.message || "关注失败");
  }
};

//编写取消关注
const unfollow = async () => {
  try {
    const res = await http({
      url: "/posts/unfollow?followingId=" + props.post.userId,
      method: "POST",
    });
    if (res.code == 200) {
      // toast("取消关注成功");
      await isAction();
    } else {
      toast(res.message);
    }
  } catch (e) {}
};

onLoad(async () => {
  await isAction();
});
</script>

<style lang="scss" scoped>
.user-post {
  padding: 16px;
  background-color: #fff;
  border: 1px solid #eaeaea;
  border-radius: 8px;

  .icon {
    width: 35rpx;
    height: 35rpx;
    object-fit: contain;
  }

  .user-info {
    display: flex;
    align-items: center;
  }

  .avatar {
    width: 50px;
    height: 50px;
    margin-right: 12px;
    border-radius: 50%;
  }

  .user-details {
    flex: 1;
  }

  .user-name {
    margin: 0;
    font-size: 16px;
    font-weight: bold;
  }

  .location {
    margin: 0;
    font-size: 12px;
    color: #888;
  }

  .time {
    font-size: 12px;
    color: #888;
    text-align: right;
  }

  .images-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 8px;
    margin: 16px 0;
  }

  .post-image {
    width: 100%;
    height: 200rpx;
    object-fit: cover;
  }

  .post-content {
    font-size: 14px;
    color: #333;
  }

  .actions {
    display: flex;
    justify-content: space-around;
    margin-top: 16px;
  }

  .action-item {
    display: flex;
    gap: 5px;
    align-items: center;
    color: #888;
  }

  .action-item .iconfont {
    margin-right: 4px;
    font-size: 20px;
  }
}
</style>
