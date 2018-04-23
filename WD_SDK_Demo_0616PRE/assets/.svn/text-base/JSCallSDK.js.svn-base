/**
 * Created by wd_WYC on 16/8/24.
 */
var payParameter = Object();
payParameter.access = null;
payParameter.appid = null;
payParameter.appkey = null;
payParameter.subnum = null;
payParameter.amount = null;
payParameter.subject = null;
payParameter.body = null;
payParameter.description = null;
payParameter.orderNo = null;
payParameter.channel = null;
payParameter.objectState = true;

function getMessageFromForm(){
    payParameter.access = null;
    payParameter.appid = null;
    payParameter.appkey = null;
    payParameter.subnum = null;
    payParameter.amount = null;
    payParameter.subject = null;
    payParameter.body = null;
    payParameter.description = null;
    payParameter.orderNo = null;
    payParameter.channel = null;

    var formElement = document.getElementById("messageForm");
    if(formElement != null){
        payParameter.appid = document.getElementById("appid").value;
        payParameter.appkey = document.getElementById("appkey").value;
        payParameter.subnum = document.getElementById("subnum").value;
        payParameter.amount = document.getElementById("amount").value;
        payParameter.subject = document.getElementById("subject").value;
        payParameter.body = document.getElementById("body").value;
        payParameter.description = document.getElementById("description").value;
        payParameter.orderNo = document.getElementById("orderNo").value;
        var selectIndex = document.getElementById("channel").selectedIndex;
        var formObject = document.getElementById("channel");
        payParameter.channel = formObject.options[selectIndex].value;

        var selectAccessIndex = document.getElementById("access").selectedIndex;
        var formAccess = document.getElementById("access");
        payParameter.access = formAccess.options[selectAccessIndex].value;

        if(selectAccessIndex == 0){
            payParameter.access = "CC";
        }else if(selectAccessIndex ==1){
            payParameter.access = "CT";
        }else{
            payParameter.access = "CST";
        }

    }else{
        alert("未找到form");
    }

}

function randomString(len) {
    len = len || 32;
    var $chars = 'abcdefhijkmnprstwxyz2345678';    /****默认去掉了容易混淆的字符oOLl,9gq,Vv,Uu,I1****/
    var maxPos = $chars.length;
    var pwd = '';
    for (i = 0; i < len; i++) {
        pwd += $chars.charAt(Math.floor(Math.random() * maxPos));
    }
    pwd = "974"+pwd+"and";
    document.getElementById("orderNo").value = pwd;
}

function javaScriptCall_Android_1() {
    getMessageFromForm();
    var info = "{appid:" + payParameter.appid + ",appkey:" + payParameter.appkey + ",subnum:" + payParameter.subnum + ",subject:" + payParameter.subject
        + ",orderNo:" + payParameter.orderNo + ",channel:" + payParameter.channel + ",amount:" + payParameter.amount+",access:"+payParameter.access
        + ",body:" + payParameter.body + ",description:" + payParameter.description + "}";

    blin.CallAndroid(info);

    //document.getElementById("resultMessage").innerHTML = info;
}




function android_callJavaScript(paramtterString){
    document.getElementById("resultCode").innerHTML=paramtterString;
}