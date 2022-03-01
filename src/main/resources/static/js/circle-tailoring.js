function updateCoords(c) {
    $('#x').val(c.x);
    $('#y').val(c.y);
    $('#w').val(c.w);
    $('#h').val(c.h);
}
function clearCoords() {
    $('.up-load input').val('');
}
let jcropApi=null;
let originWidth=null;
let originHeight=null;
//头像上传
(function headImgUpLoad() {
    let imageInput = document.getElementById('image-input');
    let imageInputFrame = document.getElementById('image-input-frame');
    let upLoad = document.getElementsByClassName('up-load')[0];
    imageInput.onclick = function () {
        imageInputFrame.click();
    }
    $('#image-select').click(function () {
        imageInputFrame.click();
    });

    //预览图像
    imageInputFrame.onchange = function () {
        upLoad.style.display = "flex";
        let picture = this.files[0], img;
        img = new Image();
        img.onload = function () {
            let previewImg = document.getElementById('preview-img');
            previewImg.src = this.src;
            originWidth=this.width;
            originHeight=this.height;
            //图片裁剪初始化
            $('#preview-img').Jcrop({
                allowSelect: false,
                trackDocument: false,
                allowResize: false,
                aspectRatio: 1,
                setSelect: [0, 0, 200, 200],
                onChange: updateCoords,
                onSelect: updateCoords
            },function (){
                jcropApi=this;
            });
            $('#scale').val(500/this.width);
            $('.img-show>div').css({'width':'100%','height':'100%'});
        }
        img.src = URL.createObjectURL(picture);
    }
    let uploadClose = document.getElementById('upload-close');
    uploadClose.onclick = function () {
        imageInputFrame.value = null;
        upLoad.style.display = "none";
        jcropApi.destroy();
    }

    $('#image-send').click(function () {
        upLoad.style.display = "none";
        jcropApi.destroy();
    });

})();
