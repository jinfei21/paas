import Vue from 'vue';
import App from './App';
import router from './router';
import ElementUI from 'element-ui';
// import 'element-ui/lib/theme-default/index.css';    // 默认主题
import '../static/css/theme-green/index.css';       // 浅绿色主题
import store from './store'
import axios from 'axios'

Vue.prototype.$http = axios;

Vue.use(ElementUI);
new Vue({
    store,
    router,
    render: h => h(App)
}).$mount('#app');
