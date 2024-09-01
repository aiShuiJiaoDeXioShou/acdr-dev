import { defineUniPages } from '@uni-helper/vite-plugin-uni-pages'

export default defineUniPages({
  globalStyle: {
    navigationStyle: 'default',
    navigationBarTitleText: '宠屋',
    navigationBarBackgroundColor: '#f8f8f8',
    navigationBarTextStyle: 'black',
    backgroundColor: '#FFFFFF',
  },
  easycom: {
    autoscan: true,
    custom: {
      '^wd-(.*)': 'wot-design-uni/components/wd-$1/wd-$1.vue',
      '^(?!z-paging-refresh|z-paging-load-more)z-paging(.*)':
        'z-paging/components/z-paging$1/z-paging$1.vue',
    },
  },
  tabBar: {
    color: '#999999',
    selectedColor: '#000000',
    backgroundColor: '#F8F8F8',
    borderStyle: 'black',
    height: '50px',
    fontSize: '10px',
    iconWidth: '24px',
    spacing: '3px',
    list: [
      {
        iconPath: 'static/tabbar/home.png',
        selectedIconPath: 'static/tabbar/homeL.png',
        pagePath: 'pages/index/index',
        text: '首页',
      },
      {
        iconPath: 'static/tabbar/space.png',
        selectedIconPath: 'static/tabbar/spaceL.png',
        pagePath: 'pages/space/index',
        text: '互动空间',
      },
      {
        iconPath: 'static/tabbar/24gl-paperPlane.png',
        selectedIconPath: 'static/tabbar/24gl-paperPlaneL.png',
        pagePath: 'pages/push/index',
        text: '发布',
      },
      {
        iconPath: 'static/tabbar/message.png',
        selectedIconPath: 'static/tabbar/messageL.png',
        pagePath: 'pages/message/index',
        text: '消息',
      },
      {
        iconPath: 'static/tabbar/my.png',
        selectedIconPath: 'static/tabbar/myL.png',
        pagePath: 'pages/my/index',
        text: '我的 ',
      },
    ],
  },
})
