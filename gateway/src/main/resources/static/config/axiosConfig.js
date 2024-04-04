// 引入外部js文件
import { loadJs } from "./loadJs.js";

Promise.all([loadJs('axios', '/static/axios@1.6.8.js'),
    loadJs('Cookies', '/static/js.cookie@3.0.5.js')])
    .then(result => {
        let axios = result[0], Cookies = result[1];
        let axiosInstance = axios.create({
            baseURL: '/',
            timeout: 10000,
            headers: {
                'X-Requested-With': 'XMLHttpRequest'
            },
            // `xsrfCookieName` is the name of the cookie to use as a value for xsrf token
            xsrfCookieName: 'XSRF-TOKEN', // default
            // `xsrfHeaderName` is the name of the http header that carries the xsrf token value
            xsrfHeaderName: 'X-Xsrf-Token', // default
            // `undefined` (default) - set XSRF header only for the same origin requests
            withXSRFToken: (config) => {
                config.headers.set(config.xsrfHeaderName, Cookies.get(config.xsrfCookieName));
                return false; // 关闭 axios 默认配置
            }

        });

        // Add a request interceptor
        axiosInstance.interceptors.request.use(function (config) {
            // Do something before request is sent
            return config;
        }, function (error) {
            // Do something with request error
            return Promise.reject(error);
        });

        // Add a response interceptor
        axiosInstance.interceptors.response.use(function (response) {
            // Any status code that lie within the range of 2xx cause this function to trigger
            // Do something with response dat
            if(response.headers["redirect-url"]) {
                window.location.href = response.headers["redirect-url"]
            }

            let data = response.data
            if(data.code) {
                return data.result;
            }
            return response;
        }, function (error) {
            // Any status codes that falls outside the range of 2xx cause this function to trigger
            // Do something with response error
            return Promise.reject(error);
        });
        window.axios = axiosInstance;
        window.Cookies = Cookies;
    })
    .catch(errs => {
        // for (let err of errs) {
            console.error(errs)
        // }
    })