(function(){
    let addFolder=document.querySelector('#add-folder');
    let folderOperation=document.querySelector('#folder-operation');
    let folderSettingClose=document.querySelector('#folder-setting-close');
    let userSettingHidden=null;
    let selectMumbers=document.getElementById('select-mumbers');
    let folderContactAddBtn=document.getElementById('folder-contact-add-btn');
    let selectList=document.getElementsByClassName('select-list')[0];
    let selectSettingClose=document.querySelector('.select-setting-close');

    //展开分类页面
    function expandFolder(obj){
        userSettingHidden=obj.parentNode.parentNode;
        userSettingHidden.style.width="0";
        userSettingHidden.style.opacity="0";
        folderOperation.style.width="384px";
        folderOperation.style.opacity="1";
    }
    //展开选择成员页面
    function expandSelect(obj){
        let parent=obj.parentNode.parentNode.parentNode.parentNode;
        parent.style.width="0";
        parent.style.opacity="0";
        selectMumbers.style.width="384px";
        selectMumbers.style.opacity="1";
    }
    //关闭选择成员页面
    function shrinkSelect(obj){
        let parent=obj.parentNode.parentNode;
        parent.style.width="0";
        parent.style.opacity="0";
        folderOperation.style.width="384px";
        folderOperation.style.opacity="1";
    }

    //添加成员点击事件
    folderContactAddBtn.onclick=function(){
        expandSelect(this);
        //设置列表高度
        //获得页面高度
        let bodyHeight=(window.innerHeight) ? window.innerHeight : (document.documentElement && document.documentElement.clientHeight) ? document.documentElement.clientHeight : document.body.offsetHeight;
        selectList.style.height=bodyHeight-80+"px";
    }

    //关闭成员选择页面
    selectSettingClose.onclick=function(e){
        // console.log(this);
        addRippleEffect(e,this,"rgba(30, 144, 255, 1)");
        shrinkSelect(this);
    }


    //添加联系人分类
    addFolder.onclick=function(){
        expandFolder(this);
        Vue.set(vue.messagebox, 'option', 0);
        $('#newFolderName').val("");
    }
    //关闭联系人文件夹设置
    folderSettingClose.onclick=function(e){
        addRippleEffect(e,this,"rgba(120,120,120,.25)");
        let folderPage=this.parentNode.parentNode;
        $('.user-setting-hidden:eq(1)').css({'width':'384px','opacity':'1'});
        folderPage.style.width="0";
        folderPage.style.opacity="0";
    }

})();