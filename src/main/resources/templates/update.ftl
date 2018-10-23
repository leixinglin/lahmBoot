<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    <form action="/updateCar" method="post">

        <input type="hidden" value="${updateCar.id}" name="id">

        日期:<input type="date" value="${updateCar.date?string("yyyy-MM-dd")}" name="date"/><br/>
        姓名:<input type="text" value="${updateCar.name}" name="name"/><br/>

        价格:<input type="text" value="${updateCar.price}" name="price"/><br/>
        <input type="submit" value="提交"/>
    </form>
</body>
</html>