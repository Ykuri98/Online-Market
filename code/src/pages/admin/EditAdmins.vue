<template>
  <div class="EditUser">
  	<header class="clear">
  		<span>管理员账号</span>
      <div>
        <input ref="inputName" type="text" placeholder="输入搜索管理员昵称" />
        <button @click="searchAdmins"><i class="iconfont icon-search" /></button>
      </div>

      <div style="margin-right: 20px">
        <input ref="inputAccount" type="text" placeholder="输入搜索管理员账户" />
        <button></button>
      </div>
  	</header>
	<table className="userTable">
        <thead>
        	<tr><th>用户ID</th><th>账户</th><th>昵称</th><th>密码</th><th>操作</th><th>操作</th></tr>
        </thead>
        <tbody>
            <tr v-for="(item,index) in adminsList" :key="'user'+item.id">
                <td>{{item.id}}</td>
                <td>{{item.email}}</td>
                <td>{{item.nickname}}</td>
                <td>{{item.pwd}}</td>

              <td><button class="delete" @click="deleteAdmins(item.id)">删除</button></td>
              <td><button class="delete" @click="updateAdmins(item.id)">修改</button></td>
            </tr>
            <tr style="text-align: center" >
              <td colspan="6">
                <i @click="addAdmins" class="el-icon-plus"></i>
              </td>

            </tr>

        </tbody>
    </table>


  </div>
</template>

<script>
import {getAllAdmins,getSearchAdmins,deleteAdmins} from '../../api/admin';
export default {
  name: 'EditAdmins',
  computed:{
  },
  data(){
  	return{
        adminsList:[]
  	}
  },
  mounted(){
  	const res = getAllAdmins();
  	res
  	.then((admins)=>{
  		this.adminsList = admins;
  	})
  	.catch((e)=>{
  		alert(e)
  	})
  },
  methods:{
    navTo(route){
        this.$router.push(route);
    },
    addAdmins(){
        this.navTo('/backstage/editAdmins/addAdmins')

    },
    updateAdmins(id){
        this.navTo('/backstage/editAdmins/'+id)
    },
    deleteAdmins(id){
  		const res = deleteAdmins(id);
  		res
  		.then(()=>{
  			alert('删除成功');
  			this.adminsList.map((item,index)=>{
  				if(item.id===id){
  					this.adminsList.splice(index,1);
  				}
  			})
  		})
  		.catch((e)=>{
  			alert(e);
  		})
  	},
    searchAdmins(){
  		const nickname = this.$refs.inputName.value;
      const email = this.$refs.inputAccount.value;

      const res = getSearchAdmins({
          nickname:nickname,
          email:email
      });
      res
  		.then((data)=>{
  			this.adminsList = data;
  		})
  		.catch((e)=>{
  			alert(e);
  		})
  	},
  }
}
</script>

<style scoped lang="less">
@import "../../assets/css/var.less";
.EditUser{
	header{
		width: 100%;
		height: 40px;
		line-height: 40px;
		span{
			float: left;
		}
		div{
			height: 20px;
			float: right;
      input{
        border: none;
        border-bottom: 1px solid #337da4;
        background-color: rgba(0,0,0,0);
        width: 180px;
        padding-left: 10px;
      }
			button{
        position: relative;
        top: -1px;
        border: none;
        background-color: rgba(0,0,0,0);
				i{
					font-size: 17px;
          color:#337da4;
				}
			}
		}
	}
}
</style>
