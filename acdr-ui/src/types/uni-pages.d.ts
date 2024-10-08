/* eslint-disable */
/* prettier-ignore */
// @ts-nocheck
// Generated by vite-plugin-uni-pages

interface NavigateToOptions {
  url: "/pages/index/index" |
       "/pages/address/index" |
       "/pages/ai/ai-interactive-space" |
       "/pages/certification/index" |
       "/pages/certification/pet-sitter" |
       "/pages/extended/index" |
       "/pages/login/index" |
       "/pages/login/phone" |
       "/pages/mall/index" |
       "/pages/map/index" |
       "/pages/message/chat" |
       "/pages/message/index" |
       "/pages/message/message-list" |
       "/pages/my/index" |
       "/pages/order/index" |
       "/pages/order/order-detail" |
       "/pages/order/take" |
       "/pages/others/agreement" |
       "/pages/others/null" |
       "/pages/others/official-account" |
       "/pages/permission/bind-phone" |
       "/pages/permission/real-name-auth" |
       "/pages/pet/index" |
       "/pages/pet/pet-add-page" |
       "/pages/pet/pet-detail-page" |
       "/pages/push/door" |
       "/pages/push/goods" |
       "/pages/push/index" |
       "/pages/push/share" |
       "/pages/service/detail" |
       "/pages/service/door" |
       "/pages/service/index" |
       "/pages/service/my-service" |
       "/pages/service/search" |
       "/pages/service/shop" |
       "/pages/service/slippery" |
       "/pages/service/store" |
       "/pages/settings/index" |
       "/pages/space/index";
}
interface RedirectToOptions extends NavigateToOptions {}

interface SwitchTabOptions {
  url: "/pages/index/index" | "/pages/space/index" | "/pages/push/index" | "/pages/message/index" | "/pages/my/index"
}

type ReLaunchOptions = NavigateToOptions | SwitchTabOptions;

declare interface Uni {
  navigateTo(options: UniNamespace.NavigateToOptions & NavigateToOptions): void;
  redirectTo(options: UniNamespace.RedirectToOptions & RedirectToOptions): void;
  switchTab(options: UniNamespace.SwitchTabOptions & SwitchTabOptions): void;
  reLaunch(options: UniNamespace.ReLaunchOptions & ReLaunchOptions): void;
}
