<#assign base=request.contextPath />
<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <title>Title</title>

</head>
<body>
    用户名：<input type="text" name="userName" id="userName"/>
    密码:<input type="password" name="passWord" id="passWord"/>
    <input type="button" value="登录" id="login_btn">
    <script src="${base}/static/js/jquery-2.1.4.min.js"></script>

    <script>
        $("#login_btn").click(function () {
            var userName=$("#userName").val();
            var passWord=$("#passWord").val();
            if(notEmpty(userName)&&notEmpty(passWord)){

                $.ajax({
                    url:"/user/login",
                    contentType : 'application/x-www-form-urlencoded; charset=UTF-8',//防止乱码
                    type:"POST",
                    data:{userName:userName,passWord:passWord},
                    dataType:"json",
                    success:function(data){
                        console.log(data);
                        if(data.code==1){
                            $(location).attr('href','/index/1');
                        }else{
                            console.log(data.message);
                            /*$(".login_error").text(data.message);
                            errorAnimate();*/
                        }
                    },
                    error:function(data){
                        console.log(data.message);
                        //$(".login_error").text(data.message);
                        //errorAnimate();
                    }
                });
            }else{
                layer.msg('账号密码不能为空！');
                //errorAnimate();
            }
        })

        function notEmpty(str){
            if(""!=str&&str!=null&&str!="undefined"){
                return  true;
            }
            return false;
        }
    </script>
</body>
</html>