###post: /api/register  
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
  
###post: /api/login  
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
  
###get: /api/getCategory  
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