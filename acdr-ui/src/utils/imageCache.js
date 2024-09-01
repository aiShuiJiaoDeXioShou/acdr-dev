const isH5 = typeof window !== 'undefined';

// 打开或创建IndexedDB（仅H5环境）
function openDB() {
  return new Promise((resolve, reject) => {
    const request = indexedDB.open("imageCacheDB", 1);
    request.onupgradeneeded = function(event) {
      const db = event.target.result;
      if (!db.objectStoreNames.contains("images")) {
        db.createObjectStore("images", { keyPath: "url" });
      }
    };
    request.onsuccess = function(event) {
      resolve(event.target.result);
    };
    request.onerror = function(event) {
      reject("Database error: " + event.target.errorCode);
    };
  });
}

// 获取文件名
function getFileName(url) {
  return url.substring(url.lastIndexOf('/') + 1);
}

// 将图片转换为Base64编码
function getBase64Image(img) {
  var canvas = document.createElement("canvas");
  canvas.width = img.width;
  canvas.height = img.height;
  var ctx = canvas.getContext("2d");
  ctx.drawImage(img, 0, 0, img.width, img.height);
  var dataURL = canvas.toDataURL("image/png");  // 可选其他值 image/jpeg
  return dataURL;
}

// 转换URL到Base64
function toBase64(url) {
  return new Promise((resolve, reject) => {
    var image = new Image();
    image.crossOrigin = "Anonymous";  // 支持跨域图片
    image.onload = function() {
      var base64 = getBase64Image(image);
      resolve(base64);
    };
    image.onerror = function() {
      reject('Failed to convert image to base64.');
    };
    image.src = url + '?v=' + Math.random(); // 处理缓存
  });
}

// H5环境下获取缓存的图片
async function getCachedImageH5(url) {
  try {
    const db = await openDB();
    const tx = db.transaction("images", "readonly");
    const store = tx.objectStore("images");
    const request = store.get(url);

    return new Promise((resolve, reject) => {
      request.onsuccess = async function(event) {
        const result = event.target.result;
        if (result) {
          resolve(result.base64);
        } else {
          try {
            const base64 = await downloadAndSaveImageH5(url, db);
            resolve(base64);
          } catch (err) {
            reject(err);
          }
        }
      };
      request.onerror = function(event) {
        reject("Failed to retrieve from IndexedDB: " + event.target.errorCode);
      };
    });
  } catch (err) {
    throw new Error("Failed opening IndexedDB: " + err);
  }
}

// H5环境下下载并保存图片
async function downloadAndSaveImageH5(url, db) {
  const base64 = await toBase64(url);

  return new Promise((resolve, reject) => {
    const tx = db.transaction("images", "readwrite");
    const store = tx.objectStore("images");
    const request = store.put({ url, base64 });

    request.onsuccess = () => {
      resolve(base64);
    };
    request.onerror = (event) => {
      reject("Failed to save to IndexedDB: " + event.target.errorCode);
    };
  });
}

// 非H5环境下获取缓存的图片
async function getCachedImageNonH5(url) {
  const fs = uni.getFileSystemManager();
  const filePath = `${uni.env.USER_DATA_PATH}/${getFileName(url)}`;

  return new Promise((resolve, reject) => {
    fs.access({
      path: filePath,
      success: () => {
        fs.readFile({
          filePath: filePath,
          encoding: 'base64',
          success: (res) => {
            resolve(`data:image/png;base64,${res.data}`);
          },
          fail: reject
        });
      },
      fail: async () => {
        try {
          const base64 = await downloadAndSaveImageNonH5(url, filePath);
          resolve(base64);
        } catch (err) {
          reject(err);
        }
      }
    });
  });
}

// 非H5环境下下载并保存图片
async function downloadAndSaveImageNonH5(url, filePath) {
  return new Promise((resolve, reject) => {
    uni.downloadFile({
      url: url,
      success: (downloadResult) => {
        if (downloadResult.statusCode === 200) {
          const fs = uni.getFileSystemManager();
          fs.readFile({
            filePath: downloadResult.tempFilePath,
            encoding: 'base64',
            success: (res) => {
              const base64 = `data:image/png;base64,${res.data}`;
              fs.saveFile({
                tempFilePath: downloadResult.tempFilePath,
                filePath: filePath,
                success: () => {
                  resolve(base64);
                },
                fail: (err) => {
                  reject(`保存文件失败: ${err}`);
                }
              });
            },
            fail: (err) => {
              reject(`读取文件失败: ${err}`);
            }
          });
        } else {
          reject(`下载文件失败，状态码: ${downloadResult.statusCode}`);
        }
      },
      fail: (err) => {
        reject(`下载文件失败: ${err}`);
      }
    });
  });
}

// 根据环境选择适当的策略
async function getCachedImage(url) {
  if (isH5) {
    return getCachedImageH5(url);
  } else {
    return getCachedImageNonH5(url);
  }
}

export default {
  getCachedImage,
  getFileName,
  downloadAndSaveImageH5,
  downloadAndSaveImageNonH5,
  toBase64
};
