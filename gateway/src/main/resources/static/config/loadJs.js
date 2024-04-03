// 通过append <script/> 来加载 js 文件
// (function(global, factory) {
//     typeof exports === 'object' && typeof module !== 'undefined' ? exports = factory(exports) :
//     typeof define === 'function' && define.amd ? define(['exports'], factory) :
//     (global = typeof globalThis !== 'undefined' ? globalThis : global || self, factory(global));
// })(this, function (exports) {
//
//     function loadJs(globalName, url) {
//
//          if (exports[globalName]) {
//              return Promise.resolve(exports[globalName]);
//          }
//
//          return new Promise((reslove, reject) => {
//              let scriptEle = document.createElement("script");
//              scriptEle.type = "text/javascript";
//              scriptEle.src = url;
//              scriptEle.async = true;
//              scriptEle.onload = () => {
//                  // let now1 = new Date();
//                  // console.log("定时器开启,脚本:" + globalName + "," + now1.getSeconds() + ":" + now1.getMilliseconds())
//                  let interval = setInterval(() => {
//                      // let now2 = new Date();
//                      // console.log("开始等待脚本执行完成:" + globalName + "," + now2.getSeconds() + ":" + now2.getMilliseconds())
//                      if (global[globalName]) {
//                          clearInterval(interval);
//                          // let now3 = new Date();
//                          // console.log("脚本执行完成:" + globalName + "," + now3.getSeconds() + ":" + now3.getMilliseconds())
//                          resolve(global[globalName])
//                      }
//                  }, 1) // 考虑到脚本执行时长问题,每隔1秒获取脚本执行配置的变量,有变量为执行完成
//              };
//              document.body.appendChild(scriptEle);
//          });
//     }
//
//     exports.loadJs = loadJs;
//
// });

export function loadJs(globalName, url) {

    let global = window;

    if (global[globalName]) {
        return Promise.resolve(global[globalName]);
    }

    return new Promise((resolve, reject) => {
        let scriptEle = document.createElement("script");
        scriptEle.type = "text/javascript";
        scriptEle.src = url;
        scriptEle.async = true;
        scriptEle.onload = () => {
            // let now1 = new Date();
            // console.log("定时器开启,脚本:" + globalName + "," + now1.getSeconds() + ":" + now1.getMilliseconds())
            let interval = setInterval(() => {
                // let now2 = new Date();
                // console.log("开始等待脚本执行完成:" + globalName + "," + now2.getSeconds() + ":" + now2.getMilliseconds())
                if (global[globalName]) {
                    clearInterval(interval);
                    // let now3 = new Date();
                    // console.log("脚本执行完成:" + globalName + "," + now3.getSeconds() + ":" + now3.getMilliseconds())
                    resolve(global[globalName])
                }
            }, 1) // 考虑到脚本执行时长问题,每隔1秒获取脚本执行配置的变量,有变量为执行完成
        };
        document.body.appendChild(scriptEle);
    });
}

export function toFormData(object) {
    const formData = new FormData()
    Object.keys(object).forEach(key => {
        const value = object[key]
        if (Array.isArray(value)) {
            value.forEach((subValue, i) =>
                formData.append(key + `[${i}]`, subValue)
            )
        } else {
            formData.append(key, object[key])
        }
    })
    return formData
}