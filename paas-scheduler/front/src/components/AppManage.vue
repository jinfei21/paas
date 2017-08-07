<template>
    <div>
    	<div>
		    <el-row :gutter="25">
			  <el-col :span="3">
				   <el-select v-model="currentField" placeholder="请选择">
				    <el-option
				      v-for="item in fieldList"
				      :key="item.value"
				      :label="item.label"
				      :value="item.value">
				    </el-option>
				  </el-select>
			  </el-col>
			  <el-col :span="6">
				  <el-input v-model="currentValue" placeholder="请输入内容"   @change="onSearch"></el-input>
			  </el-col>
			  <el-col :span="10" :offset="5">
			  		<el-button type="primary"  fixed="right" @click="addApp" >添加</el-button>
			  </el-col>
			</el-row>
        </div>
		<br />
        <div>
			<el-table
			    :data="appList"
			    style="width: 100%">
			    <el-table-column type="expand">
			      <template scope="props">
			        <el-form label-position="left" inline class="demo-table-expand">
			          <el-form-item label="应用名称">
			            <span>{{ props.row.appName }}</span>
			          </el-form-item>
			          <el-form-item label="应用类型">
			            <span>{{ props.row.type }}</span>
			          </el-form-item>
			          <el-form-item label="cpu">
			            <span>{{ props.row.cpu }}</span>
			          </el-form-item>
			          <el-form-item label="内存MB">
			            <span>{{ props.row.memory }}</span>
			          </el-form-item>
			          <el-form-item label="磁盘MB">
			            <span>{{ props.row.disk }}</span>
			          </el-form-item>
			          <el-form-item label="端口数">
			            <span>{{ props.row.ports }}</span>
			          </el-form-item>
			          <el-form-item label="应用描述">
			            <span>{{ props.row.desc }}</span>
			          </el-form-item>
			        </el-form>
			      </template>
			    </el-table-column>
			    <el-table-column
			      label="ID"
			      prop="id">
			    </el-table-column>
			    <el-table-column
			      label="应用ID"
			      prop="appId">
			    </el-table-column>
			    <el-table-column
			      label="应用名称"
			      prop="appName">
			    </el-table-column>
			    <el-table-column
			      label="部门"
			      prop="depart">
			    </el-table-column>
			    <el-table-column
			      label="负责人"
			      prop="owner">
			    </el-table-column>		
			    <el-table-column
			      label="创建时间"
			      prop="createTime">
			    </el-table-column>	
			    <el-table-column
			      label="修改时间"
			      prop="modifyTime">
			    </el-table-column>		
			    
			    <el-table-column
			      fixed="right"
			      label="操作"
			      width="100">
			      <template scope="scope">
			        <el-button @click="handleView" type="text" size="small">查看</el-button>
			        <br />
			        <el-button @click="handleEdit(scope)" type="text" size="small">编辑</el-button>
			      </template>
			    </el-table-column>
			  </el-table>
        </div>
        <div align='center'>
			  <el-pagination
			      @size-change="handleSizeChange"
			      @current-change="handleCurrentChange"
			      :current-page="currentPage"
			      :page-sizes="[10, 20, 30, 40]"
			      :page-size="pageSize"
			      layout=" sizes, prev, pager, next, jumper,total"
			      :total="total">
			  </el-pagination>
        </div>        
        <el-dialog title="创建APP" :visible.sync="dialogVisible">	
			<el-form ref="form" :model="app" label-width="80px">
				<el-form-item label="应用ID">
			    	<el-input v-model="app.appId"></el-input>
				</el-form-item>
				<el-form-item label="应用名称">
			    	<el-input v-model="app.appName"></el-input>
				</el-form-item>
				<el-form-item label="应用类型">
				    <el-select v-model="app.type" placeholder="请选择应用类型">
				      <el-option label="Service" value="service"></el-option>
				      <el-option label="Worker" value="worker"></el-option>
				      <el-option label="Job" value="job"></el-option>
				      <el-option label="OneOff" value="oneoff"></el-option>
				    </el-select>
				</el-form-item>					
				<el-form-item label="负责人">
			    	<el-input v-model="app.owner"></el-input>
				</el-form-item>			
				<el-form-item label="部门">
			    	<el-input v-model="app.depart"></el-input>
				</el-form-item>			
				<el-form-item label="CPU">
			    	<el-slider v-model="app.cpu" :step="0.1" :min="0" :max="8"></el-slider>
				</el-form-item>					    			    
			    <el-form-item label="内存">
			    	<el-slider v-model="app.memory" :step="1" :min="0" :max="1000"></el-slider>
				</el-form-item>
				<el-form-item label="端口">
			    	<el-slider v-model="app.ports" :step="1" :min="0" :max="10"></el-slider>
				</el-form-item>						
				<el-form-item label="磁盘">
			    	<el-slider v-model="app.disk" :step="1" :min="0" :max="1000"></el-slider>
				</el-form-item>					
				<el-form-item label="应用描述">
			    	<el-input type="textarea" v-model="app.desc"></el-input>
			    </el-form-item>
				<el-form-item>
				    <el-button type="primary" @click="onSubmit">提交</el-button>
				    <el-button @click="onClose">取消</el-button>
				 </el-form-item>		
			</el-form>
		</el-dialog>
    </div>
