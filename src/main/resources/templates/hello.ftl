<html>
<body>
welcome ${name}  to freemarker!
</body>

<#--
<p>性别：
           <#if gender==0>
              女
           <#elseif gender==1>
              男
           <#else>
              保密
           </#if>
</p>

 <#include "/header.ftl" >
<h4>我的好友：</h4>
       <#list friends as item>
           姓名：${item.name} , 年龄${item.age}
           <br>
       </#list>
-->
</html>