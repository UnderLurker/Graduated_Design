
//头像上传
function headImgUpLoad(){
    let imageInput=document.getElementById('image-input');
    let imageInputFrame=document.getElementById('image-input-frame');
    let upLoad=document.getElementsByClassName('up-load')[0];
    imageInput.onclick=function(){
        imageInputFrame.click();
    }
    //预览图像
    imageInputFrame.onchange=function(){
        upLoad.style.display="flex";
        let picture=this.files[0],img;
        img=new Image();
        img.onload=function(){
            let previewImg=document.getElementById('preview-img');
            previewImg.setAttribute('src',this.src);
            
            //图片裁剪
            // let canvas = document.getElementById('preview-img-canvas');
            // let canvasWidth=canvas.getBoundingClientRect().width;
            // let scale=this.width/canvasWidth;
            // let clientHeight=this.height/scale;
            // canvas.style.height=""+clientHeight+"px";

            // let ctx = canvas.getContext('2d');

            // console.log(canvasWidth+","+clientHeight);
            // ctx.drawImage(img,0,0,canvasWidth,clientHeight);
        }
        img.src=URL.createObjectURL(picture);
    }
    let uploadClose=document.getElementById('upload-close');
    uploadClose.onclick=function(){
        upLoad.style.display="none";
    }
    
    let submit=document.getElementsByClassName('submit')[0];
    submit.onclick=function(){
        document.getElementById('image-send').click();
        upLoad.style.display="none";
    }
}
