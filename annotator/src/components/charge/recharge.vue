<template>
  <el-col :span="24">
    <div id="test"></div>
    <div class="grid-bg">
      <!--最上面提示-->
      <el-col :span="2">
        <img src="../../images/recharge.png" style="width: 50px; height: 50px; margin-left: 30px"/>
      </el-col>
      <el-col :span="2">
        <div style="margin-top: 12px; color: #6b6b6b; font-weight: bolder; font-size: 19px"> 充值中心</div>
      </el-col>
      <el-col :span="20">
        <div class="prompt-label">1.充值获得的积分可以在ImgAnnotator系统中用于发布任务。</div>
        <div class="prompt-label">2.积分换算方法是 1元=1积分，你可以选择支付宝或者微信的付款方式进行充值，10元起充</div>
      </el-col>
    </div>

    <div class="grid-bg1">
      <!--充值账户提示-->
      <el-col :span="2">
        <img src="../../images/singleUser.png" style="width: 50px; height: 50px; margin-left: 30px"/>
      </el-col>
      <el-col :span="2">
        <div style="margin-top: 15px; color: #6b6b6b; font-size: 15px"> 充值账号: &nbsp;</div>
      </el-col>
      <el-col :span="14">
        <div style="margin-top: 15px; color: #6b6b6b; font-size: 15px" id="username">{{$store.state.user.userInfo.username}}</div>
      </el-col>
      <el-col :span="2">
        <img src="../../images/points.png" style="width: 50px; height: 50px; margin-left: 30px"/>
      </el-col>
      <el-col :span="2">
        <div style="margin-top: 15px; color: #6b6b6b; font-size: 15px"> 当前积分: &nbsp;</div>
      </el-col>
      <el-col :span="2">
        <div style="margin-top: 15px; color: #6b6b6b; font-size: 15px" id="points">{{$store.state.user.userInfo.points}}</div>
      </el-col>
    </div>
    <!--充值金额选项-->
    <el-row :gutter="15" style="margin-top: 40px">
      <el-col :span="2" class="prompt-label" style="margin-left: 70px; margin-top: 20px">
        <div>充值金额:</div>
      </el-col>
      <el-col :span="5">
        <el-col :span="1" style="margin-top: 17px">
          <el-radio v-model="amount" label="10.00" id="bt1"> &nbsp;</el-radio>
        </el-col>
        <el-col :span="16" style="margin-left: 10px">
          <el-card style="" shadow="hover">
            &nbsp&nbsp￥&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp 10元
          </el-card>
        </el-col>
      </el-col>
      <el-col :span="5" style="margin-left: 0">
        <el-col :span="1" style="margin-top: 17px">
          <el-radio v-model="amount" label="20.00"> &nbsp;</el-radio>
        </el-col>
        <el-col :span="16" style="margin-left: 10px">
          <el-card style="" shadow="hover">
            &nbsp&nbsp￥&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp 20元
          </el-card>
        </el-col>
      </el-col>
      <el-col :span="5" style="margin-left: 0">
        <el-col :span="1" style="margin-top: 17px">
          <el-radio v-model="amount" label="50.00"> &nbsp;</el-radio>
        </el-col>
        <el-col :span="16" style="margin-left: 10px">
          <el-card style="" shadow="hover">
            &nbsp&nbsp￥&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp 50元
          </el-card>
        </el-col>
      </el-col>
      <el-col :span="5" style="margin-left: 0px">
        <el-col :span="1" style="margin-top: 17px">
          <el-radio v-model="amount" label="100.00" style="font-size: 25px">&nbsp;</el-radio>
        </el-col>
        <el-col :span="16" style="margin-left: 10px">
          <el-card style="" shadow="hover">
            &nbsp&nbsp￥&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp 100元
          </el-card>
        </el-col>
      </el-col>
    </el-row>
    <!--充值方式选项-->
    <el-row :gutter="15" style="margin-top: 40px">
      <el-col :span="2" class="prompt-label" style="margin-left: 70px; margin-top: 12px">
        <div>付款方式:</div>
      </el-col>
      <el-col :span="6">
        <el-col :span="1" style="margin-top: 10px">
          <el-radio v-model="mode" :label="2"> &nbsp</el-radio>
        </el-col>
        <el-col :span="16" style="margin-left: 10px">
          <img src="../../images/WeChat.png" style="height: 50px; width: 190px; margin-top: -6px">
        </el-col>
      </el-col>
      <el-col :span="6">
        <el-col :span="1" style="margin-top: 10px">
          <el-radio v-model="mode" :label="1"> &nbsp</el-radio>
        </el-col>
        <el-col :span="16" style="margin-left: 10px">
          <img src="../../images/Alipay.png" style="height: 55px; width: 160px; margin-top: -8px">
        </el-col>
      </el-col>
    </el-row>
    <!--金额提示-->
    <el-row style="margin-left: 70px; margin-top: 20px">
      <el-col :span="2" class="prompt-label">
        <div>应付金额:</div>
      </el-col>
      <el-col :span="8" style="margin: 8px 10px 10px 20px; font-size: 30px">
        <div style="color: #f2463f; font-weight: bolder" id="payMoney">{{amount+' 元'}}</div>
      </el-col>
    </el-row>
    <form style="display: none" id='formpay' name='formpay' method='post' action='https://pay.bbbapi.com/'>
      <input name='uid' id='uid' type='text' value=''/>
      <input name='price' id='price' type='text' value=''/>
      <input name='istype' id='istype' type='text' value=''/>
      <input name='notify_url' id='notify_url' type='text' value=''/>
      <input name='return_url' id='return_url' type='text' value=''/>
      <input name='orderid' id='orderid' type='text' value=''/>
      <input name='orderuid' id='orderuid' type='text' value=''/>
      <input name='goodsname' id='goodsname' type='text' value=''/>
      <input name='key' id='key' type='text' value=''/>
      <input type='submit' id='submitdemo1'>
    </form>

    <el-col :span="4">
      <el-button value="确认支付" id="demoBtn1" @click="onSubmit"
                 style="margin-left: 73px; margin-top: 10px; color: #ffffff; background-color: #56b5dc;">确认支付
      </el-button>
    </el-col>
    <el-col :span="18" style="margin-top: 18px; color: #5e5e5e">
      <div>温馨提示：充值所获积分仅限在本网站上使用，如有疑问，欢迎致电：13770758178</div>
    </el-col>
  </el-col>
