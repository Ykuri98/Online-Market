<template>
  <div class="EditGoods">
    <header class="clear">
      <span>管理管理员</span>
    </header>
    <div class="content">

      <div class="inputBox">
        <span>账号：</span>
        <TextInput class="long" placeholder="请输入邮箱账号" v-model="email" :initText="email"/>
      </div>


      <div class="inputBox">
        <span>昵称：</span>
        <TextInput class="long" placeholder="请输入昵称" v-model="nickname" :initText="nickname"/>
      </div>
      <div class="inputBox">
        <span>密码：</span>
        <TextInput class="long" placeholder="请输入密码" v-model="pwd" :initText="pwd"/>
      </div>

      <div class="btnBox" style="text-align: left">
        <button class="confirmBtn" @click="saveChange">保存修改</button>
        <button class="normalBtn" @click="back">返回</button>
      </div>

    </div>

  </div>
</template>

<script>
import TextInput from '../../components/TextInput';
import {getAdminsInfo,updateAdminss,addAdminss} from '../../api/admin';
export default {
  name: 'EditEditAdmins',
  components:{
    TextInput
  },
  data(){
    return{
      id:this.$route.params.id, // 两种参数 addAdmins  or  id
      email:'',
      nickname:'',
      pwd:''
    }
  },
  methods:{
    back(){
      this.$router.go(-1);
    },
    saveChange(){


        // 必须qq邮箱 qq号5-12位
        let testEmail = /^\d{5,12}@[qQ][qQ]\.(com|cn)$/
        if (!testEmail.test(this.email)){
            alert("账户正则验证未通过!")
            return;
        }

        //只能包括数字字母的组合，长度为4-15位
        let testNickName = /^[A-Za-z0-9]{4,15}$/;
        if (!testNickName.test(this.nickname)){
            alert("昵称正则验证未通过!")
            return;
        }
        //最少8位,最多16位，至少1个大写字母，1个小写字母，1个数字 和 1个特殊字符
        let testPwdName = /^.*(?=.{8,16})(?=.*\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*? ]).*$/;
        if (!testPwdName.test(this.pwd)){
            alert("密码正则验证未通过!")
            return;
        }


      if(this.id==='addAdmins'){// 表示添加
        const res = addAdminss({
          nickname:this.nickname,
          email:this.email,
          pwd:this.pwd
        });
        res
        .then(()=>{
          alert('创建管理员成功！')
        })
        .catch((e)=>{
          alert(e);
        })
      }else{// 表示修改
        const res = updateAdminss({
          id:this.id,
          nickname:this.nickname,
          email:this.email,
          pwd:this.pwd
        });
        res
        .then(()=>{
          alert('修改成功！')
        })
        .catch((e)=>{
          alert(e);
        })
      }
    }
  },
  mounted(){
    //新建账号
    if(this.id==='addAdmins'){

    }else{
      const res = getAdminsInfo(this.id);
      res
      .then((data)=>{

        this.email = data.email;
        this.nickname = data.nickname;
        this.id = data.id;
        this.pwd = data.pwd;
      })
      .catch((e)=>{
        alert(e);
      })
    }
  }
}
</script>

<style scoped lang="less">
@import "../../assets/css/var.less";
.EditGoods{
  header{
    width: 100%;
    height: 40px;
    line-height: 40px;
    span{
      float: left;
    }
  }
  .content{
      width: 100%;
      background-color: white;
      padding: 10px;
      textarea{
        width: 80%;
        height: 300px;
        border: 1px solid @borderColor;
      }
      .inputBox{
        margin-bottom: 30px;
        span{
          width: 90px;
          display: inline-block;
          color:@fontDefaultColor;
          font-weight: 600;
          vertical-align: middle;
        }
        .verTop{
          vertical-align: top;
        }
        .Radio /deep/ input:checked+.tipsBox{
          &:after{
            background-color:#337da4;
          }
        }
        .tips{
          font-weight: normal;
          width: auto;
          font-size: 13px;
          position: relative;
          left: 3px;
        }
        .val{
          width: auto;
          font-weight: 500;
          color:@fontDeepColor;
        }
        ul{
          display: inline-block;
          width: 500px;
          li{
            width: 100%;
            height: 40px;
            span{
              width: auto;
              font-size: 13px;
              font-weight: 500;
            }
            input{
              margin-right: 10px;
            }
            button{
              width: 50px;
              height: 25px;
              color:#d7514a;
              border:none;
              background-color: white;
              border-radius: 5px;
            }
          }

        }
        .long{
          width: 900px;
        }
      }
      .btnBox{
        display: block;
        width: 250px;
      }
      .confirmBtn{
        display: inline-block;
        margin-right: 30px;
        background-color: #337da4;
        color:white;
        border: none;
        width: 100px;
        height: 30px;
        border-radius: 5px;
        cursor: pointer;
      }
      .normalBtn{
        display: inline-block;
        background-color: grey;
        color:white;
        border: none;
        width: 100px;
        height: 30px;
        border-radius: 5px;
        cursor: pointer;
      }
  }
}
</style>
