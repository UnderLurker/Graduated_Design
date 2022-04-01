var emotionNameList = [
    'c-1',
    'c-2',
    'c-3',
    'c-4',
    'c-5',
    'c-6',
    'c-7',
    'c-8',
    'c-9',
    'c-10',
    'c-11',
    'c-12',
    'c-13',
    'c-14',
    'c-15',
    'c-16',
    'c-17',
    'c-18',
    'c-19',
    'c-20',
    'c-21',
    'c-22',
    'c-23',
    'c-24',
    'c-25',
    'c-26',
    'c-27',
    'c-28',
    'c-29',
    'c-30',
    'c-31',
    'c-32',
    'c-33',
    'c-34',
    'c-35',
    'c-36',
    'c-37',
    'c-38',
    'c-39',
    'c-40',
    'c-',
    'c-aixin',
    'c-aoman',
    'c-baiyan',
    'c-baoquan',
    'c-bishi',
    'c-caidao',
    'c-chigua',
    'c-ciya',
    'c-coolb',
    'c-coshuaji',
    'c-diaoxie',
    'c-doge',
    'c-dogehechi',
    'c-dogexiaoku',
    'c-dogeyuanliangta',
    'c-doujiyanhuaji',
    'c-erha',
    'c-erhading',
    'c-fadai',
    'c-fanu',
    'c-fived',
    'c-fivef',
    'c-fivem',
    'c-fiveo',
    'c-fivey',
    'c-fy',
    'c-hahaha',
    'c-haixiu',
    'c-han',
    'c-hehe',
    'c-heiha',
    'c-heixian',
    'c-hejiu',
    'c-hongyaowan',
    'c-huaixiao',
    'c-huaji',
    'c-huanhu',
    'c-huoba',
    'c-jingya',
    'c-jizhi',
    'c-keai',
    'c-kelian',
    'c-koubi',
    'c-ku',
    'c-kuan',
    'c-kuanlvmao',
    'c-liuhanhuaji',
    'c-liulei',
    'c-lvmao',
    'c-lvyaowan',
    'c-meigui',
    'c-miaomiao',
    'c-mojinghuaji',
    'c-naikezui',
    'c-nanguo',
    'c-nb',
    'c-ok',
    'c-oned',
    'c-onef',
    'c-onem',
    'c-oneo',
    'c-oney',
    'c-oy',
    'c-pen',
    'c-penxue',
    'c-piezui',
    'c-pu',
    'c-pyjiaoyi',
    'c-qiang',
    'c-qinqin',
    'c-qqdoge',
    'c-ruo',
    'c-se',
    'c-shounuehuaji',
    'c-shui',
    'c-teny',
    'c-tuosai',
    'c-tushe',
    'c-twod',
    'c-twof',
    'c-twom',
    'c-twoo',
    'c-twoy',
    'c-ty',
    'c-weiqu',
    'c-weiweiyixiao',
    'c-weixiao',
    'c-wozuimei',
    'c-wulian',
    'c-wunai',
    'c-wuyu',
    'c-wuzuixiao',
    'c-xiaoku',
    'c-xiaoyan',
    'c-xinsui',
    'c-ye',
    'c-yinxian',
    'c-yiwen',
    'c-zaijian',
    'c-zhoumei',
];
$(document).ready(setCoolapkEmotion);

$('head').append($('<style type="text/css">.coolapk-emotion {width: 1em; height: 1em; vertical-align: -0.15em; fill: currentColor; overflow: hidden;}</style>'));

function setCoolapkEmotion() {
    for (let i in emotionNameList) {
        let x = emotionNameList[i];
        if ($('i.' + x).length) {
            $('i.' + x).after($('<svg class="coolapk-emotion" aria-hidden="true"><use xlink:href="#' + x + '"></use></svg>')).remove();
        }
    }! function (M) {
            c = (l = document.getElementsByTagName("script"))[l.length - 1].getAttribute("data-injectcss");
        if (c && !M.__iconfont__svg__cssinject__) {
            M.__iconfont__svg__cssinject__ = !0;
            try {
                document.write("<style>.svgfont {display: inline-block;width: 1em;height: 1em;fill: currentColor;vertical-align: -0.1em;font-size:16px;}</style>")
            } catch (l) {
                console && console.log(l)
            }
        }! function (l) {
            if (document.addEventListener)
                if (~["complete", "loaded", "interactive"].indexOf(document.readyState)) setTimeout(l, 0);
                else {
                    var c = function () {
                        document.removeEventListener("DOMContentLoaded", c, !1), l()
                    };
                    document.addEventListener("DOMContentLoaded", c, !1)
                }
            else document.attachEvent && (t = l, p = M.document, h = !1, (i = function () {
                try {
                    p.documentElement.doScroll("left")
                } catch (l) {
                    return void setTimeout(i, 50)
                }
                a()
            })(), p.onreadystatechange = function () {
                "complete" == p.readyState && (p.onreadystatechange = null, a())
            });

            function a() {
                h || (h = !0, t())
            }
            var t, p, h, i
        }(function () {
            var l, c, a, t, p, h;
            (l = document.createElement("div")).innerHTML = i, i = null, (c = l.getElementsByTagName("svg")[0]) && (c.setAttribute("aria-hidden", "true"), c.style.position = "absolute", c.style.width = 0, c.style.height = 0, c.style.overflow = "hidden", a = c, (t = document.body).firstChild ? (p = a, (h = t.firstChild).parentNode.insertBefore(p, h)) : t.appendChild(a))
        })
    }(window);
}