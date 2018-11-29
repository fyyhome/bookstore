
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
  </head>
  <body>
  测试获取每类书籍信息:<br>
  <form action="/api/getSpecialKind">
    书类编号c_id:<input type="text" name="c_id">
    <input type="submit" value="提交">
  </form>
  测试获取每本书信息:<br>
  <form action="/api/getBookInformation">
    书籍编号book_id:<input type="text" name="book_id">
    <input type="submit" value="提交">
  </form>

  session添加购物车:<br>
  <form action="/api/addToShopCar">
    书籍编号book_id:<input type="text" name="book_id">
    <input type="submit" value="提交">
  </form>

  购物车中商品数量加一:<br>
  <form action="/api/goodsNumPlus">
    书籍编号book_id:<input type="text" name="book_id">
    <input type="submit" value="提交">
  </form>
  购物车中商品数量减一:<br>
  <form action="/api/goodsNumSub">
    书籍编号book_id:<input type="text" name="book_id">
    <input type="submit" value="提交">
  </form>

  修改购物车指定书籍数量:<br>
  <form action="/api/updateGoodsNum">
    书籍编号book_id:<input type="text" name="book_id">
    书籍数目book_num:<input type="text" name="book_num">
    <input type="submit" value="提交">
  </form>
  </body>
</html>