</template>

<script>
    import {mapGetters} from 'vuex';
    
    export default {
    
    	methods:{
    	     handleView() {
		       
		     },		      
		     handleEdit(data){
		      	console.log(data);
		      	this.app = data.row;
		      	this.dialogVisible= true;
		     },
		     handleSizeChange(){
		      	this.$store.dispatch('queryApp',{
		      		pageSize:this.pageSize,
		      		page:currentPage,
		      		field:currentField,
		      		keyword:currentValue
		      	});
		     },
		     handleCurrentChange(){
		      	this.$store.dispatch('queryApp',{
		      		pageSize:this.pageSize,
		      		page:currentPage,
		      		field:currentField,
		      		keyword:currentValue
		      	});
		     },
		     handleClose(done) {
		        this.$confirm('确认关闭？')
		          .then(_ => {
		            done();
		          })
		          .catch(_ => {});
		     },
		     addApp(){
		      	this.app={
			          appId:'',
			          appName:'',
			          type:'',
			          cpu:0,
			          memory:0,
			          ports:0,
			          disk:0,
			          owner:'',
			          depart:'',
			          desc:''			          	
			        };
		      	this.dialogVisible= true;
		     },
		     onSubmit(){
		      	this.dialogVisible= false;
		      	this.$store.dispatch('createApp',this.app);
		      	console.log(this.app)
		     },
		     onClose(){
		     	this.dialogVisible= false;
		     },
		     onSearch(){		      	
		      	console.log(this.currentValue);
		      	this.$store.dispatch('queryApp',{
		      		pageSize:this.pageSize,
		      		page:this.currentPage,
		      		field:this.currentField,
		      		keyword:this.currentValue
		      	});		      	
		    }
    	},
    	
        data: function(){
            return {
			        dialogVisible:false,
			        currentField:{
			          value: 'appId',
			          label: '应用ID'
			        },
			        currentValue:'',
			        app:{
			          appId:'',
			          appName:'',
			          type:'',
			          cpu:0,
			          memory:0,
			          ports:0,
			          disk:0,
			          owner:'',
			          depart:'',
			          desc:''			          	
			        }
            }
        },
        
        computed:{
        	...mapGetters({
        		appList:'getAppList',
        		fieldList:'getFieldList',
        		currentPage:'getCurrentPage',
        		total:'getTotal',
        		pageSize:'getPageSize'
        	})
        },
        created (){
        	this.$store.dispatch('queryApp');
        }
        
    }
</script>

<style>
  .demo-table-expand {
    font-size: 0;
  }
  .demo-table-expand label {
    width: 90px;
    color: #99a9bf;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }
</style>

