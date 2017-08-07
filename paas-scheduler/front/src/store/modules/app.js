import * as types from '../mutation-types'
import { parse } from 'qs'
import axios from 'axios'
import {api} from '../../api'


//initial state
const state = {
	fieldList :  [{
        value: 'appId',
        label: '应用ID'
      }, {
        value: 'appName',
        label: '应用名称'
      }],
	currentField : "appId",
	appList:[],
	currentPage:1,
	total:10,
	pageSize:10
}

// getters
const getters = {
	getFieldList : state => state.fieldList,
	getCurrentField : state => state.currentField,
	getAppList : state => state.appList,
	getCurrentPage : state => state.currentPage,
	getTotal : state => state.total,
	getPageSize : state => state.pageSize
}

// actions
const actions = {
	queryApp({commit},payload){
	   api.appService.query(parse(payload)).then(function (res) {
		   commit(types.Load_App,res);
       }.bind(this)).catch(function (err) {
           console.log(err);
       }.bind(this));
	},
	createApp({commit,dispatch},payload){
		api.appService.create(parse(payload)).then(function (res) { 
			   dispatch('queryApp',null)
	    }.bind(this)).catch(function (err) {
	           console.log(err);
	    }.bind(this));
	},
	
	updateApp({commit,dispatch},payload){
		api.appService.update(parse(payload)).then(function (res) { 
			   dispatch('queryApp',null)
	    }.bind(this)).catch(function (err) {
	           console.log(err);
	    }.bind(this));
	},
}

// mutations
const mutations = {
	[types.Load_App](state,data){
		console.log(data);
		state.appList = data.data;
		state.pageSize = data.page.pageSize;
		state.total = data.page.total;
		state.currentPage =  data.page.current;
	}
}


export default {
    state,
    getters,
    actions,
    mutations
}