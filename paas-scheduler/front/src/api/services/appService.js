import request from '../request'


export default {
	
	query (params) {
	  return request({
	    url: '/app/list',
	    method: 'get',
	    data: params,
	  })
	},
	
	create(params) {
		return request({
		    url: '/app/add',
		    method: 'post',
		    data: params,
		})
	},
	
	update(params) {
		return request({
		    url: '/app/update',
		    method: 'post',
		    data: params,
		})
	},	
	
}

