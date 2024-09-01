import config from './config';  // 确保引入配置文件

// H5 平台导入
// #ifdef H5
import AMapLoader from '@amap/amap-jsapi-loader';

let AMap = null;
let map = null;

// 初始化高德地图 (H5)
const initAMap = async () => {
  if (!AMap) {
    AMap = await AMapLoader.load({
      key: config.AMapKey,
      version: '2.0',
      plugins: ['AMap.Geocoder', 'AMap.Geolocation', 'AMap.ToolBar'],
    });
  }
};

// 初始化地图容器 (H5)
export const initMapH5 = async (containerId, center = [116.397428, 39.90923], zoom = 15) => {
  await initAMap();  // 确保 AMap 已经加载

  if (!map) {
    map = new AMap.Map(containerId, {
      center,
      zoom,
    });
  }

  return map;
};

// 获取当前位置和详细地址 (H5)
export const getCurrentLocationH5 = async () => {
  await initAMap();  // 确保 AMap 已经加载

  return new Promise((resolve, reject) => {

    const geolocation = new AMap.Geolocation({
      enableHighAccuracy: true,
      timeout: 10000,
      zoomToAccuracy: true,
    });

    geolocation.getCurrentPosition((status, result) => {
      if (status === 'complete' && result) {
        const geocoder = new AMap.Geocoder();
        geocoder.getAddress(
          [result.position.lng, result.position.lat],
          (geoStatus, geoResult) => {
            if (geoStatus === 'complete' && geoResult) {
              const locationInfo = {
                latitude: result.position.lat,
                longitude: result.position.lng,
                address: geoResult.regeocode.formattedAddress,
              };
              resolve(locationInfo);
            } else {
              reject('逆地理编码失败');
            }
          },
        );
      } else {
        reject('定位失败，请检查网络或定位权限');
      }
    });
  });
};

// 显示当前位置在地图上 (H5)
export const showLocationOnMapH5 = (locationInfo) => {
  if (!map) return;

  const marker = new AMap.Marker({
    position: [locationInfo.longitude, locationInfo.latitude],
    map: map,
  });

  map.setCenter([locationInfo.longitude, locationInfo.latitude]);
  map.setFitView([marker]);  // 自动调整地图视野以适应所有标记
};

// 添加两点之间的连线和显示距离 (H5)
export const addLineAndDistanceH5 = (startLocation, endLocation) => {
  if (!map) return;

  const start = new AMap.LngLat(startLocation.longitude, startLocation.latitude);
  const end = new AMap.LngLat(endLocation.longitude, endLocation.latitude);

  const polyline = new AMap.Polyline({
    path: [start, end],
    isOutline: true,
    outlineColor: '#ffeeff',
    borderWeight: 2,
    strokeColor: '#3366FF',
    strokeOpacity: 1,
    strokeWeight: 3,
    strokeStyle: 'solid',
    strokeDasharray: [10, 5],
  });

  map.add(polyline);

  // 计算距离并在地图上显示
  const distance = Math.round(start.distance(end)); // 计算距离
  const distanceMarker = new AMap.Text({
    text: `${distance} 米`,
    anchor: 'bottom-center',
    position: new AMap.LngLat(
      (startLocation.longitude + endLocation.longitude) / 2,
      (startLocation.latitude + endLocation.latitude) / 2
    ),
  });

  map.add(distanceMarker);

  map.setFitView([polyline]); // 调整地图视野使其适应连线
};
// #endif

// 微信小程序平台导入
// #ifdef MP-WEIXIN
const amapFile = require('../static/js/amap-wx');

const amapPlugin = new amapFile.AMapWX({
  key: config.AMapKey,
});

// 获取当前位置和详细地址 (微信小程序)
export const getCurrentLocationWx = () => {
  return new Promise((resolve, reject) => {
    amapPlugin.getRegeo({
      success: (data) => {
        if (data && data.length > 0) {
          const locationInfo = {
            latitude: data[0].latitude,
            longitude: data[0].longitude,
            address: data[0].regeocodeData.formatted_address,
          };
          resolve(locationInfo);
        } else {
          reject('未能获取到详细地址信息');
        }
      },
      fail: (error) => {
        reject('定位失败: ' + error.message);
      }
    });
  });
};

// 显示当前位置在地图上 (微信小程序)
export const showLocationOnMapWx = (mapContext, locationInfo) => {
  if (mapContext && locationInfo && locationInfo.latitude && locationInfo.longitude) {
    mapContext.moveToLocation({
      latitude: locationInfo.latitude,
      longitude: locationInfo.longitude,
    });
  } else {
    console.error("Invalid mapContext or locationInfo");
  }
};


// 添加两点之间的连线和显示距离 (微信小程序)
export const addLineAndDistanceWx = (mapContext, startLocation, endLocation) => {
  mapContext.addPolyline({
    points: [
      {
        longitude: startLocation.longitude,
        latitude: startLocation.latitude,
      },
      {
        longitude: endLocation.longitude,
        latitude: endLocation.latitude,
      },
    ],
    color: '#3366FF',
    width: 4,
    dottedLine: false,
  });

  amapPlugin.getDistance({
    origin: `${startLocation.longitude},${startLocation.latitude}`,
    destination: `${endLocation.longitude},${endLocation.latitude}`,
    success: (data) => {
      if (data && data.distance) {
        // 显示距离（在页面中处理，微信地图不直接支持文本标记）
        console.log(`距离: ${data.distance} 米`);
      }
    },
  });
};

// 微信小程序的初始化地图函数
const initMapWeixin = (containerId, center, zoom) => {
  const mapContext = wx.createMapContext(containerId);

  // 设置中心点和缩放级别（微信小程序中缩放级别可以在地图组件中设置）
  mapContext.moveToLocation({
    latitude: center[1],
    longitude: center[0],
  });

  // 可以返回 mapContext 以便后续使用
  return mapContext;
};
// #endif

// 公共API

// 初始化地图 (根据平台调用)
export const initMap = async (containerId, center = [116.397428, 39.90923], zoom = 15) => {
  // #ifdef H5
  return await initMapH5(containerId, center, zoom);
  // #endif

  // #ifdef MP-WEIXIN
  return initMapWeixin(containerId, center, zoom);
  // #endif
};


// 获取当前位置和详细地址
export const getCurrentLocation = () => {
  // #ifdef H5
  return getCurrentLocationH5();
  // #endif

  // #ifdef MP-WEIXIN
  return getCurrentLocationWx();
  // #endif
};

// 显示当前位置在地图上
export const showLocationOnMap = (mapContextOrContainerId, locationInfo) => {
  // #ifdef H5
  showLocationOnMapH5(locationInfo);
  // #endif

  // #ifdef MP-WEIXIN
  showLocationOnMapWx(mapContextOrContainerId, locationInfo);
  // #endif
};

// 添加两点之间的连线和显示距离
export const addLineAndDistance = (mapContextOrContainerId, startLocation, endLocation) => {
  // #ifdef H5
  addLineAndDistanceH5(startLocation, endLocation);
  // #endif

  // #ifdef MP-WEIXIN
  addLineAndDistanceWx(mapContextOrContainerId, startLocation, endLocation);
  // #endif
};
