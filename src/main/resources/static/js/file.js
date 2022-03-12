(function (){
    document.getElementById('media').onchange=function (){
        let files=this.files;
        let filesName='';
        for(let file of files){
            filesName+=file.name+'<br>';
        }

        Vue.set(vue.messagebox, 'option', 3);
        Vue.set(vue.messagebox, 'title', "发送文件");
        Vue.set(vue.messagebox, 'msg', filesName);
        $('.create-chat-folder').css('display', 'block');
        $('.cancel').css('display', 'block');
    }
})();