<h1>javaweb bookstore_be api文档</h1>

<h4>已登录的请求头验证： "Authorization": "token"</h4>

**注册 post: /api/register**  
req:  
{  
>   user_num: '',  
>   user_pwd: ''  

}  
res:  
{  
>   status: 1, // 2 失败  
>   message: '',  
>   data: ''//失败原因
  
}  
  
**登录 post: /api/login**  
req:  
{  
>    user_num: '',  
>    user_pwd: ''  

}  
res:  
{  
>   status: 1, // 2失败  
>   message: '',// 失败原因  
>   data:{  
>>       token: ''  
>    }  

}  
  
**获取书籍分类 get: /api/getCategory**  
req: null  
res:   
{  
>   "data":[  
>>          {"name":"文学艺术","id":9},  
>>          {"name":"少儿童书","id":10},  
>>          {"name":"人文社科","id":13}
>  ],  
>   "message":"请求成功",  
>   "status":1  

}  

**查询用户信息 get: /api/searchUser?user_name="xxx"  
/api/searchUser返回全部用户**  
res:   
{  
>   "data":[  
>>          {"user_id":"","user_name":9,"user_type": 0/1},  
>  ],  
>   "message":"请求成功",  
>   "status":1(成功)/2(用户不存在)    
  
}  
**修改订单状态 post: /api/updateOrder**  
req:  
{  
> "order_id": 001  
  
}
res:  
同上post，成功或失败

**登录用户添加购物车 post: /api/addBookWithToken**  
req:  
{  
>   "book_id": 001,  
>   "book_count": 10,  
  
}  
res:  
同上post，成功或失败  

**登录用户查看购物车 get:  /api/getShopCarWithToken**  
res:  
{  
>   "data":[  
>>          {"book_id": 001,  
>>           "book_name":"xxx",  
>>           "book_author":"xxx",  
>>           "book_price": 9.9,
>>           "book_publishing":"xxx",
>>           "book_smimg":"http://xxxx.jpg"
>>          },
>>          {
>>             ...
>>          } 
>  ],  
>   "message":"请求成功",  
>   "status":1 
  
}

**提交订单 post:  /api/submitOrder**  
req:  
[  
> {  
>>    "book_id": 001,  
>>    "book_count": 10  

> },  
  {  
>>     "book_id": 002,
>>     "book_count": 10  
>  }  

]  
res:  
同上post，成功或失败  

**管理员添加书籍信息 post:  /api/addBookToSql**  
req:  
{  
> "book_name": "xxx",  
  "book_author": "xxx",  
  "book_price": "xxx",  
  "book_publishing": "xxx",  
  "book_smimg": "http://xxx.jpg",  
  "book_mdimg": "http://xxx.jpg",  
  "book_describe": "xxx",  
  "c-id": 13  
  
}  

**登录用户查看自己订单 get:  /api/getUserOrders**  
res:  
{  
>   "status": 0,  
    "message": "xxx",  
    "data": [  
>>        {
>>            "order_id": 001,  
>>            "is_finsh": 0(未完成)/1,
>>            "total_price"; 99,
>>            "books": [
>>>                {
>>>                    "book_id": 001,
>>>                    "book_name": "xxx",
>>>                    "book_author": "xxx",
>>>                    "book_publishing": "xxx",
>>>                    "book_smimg": "http://xxx.jpg",
>>>                    "book_count": 5
>>>                }
>>           ]
>>        }
>    ]

}  


**书籍分页 get: /api/getBookList?page=1&size=12**  
res:  
{  
>  status: 1,  
   message: "xxx",  
   data: [  
>>      {  
>>         "book_id": 001,
>>         "book_name": "xxx",
>>         "book_author": "xxx",
>>         "book_publishing": "xxx",
>>         "book_smimg": "http://xxx.jpg",  
>>         "book_price": 9.9  
>>      }  
>   ]
  
}  

**购物车里的书籍增一或减一操作 post: api/updateCarBookCount**   
req:  
{  
>  "type":  -1(减)/1(加),  
  "book_id": 001  
  
}  
res:  
同上，依据status判断成功与否