<template>
  <div class="MallShow">
    <FixedNav v-show="navShouldFixed">
      <div slot="navContent" class="container fixedNavContainer">
        <h3 class="fixedLeft" @click="navTo('/mall/show/index')">Online Mart</h3>
        <ul class="fixedRight">
          <li
            v-for="(item,index) in typeList"
            :key="'type'+item.id"
            :class="{selected:judgeCurPath(item.id)}"
            @click="selectType(item.id)"
          >
            {{item.name}}
          </li>
        </ul>
      </div>
    </FixedNav>
    <ul ref="typeList" class="typeList">
      <li
        v-for="(item,index) in typeList"
        :key="'type'+item.id"
        :class="{selected:judgeCurPath(item.id)}"
        @click="selectType(item.id)"
      >
        {{item.name}}
      </li>
    </ul>
    <router-view></router-view>
  </div>
</template>

<script>
import {getTypes,getGoodsList} from '../../api/client';
import TipsInput from '../../components/TipsInput';
import FixedNav from '../../components/FixedNav';

export default {
  name: 'MallShow',
  components:{
    TipsInput,
    FixedNav,
  },
  computed:{
    curPath(){
      return this.$route.path;
    }
  },
  data () {
    return {
      typeList:[],
      searchText:'',
      tips:[''],
      navShouldFixed:false,
      datamid: ''
    }
  },

  methods:{
    navTo(route){
      this.$router.push(route);
    },
    judgeCurPath(typeId){
      if(typeId===-1){
        if(this.curPath.indexOf('/show/index')>-1){
          return true;
        }else{
          return false;
        }
      }else{
        if(this.curPath.indexOf(`/show/goodsList/${typeId}`)>-1){
          return true;
        }else{
          return false;
        }
      }
    },
    selectType(typeId){

        var tag = 0
        for (const obj of this.datamid) {
            if (this.datamid.length < 7){
                break
            }
            tag = tag + 1
            if (obj.id == typeId){
                if (tag < 4){
                    // 不做任何操作
                    this.typeList = this.datamid.slice(0, 7);
                    break;
                }else {
                    if ((this.datamid.length - tag) <3){
                        // 倒序
                        this.typeList = this.datamid.slice(this.datamid.length - 6 , this.datamid.length );
                        if (this.typeList[0].id == -1){
                        }else {
                            this.typeList.unshift({
                                id:-1,
                                name:'首页'
                            });
                        }
                        break
                    }else {
                        // 移动
                        this.typeList = this.datamid.slice(tag-3, tag+3);
                        if (this.typeList[0].id == -1){
                        }else {
                            this.typeList.unshift({
                                id:-1,
                                name:'首页'
                            });
                        }
                        break;
                    }
                }
            }
        }

      //首页
      if(typeId===-1){
        this.navTo('/mall/show/index');
      }else{
        this.navTo('/mall/show/goodsList/'+typeId+'/all');
      }
    },
    searchTip(tip){
      // alert(tip)
    },
    searchTextChange(text){
    },
    searchConfirm(){
      if(this.searchText.trim().length<=0){
        alert('输入不能为空！');
        return;
      }
      this.navTo(`/mall/show/goodsList/0/${this.searchText}`);
    },
    scrollHandle(){
      const top = this.$refs.typeList.getBoundingClientRect().top;
      //还未到顶
      if(top>0){
        this.navShouldFixed=false;
      }
      //已经到顶
      else{
        this.navShouldFixed=true;
      }
    }
  },

  mounted(){
    //获取数据
    const res = getTypes();
    res
    .then((data)=>{
      data.unshift({
        id:-1,
        name:'首页'
      });
      this.datamid  = data;
      if ( data.length>7 ){
          this.typeList = data.slice(0, 7);
      }else {
          this.typeList = data;
      }
    })
    .catch((e)=>{
      alert(e);
    });

    //监听滚动事件
    document.addEventListener('scroll',this.scrollHandle,false);
  },

  destroyed(){
    document.removeEventListener('scroll',this.scrollHandle,false);
  },
  watch:{
    searchText(newVal,oldVal){
      this.searchTextChange(newVal);
    }
  }
}
</script>

<style scoped lang="less">
@import "../../assets/css/var.less";
.MallShow{
  width: 100%;
  .typeList{
    width: 100%;
    margin-top: 20px;
    text-align: center;
    background-color: white;
    li{
      display: inline-block;
      width: 60px;
      margin: 0 50px;
      text-align: center;
      height: 30px;
      line-height: 20px;
      font-weight: 600;
      font-size: 14px;
      cursor: pointer;
      overflow:hidden;
      text-overflow:ellipsis;
      white-space:nowrap
    }
    .selected{
      color:@thirdColor;
      border-bottom: 3px solid @thirdColor;
    }
  }
    .fixedNavContainer{
    text-align: left;
    .fixedLeft{
      display: inline-block;
      vertical-align: middle;
      width: 15%;
      height: 100%;
      font-size:18px;
      color:@thirdColor;
      user-select: none;
      line-height: 64px;
      text-align: center;
      cursor: pointer;
    }
    .fixedRight{
      width: 70%;
      height: 100%;
      display: inline-block;
      vertical-align: middle;
      li{
        display: inline-block;
        width: 60px;
        margin: 0 30px;
        text-align: center;
        height: 30px;
        line-height: 20px;
        font-weight: 600;
        font-size: 14px;
        cursor: pointer;
        position: relative;
        top: 4px;
        overflow:hidden;
        text-overflow:ellipsis;
        white-space:nowrap
      }
      .selected{
        color:@thirdColor;
        border-bottom: 3px solid @thirdColor;
      }
    }
  }
}
</style>