</template>

<script>
  import {mapActions} from 'vuex'

  export default {
    //根据选择的支付金额，这边的单选框组名叫amount，随时修改payMoney的值
    //根据选择的支付方式，这边的单选框组名叫mode
    //显示当前用户名 username， 当前用户积分 points
    data() {
      return {
        amount: '10.00',
        mode: 1,
      };
    },

    props:{
      showMessage:{
        type: String,
        default: 'not-show'
      }
    },

    mounted(){
      if(this.showMessage==='show'){
        // this.updateWithoutPointer();

        this.$http.post('user/changePoints',{
          username: this.$store.state.user.userInfo.username,
          points: this.getNum(this.$store.state.charger.currentNum)
        }).then(()=>{console.log('调用成功');this.updateWithoutPointer()}).catch(err=>{console.log(err)});

        this.$alert('充值成功', '提示', {
          confirmButtonText: '确定',
          callback: () => {
            this.$message({
              type: 'success',
              message: '您已经充值成功',
              duration: 2000
            });
          }
        });
      }
    },

    methods: {
      // 得到支付方式
      // handleClick() {
      //   this.$message.error('尚未开通此功能');
      // },
      ...mapActions(['updateWithoutPointer','writeNum']),

      getNum(str){
        switch(str){
          case '10.00': return 10;
          case '20.00': return 20;
          case '50.00': return 50;
          case '100.00': return 100;
          default: return 0;
        }
      },

      onSubmit() {
        // 微信的istype是1，支付宝的是2
        // 价格传参一定要保留2位小数
        // orderuid 用户名
        this.writeNum(this.amount);
        this.$http.post('/pays/pay', {
          price: this.amount,
          istype: this.mode,
          orderuid: this.$store.state.user.userInfo.username
        })
          .then(function (data) {
            $('#uid').val(data.data.data.uid);
            $('#price').val(data.data.data.price);
            $("#istype").val(data.data.data.istype);
            $('#notify_url').val(data.data.data.notify_url);
            $('#return_url').val(data.data.data.return_url);
            $('#orderid').val(data.data.data.orderid);
            $('#orderuid').val(data.data.data.orderuid);
            $("#goodsname").val(data.data.data.goodsname);
            $('#key').val(data.data.data.key);

            $('#submitdemo1').click();
          })

          .catch(function (err) {
            console.log("外层" + err);
          })
      }
    }
  }


  // this.$http({
  //   headers: {
  //     "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"
  //   },
  //   method: "POST",
  //   url: "https://pay.bbbapi.com/",
  //   data: {
  //     uid: "d6e476045dd7c4f6d6b4f85c",
  //     price: 20.00,
  //     istype: 1,
  //     notify_url: "http://localhost:8080/pays/notifyPay",
  //     return_url: "http://localhost:8080/3-1",
  //     orderid: "sad",
  //     orderuid: 'sddd',
  //     goodsname: 'suiy',
  //     key: 'suiy1http://localhost:8081/3-1/notifyPaysadsddd20.00http://localhost:8081/3-19fc9a411cac1508a6ab0687b2105b060d6e476045dd7c4f6d6b4f85c'
  //   },
  //   //function: window.location.href = 'https://pay.bbbapi.com/'
  // })


  //window.location.href = 'https://pay.bbbapi.com/'
  //console.log("hello")
  //   that.$http.post('https://pay.bbbapi.com/',
  //     {
  //     uid: "d6e476045dd7c4f6d6b4f85c",
  //     price: 20,
  //     istype: 1,
  //     notify_url: "http://localhost:8080/pays/notifyPay",
  //     return_url: "http://localhost:8080/3-1",
  //     orderid: "sad",
  //     orderuid: 'sddd',
  //     goodsname: 'suiy',
  //     key: 'suiy1http://localhost:8081/3-1/notifyPaysadsddd20http://localhost:8081/3-19fc9a411cac1508a6ab0687b2105b060d6e476045dd7c4f6d6b4f85c'
  //   })
</script>

<style scoped>
  .grid-bg {
    margin: 30px 30px 0 30px;
    border-radius: 4px;
    min-height: 36px;
    background-color: aliceblue;
    padding: 10px 0;
    height: 160px;
  }

  .grid-bg1 {
    margin: 30px 50px 0 50px;
    border-radius: 4px;
    min-height: 36px;
    background-color: aliceblue;
    padding: 10px 0;
    height: 50px;
  }

  .prompt-label {
    margin: 20px 10px 10px 0px;
    font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
    font-size: 18px;
    color: #343434;
  }

  /*表单不可见*/
  .submit-input {
    /*visibility: hidden;*/
  }
</style>
