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
handleExport = (url,filename) => {
    const req = new XMLHttpRequest();
    req.open('GET', url, true);
    req.responseType = 'blob';
    req.setRequestHeader('Content-Type', 'application/json');
    req.onload = () => {
      const data = req.response;
      const blob = new Blob([data],{type:"application/octet-stream"});
      const blobUrl = window.URL.createObjectURL(blob);
      this.download(blobUrl,filename) ;
    };
    req.send('');
  };
 
 download = (blobUrl,filename) => {
  const a = document.createElement('a');
  a.style.display = 'none';
  a.download = filename; //文件名
  a.href = blobUrl;
  a.click();
  // document.body.removeChild(a);
}
