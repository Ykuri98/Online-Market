<template>
  <div class="ClientLogin" :style="{width:width+'px',height:height+'px'}">
    <div class="content">
      <h3>Online Mart</h3>
      <div class="tag">
        <span @click="setIndex(0)" :class="{selected:curIndex===0}">登录</span>
        <span @click="setIndex(1)" :class="{selected:curIndex===1}">注册</span>
      </div>
      <div class="formBox" v-show="curIndex===0">
        <input ref="email" type="text" placeholder="邮箱账号" />
        <input ref="pwd" type="password" placeholder="密码" />
        <button @click="login">登录</button>
      </div>
      <div class="formBox" v-show="curIndex===1">
        <input ref="signEmail" type="text" placeholder="请输入注册的邮箱" />
        <input ref="signName" type="text" placeholder="请输入昵称" />
        <input ref="signPwd" type="password" placeholder="请输入密码" />
        <input ref="signRecipient" type="text" placeholder="请输入收件人姓名" />
        <input ref="signAddress" type="text" placeholder="请输入收件地址" />
        <input ref="signPhone" type="text" placeholder="请输入联系电话" />
        <button @click="signup">注册</button>
      </div>
    </div>
  </div>
</template>

<script>
import { mapMutations } from 'vuex'
import {getClientSize} from '../../util/util';
import {login,signup} from '../../api/client';

export default {
  name: 'ClientLogin',
  computed:{
    width(){
      return getClientSize().width;
    },
    height(){
      return getClientSize().height;
    },
  },
  data () {
    return {
      curIndex:0
    }
  },
  created(){
      if (this.$route.params.parm == 'login'){
          this.curIndex = 0
      }else if (this.$route.params.parm == 'register') {
          this.curIndex = 1
      }
  },
  methods:{
    ...mapMutations({
      setClientName: 'SET_CLIENT_NAME',
      setClientToken: 'SET_CLIENT_TOKEN'
    }),
    setIndex(index){
      if(index===this.curIndex){
        return;
      }
      this.curIndex = index;
    },
    login(){
      const email = this.$refs.email.value;
      const pwd = this.$refs.pwd.value;
      const res = login({
          email:email,
        pwd:pwd
      });
      res
      .then((data)=>{
        this.setClientName(data.name);
        this.setClientToken(data.token);
        this.$router.push('/');
      })
      .catch((e)=>{
        alert(e)
      })
    },
    signup(){

        // 必须qq邮箱 qq号5-12位
        let testEmail = /^\d{5,12}@[qQ][qQ]\.(com|cn)$/
        if (!testEmail.test(this.$refs.signEmail.value)){
            alert("邮箱正则验证未通过!")
            return;
        }
        //只能包括数字字母的组合，长度为4-15位
        let testNickName = /^[A-Za-z0-9]{4,15}$/;
        if (!testNickName.test(this.$refs.signName.value)){
            alert("昵称正则验证未通过!")
            return;
        }
        //最少8位,最多16位，至少1个大写字母，1个小写字母，1个数字和1个特殊字符
        let testPwdName = /^.*(?=.{8,16})(?=.*\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*? ]).*$/;
        if (!testPwdName.test(this.$refs.signPwd.value)){
            alert("密码正则验证未通过!")
            return;
        }
        //中文正则验证
        let cnChina = /[\u4E00-\u9FA5]/;
        if (!cnChina.test(this.$refs.signAddress.value)){
            alert("地址正则验证未通过!")
            return;
        }
        if (!cnChina.test(this.$refs.signRecipient.value)){
            alert("收件人正则验证未通过!")
            return;
        }
        // 1开始,第二位(3,4,5,8,7) 必须九位
        let testPhone = /^1[3|4|5|8|7][0-9]\d{4,8}$/
        if (!testPhone.test(this.$refs.signPhone.value)){
            alert("号码正则验证未通过!")
            return;
        }

      const res = signup({
        email:this.$refs.signEmail.value,
        nickname:this.$refs.signName.value,
        pwd:this.$refs.signPwd.value,
        recipient:this.$refs.signRecipient.value,
        address:this.$refs.signAddress.value,
        phone:this.$refs.signPhone.value,
      });
      res
      .then((data)=>{
        this.setClientName(data.name);
        this.setClientToken(data.token);
        this.$router.push('/');
      })
      .catch((e)=>{
        alert(e)
      })
    }
  }
}
</script>

<style scoped lang="less">
@import "../../assets/css/var.less";
.ClientLogin{
  background-color: @bgColor;
  position: relative;
  .content{
    width: 300px;
    position: absolute;
    top: 50%;
    left: 50%;
    margin-top: -260px;
    margin-left: -150px;
    text-align: center;
    overflow: hidden;
    h3{
      color:@secondColor;
      font-size: 50px;
    }
    .tag{
      margin-top: 20px;
      color:@fontDefaultColor;
      margin-bottom: 20px;
      span{
        display: inline-block;
        width: 50px;
        text-align: center;
        margin: 0 10px;
        padding: 10px 0;
        cursor: pointer;
      }
      .selected{
        border-bottom: 2px solid @secondColor;
        color:@secondColor
      }
    }
    input{
      border-radius: 0;
      box-shadow: none;
      background: #fff;
      padding: 14px;
      width: 80%;
      border: 1px solid @borderColor;
      margin-bottom: 10px;
    }
    button{
      width: 90%;
      background: @secondColor;
      box-shadow: none;
      border: 0;
      border-radius: 3px;
      line-height: 41px;
      color: #fff;
      cursor: pointer;
      margin-top: 20px;
    }
  }
}
</style>
