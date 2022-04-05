function connectWebSocket() {
    //这里需要的路径需要配置相对应的路径
    // const target = "ws://192.168.137.1:8080/chat";
    const target = "ws://localhost:8080/chat";
    if ('WebSocket' in window) {
        websocket = new WebSocket(target);
    } else {
        alert('Not support websocket')
    }
    //连接发生错误的回调方法
    websocket.onerror = function () {
        setMessageInnerHTML("error");
        let time=[];
        let timeId=setInterval(()=>{
            connectWebSocket();
            time.push(timeId);
            if(websocket.readyState===1){
                for(let item of time){
                    clearInterval(item);
                }
            }
        },500);
    }
    //连接成功建立的回调方法
    websocket.onopen = function (event) {
        setMessageInnerHTML("Loc MSG: 建立连接");
    }
    //接收到消息的回调方法
    websocket.onmessage = function (event) {
        let message=event.data;
        vue.putMessage(message);
        setTimeout(()=>{
            scrollToBottom();
        },200);
    }
    //连接关闭的回调方法
    websocket.onclose = function () {
        setMessageInnerHTML("Loc MSG:关闭连接");
    }
    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function () {
        websocket.close();
    }
    return websocket.readyState;
}

connectWebSocket();
//将消息显示在网页上
function setMessageInnerHTML(innerHTML) {
    // document.getElementById('message').innerHTML += innerHTML + '<br/>';
    console.log(innerHTML);
}

//关闭连接
function closeWebSocket() {
    websocket.close();
}
