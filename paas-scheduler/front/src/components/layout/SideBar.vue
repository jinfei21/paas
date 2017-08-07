<template>
    <div class="sidebar">
        <el-menu :default-active="onRoutes" class="el-menu-vertical-demo" theme="dark" unique-opened router>
            <el-menu-item index="app">
                <i class="el-icon-setting"></i>APP管理
            </el-menu-item>
            <el-menu-item  index="2">
                <i class="el-icon-menu"></i>Deploy管理
            </el-menu-item >
            <el-menu-item  index="3">
                <i class="el-icon-date"></i>Task管理
            </el-menu-item>
            <el-submenu index="4">
                <template slot="title"><i class="el-icon-star-on"></i>集群管理</template>
                <el-menu-item index="slave">Slave管理</el-menu-item>
                <el-menu-item index="rack">Rack管理</el-menu-item>
            </el-submenu>
        </el-menu>
    </div>
</template>
<script>
    export default {
        computed:{
            onRoutes(){
                //return this.$route.path.replace('/home','');
            }
        }
    }
</script>
<style scoped>
    .sidebar{
        display: block;
        position: absolute;
        width: 250px;
        left: 0;
        top: 70px;
        bottom:0;
        background: #2E363F;
    }
    .sidebar > ul {
        height:100%;
    }
</style>
