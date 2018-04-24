// global -> 画法 -> 启动器


function chooseSwitchOnBaseOnType() {
    if(drawingType==="Total"){
        total_actualSwitchOn();
    }else if(drawingType==="Rectangle"){
        rect_actualSwitchOn();
    }else if(drawingType==="Polygon"){
        poly_actualSwitchOn();
    }else{
        alert("回传的绘画方式不正确");
    }
}

rect_actualSwitchOn();
setGlobalParamAndUpdateJson();
chooseSwitchOnBaseOnType();

console.log(window.myVueStore.state.user.loginState);
