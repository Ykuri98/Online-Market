import 'babel-polyfill';
import Vue from 'vue';
import App from './App';
import router from './router/admin';
import store from './store';

import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';

import './assets/css/common.less';
import './assets/font/iconfont.css';

import axiosadmin from './config/axios-admin'

Vue.prototype.$baseURL = axiosadmin.defaults.baseURL

Vue.use(ElementUI);


Vue.config.productionTip = false;

new Vue({
  el: '#admin',
  router,
  store,
  template: '<App/>',
  components: { App }
});
