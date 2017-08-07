import Vue from 'vue';
import Router from 'vue-router';
import Login from '../components/Login.vue'
import Home from '../components/layout/Home.vue'
import AppManage from '../components/AppManage.vue'

Vue.use(Router);

export default new Router({
    routes: [
        {
            path: '/login',
            name: 'login',
            component: Login,
        },
        {
            path: '/home',
            name: 'home',
            component: Home,
            children: [
            	{
            		path:'/app',
            		name:'appmanage',
            		component:AppManage            		
            	}
            ]
        },
        {
            path: '/',
            redirect: '/login'
        },
    ],
    linkActiveClass: 'active'
})
