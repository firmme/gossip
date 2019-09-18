
//1. 页面加载完成后, 获取url上关键词数据 :  js : BOM(浏览器对象)  DOM(文档对象)  SCMAScript(基本语法)
// ?keywords=刘德华
var parmas = location.search;

//2. 切割请求参数, 获取=后面的值
var keywords = parmas.split("=")[1]; // 如果是中文, 会进行url编码

if(keywords == undefined) {

    location = "/index.html";
}
//3. url的解码操作
keywords = decodeURI(keywords)

//4. 回显到页面中
$("#inputSeach").val(keywords)

//5. 发送异步请求
ajaxQuery(1,12);


// 执行异步请求的方法
function ajaxQuery(page,pageSize) {
    //5.1 获取请求参数的数据
    var keywords = $("#inputSeach").val(); // 获取关键词
    var dateStart = $("[name=dateStart]").val();
    var dateEnd = $("[name=dateEnd]").val();
    var editor = $("[name=editor]").val();
    var source = $("[name=source]").val();
    console.log(keywords);
    var url = "/s.action"
    var param = {"keywords": keywords,"pageBean.page":page,"pageBean.pageSize":pageSize,
        "dateStart":dateStart,"dateEnd":dateEnd,"editor":editor,"source":source};
    //5.2 发送异步请求
    $.post(url, param, function (data) { // data:  resultBean对象

        //5.2.1: 获取返回的数据中状态信息: 如果为false. 给用户提示错误信息, 并跳转到首页
        var flag = data.flag;
        if (!flag) {
            // 如果为false, 表示, 请求失败了
            alert(data.error)
            location.href = "/index.html"
            return;
        }

        //5.2.2: 获取 newsList的数据
        var newsList = data.pageBean.newsList;

        //5.2.3 : 遍历newsList, 拼接数据的div列表
        var divStr = ""
        $(newsList).each(function () {
            // this 表示的当前遍历对象: news对象
            /*
             <div class="item">
                    <div class="title"><a href="#">北京传智播客教育科技股份有限公司</a></div>
                    <div class="contentInfo_src">
                        <a href="#"><img src="./img/item.jpeg" alt="" class="imgSrc" width="121px" height="75px"></a>
                        <div class="infoBox">
                            <p class="describe">
                                大数据学习已然成为时代所趋，相较于目前市面上的书籍及学习视频，大数据培训更适用于对大数据感兴趣的人群，通过培训老师丰富的大数据实战经验分享，
                                能在大数据初期学习中，少走很多弯路，后期的项目实战，结合企业及时下大数据热门应用，可快速接轨大数据发展方向。
                            </p>
                            <p><a class="showurl" href="www.itcast.cn">www.itcast.cn 2018-08</a> <span class="lab">隔壁老王 - 网易新闻</span>
                            </p>
                        </div>
                    </div>
                </div>
             */
            var docurl = this.docurl;
            docurl = docurl.substring(0,11)+"..."
            divStr+= "<div class=\"item\">\n" +
                "                        <div class=\"title\"><a href=\""+this.docurl+"\">"+this.title+"</a></div>\n" +
                "                        <div class=\"contentInfo_src\">\n" +
                "                            <a href=\"#\"><img src=\"./img/item.jpeg\" alt=\"\" class=\"imgSrc\" width=\"121px\" height=\"75px\"></a>\n" +
                "                            <div class=\"infoBox\">\n" +
                "                                <p class=\"describe\">\n" +
                "                                   "+this.content+" \n" +
                "                                    \n" +
                "                                </p>\n" +
                "                                <p><a class=\"showurl\" href=\""+this.docurl+"\">"+docurl+" "+this.time+"</a> <span class=\"lab\">"+this.editor+" - "+this.source+"</span>\n" +
                "                                </p>\n" +
                "                            </div>\n" +
                "                        </div>\n" +
                "                    </div>";

        });

        // 循环接收后: 将divStr设置到页面中
        //5.2.4:  将拼接好的新闻列表页的html  写入到页面中
        $(".itemList").html(divStr)

        // ------------------------以下内容为分页条的处理----------------------------------
        /*
        <ul>
            <li><a href="#">< 上一页</a></li>
            <li>1</li>
            <li>2</li>
            <li class="on">3</li>
            <li>4</li>
            <li>5</li>
            <li>6</li>
            <li>7</li>
            <li>下一页 ></li>
        </ul>
         */

        //  5.3 拼接分页条
        var page = data.pageBean.page;
        var pageNumber = data.pageBean.pageNumber;
        var  pageUi = "<ul>"
        // 添加首页

        pageUi+= "<li onclick = 'ajaxQuery(1,12)'><a href='#'>首 页</a></li>";
        //5.3.1: 拼接 上一页
        if(page > 1){
            pageUi+= "<li  onclick = 'ajaxQuery("+(page-1)+",12)' ><a href='#'>< 上一页</a></li>";
        }

        //5.3.2: 拼接  页码
        //  5.3.2.1  如果总页数 小于等于 7 的, 展示 所有的页码
        if(pageNumber <=7){
            for(var i = 1 ; i <= pageNumber ; i++){

                if(page == i){
                    pageUi+="<li class='on'>"+i+"</li>";
                }else{
                    pageUi+="<li onclick = 'ajaxQuery("+i+",12)' >"+i+"</li>";
                }
            }

        }else {
            // 5.3.2.2 : 如果总页数大于7页的,  当前页 在 1~4范围, 展示 !~7

            if(page<=4){

                for(var i = 1 ; i <= 7 ; i++){

                    if(page == i){
                        pageUi+="<li class='on'>"+i+"</li>";
                    }else{
                        pageUi+="<li onclick = 'ajaxQuery("+i+",12)' >"+i+"</li>";
                    }
                }
            }

            // 5.3.2.2 : 如果总页数大于7页的,  当前页 大于4并且小于总页数-2范围, 展示 当前页-3 ~ 当前页 +3
            if(page>4 && page <(pageNumber-2)){

                for(var i = (page-3) ; i <= (page+3) ; i++){

                    if(page == i){
                        pageUi+="<li class='on'>"+i+"</li>";
                    }else{
                        pageUi+="<li onclick = 'ajaxQuery("+i+",12)' >"+i+"</li>";
                    }
                }
            }

            // 5.3.2.3 : 如果总页数大于7页的,  当前页+3 大于总页数, 展示 总页数-7 ~ 总页数
            if((page+3) > pageNumber){

                for(var i = (pageNumber-7) ; i <= pageNumber ; i++){

                    if(page == i){
                        pageUi+="<li class='on'>"+i+"</li>";
                    }else{
                        pageUi+="<li onclick = 'ajaxQuery("+i+",12)' >"+i+"</li>";
                    }
                }
            }
        }


        //5.3.3: 拼接 下一页
        if(pageNumber > page){
            pageUi+="<li onclick = 'ajaxQuery("+(page+1)+",12)' ><a href='#'>下一页 ></a> </li>"
        }

        // 添加尾页:
        pageUi+= "<li onclick = 'ajaxQuery("+pageNumber+",12)'><a href='#'>尾 页</a></li>";

        pageUi +="</ul>"

        //5.4 将pageUi 设置到页面中
        $(".pageList").html(pageUi)

    }, "json");

}

function topkeyQuery(num) {
    var url = "/top.action";
    var param = {"num":num};
    $.get(url,param,function(data){ // List<Map>
        // <div class="item">美团</div>

        var divStr = "";
        $(data).each(function () {
            // this 一个个map
            divStr += "<div class='item' onclick='findByTopKey(this)'><span>"+this.topKey+"</span><span style='float: right;color: red'>"+this.score+"</span></div>";
        })

        $(".recommend").html(divStr);


    },"json");

}

function findByTopKey(obj) {  // obj 表示当前点击的这个div对象
    var topKey = $(obj).children(":first").text();

    location.href = "/list.html?keywords="+topKey;
}