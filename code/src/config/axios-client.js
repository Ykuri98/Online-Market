import axios from 'axios'
import store from '../store'
import * as types from '../store/mutation-types'
import router from '../router/client'

axios.defaults.withCredentials = true
// axios 配置
axios.defaults.timeout = 600000;
//axios.defaults.baseURL = 'http://115.29.141.32:8084';
//axios.defaults.baseURL = 'http://localhost:8084';
//axios.defaults.baseURL = 'http://192.168.8.52:8084';

 //axios.defaults.baseURL = 'http://192.168.0.102:8084';


 //axios.defaults.baseURL = 'http://192.168.1.174';
axios.defaults.baseURL = 'http://192.168.13.40:8084'

// http request 拦截器
axios.interceptors.request.use(
	config => {
	    if (store.state.clientToken) {
	        config.headers.Authorization = `Bearer ${store.state.clientToken}`;
	    }
	    return config;
	},
	err => {
	    return Promise.reject(err);
	}
);

// http response 拦截器
axios.interceptors.response.use(
    response => {
        return response;
    },
    error => {
        if (error.response) {
            switch (error.response.status) {
                case 401:
                    // 401 清除token信息并跳转到登录页面
                    store.commit(types.CLIENT_LOGOUT);
                    router.replace({
                        path: 'login',
                        query: {redirect: router.currentRoute.fullPath}
                    })
            }
        }
        // console.log(JSON.stringify(error));//console : Error: Request failed with status code 402
        return Promise.reject(error.response.data)
    });

export default axios;
