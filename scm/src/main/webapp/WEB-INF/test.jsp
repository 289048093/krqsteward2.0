<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>

    <script>

        window.onload = function() {
            document.getElementById("addBtn").onclick = function() {
                var $form = document.getElementById("form1");

                $form.submit();
            }

            document.getElementById("updateBtn").onclick = function() {
                var $form = document.getElementById("form1");
                var id = document.getElementById('id').value;
                if(!id) {
                    alert("请输入id");
                    return;
                }

                $form.submit();
            }
        }

    </script>
</head>
<body>
<form id="form1" action="save" method="post">
    产品名称: <input type="text" name="name"  /><br/>
    sku: <input type="text" name="sku"  /><br/>
    buyPrice: <input type="text" name="buyPrice"  /><br/>
    <input type="button" value="添加" id="addBtn" />
    <input type="button" value="修改" id="updateBtn" />
    id: <input type="text" id="id" name="id"  /><br/>
</form>


</body>
</html>