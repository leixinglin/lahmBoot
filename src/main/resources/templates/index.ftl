<!DOCTYPE html>
<html lang="zh_CN">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    <form action="/index/1">
        日期：<input type="date" name="date" value="${param.date}"/>
        名称：<input type="text" name="name" value="${param.name}"/>
        <input type="submit" value="查询"/>
    </form>
    <table width="700px">

        <thead>
            <tr>
                <th>
                    id
                </th>
                <th>
                    出厂日期
                </th>
                <th>
                    汽车名称
                </th>
                <th>
                    价格
                </th>
                <th>
                    操作
                </th>
            </tr>
        </thead>
        <tbody>
            <#list carList as car>
                <tr>
                    <th>
                        ${car.id}
                    </th>
                    <th>
                        ${car.date?string("yyyy-MM-dd HH:mm:ss")}
                    </th>
                    <th>
                        ${car.name}
                    </th>
                    <th>
                        ${car.price}
                    </th>
                    <th>
                       <a href="/updatePage/${car.id}">修改</a>
                        <a href="/delete/${car.id}">删除</a>
                    </th>
                </tr>
            </#list>
        </tbody>
        <tfoot>
            <tr>
                <td>
                    当前页：${pageInfo.pageNum}
                </td>
                <td>
                    总行数：${pageInfo.total}
                </td>
                <td>
                    <#if (pageInfo.pageNum>1)>
                        <a href="/index/${pageInfo.pageNum-1}?name=${param.name}&date=${param.date}" >上一页</a>
                    </#if>

                </td>
                <td>

                    <#if (pageInfo.pageNum<pageInfo.lastPage)>
                        <a href="/index/${pageInfo.pageNum+1}?name=${param.name}&date=${param.date}" >下一页</a>
                    </#if>
                </td>
            </tr>
        </tfoot>
    </table>
    <a href="/savePage"> 添加</a>  <a href="/user/logout">退出</a>
    <br/>
    当前用户：<@shiro.principal property="userName"/>
  <@shiro.hasRole  name="user">
        <span>有用户权限</span>
    </@shiro.hasRole>
    <@shiro.hasRole name="admin">
        <span>有管理员权限</span>
    </@shiro.hasRole>
</body>
</html>