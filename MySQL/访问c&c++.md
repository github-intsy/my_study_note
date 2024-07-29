
1. `mysql_init()`创建MYSQL指针
2. `mysql_close()`释放MYSQL指针资源
3. `mysql_real_connect()`链接数据库

## 增删改操作
```c++
#include <iostream>
#include <mysql/mysql.h>
#include <unistd.h>
#include <string>
using namespace std;
const string host = "localhost";
const string user = "gsy";
const string passwd = "GaoIcloud@0.";
const string db = "school";
const unsigned int port = 3306;
int main()
{
    MYSQL *my = mysql_init(nullptr);//对象初始化
    if (nullptr == my)
    {
        cerr << "mysql init error" << endl;
    }
    //登录数据库
    if (mysql_real_connect(my, host.c_str(), user.c_str(), passwd.c_str(), db.c_str(), port, nullptr, 0) == nullptr)
    {
        cerr << "connect MYSQL error" << endl;
        return 2;
    }
    //设置传输字符
    mysql_set_character_set(my, "utf8");
    while(true)
    {
        cout<<">>>"; fflush(stdout);
        if(!getline(cin,s) || s=="quit")
        {
            puts("bye bye");
            break;
        }
        //传入请求句柄
        int n = mysql_query(my, s.c_str());
        if(n==0) puts("success");
        else puts("default");
    }
    cout << "connect success" << endl;
    mysql_close(my);
    return 0;
}
```

## select查看操作
>当前只能查看数据内容，不能查看首部行内容
```c++
#include <iostream>
#include <mysql/mysql.h>
#include <unistd.h>
#include <string>
using namespace std;
const string host = "localhost";
const string user = "gsy";
const string passwd = "GaoIcloud@0.";
const string db = "school";
const unsigned int port = 3306;
int main()
{
    MYSQL *my = mysql_init(nullptr);
    if (nullptr == my)
    {
        cerr << "mysql init error" << endl;
    }
    if (mysql_real_connect(my, host.c_str(), user.c_str(), passwd.c_str(), db.c_str(), port, nullptr, 0) == nullptr)
    {
        cerr << "connect MYSQL error" << endl;
        return 2;
    }
    mysql_set_character_set(my, "utf8");

    string s = "select * from student";
    int n = mysql_query(my, s.c_str());
    if (n == 0)
        puts("success");
    else
        puts("errno");
    
    MYSQL_RES *res = mysql_store_result(my);//转储select显示信息
    //获得行数
    my_ulonglong rows = mysql_num_rows(res);
    //获得列数
    my_ulonglong fields = mysql_num_fields(res);
    //
    for(int i=0;i<mysql_num_rows(res);++i)
    {
        MYSQL_ROW row = mysql_fetch_row(res);
        //获取当前行的当前列，类似迭代器
        //每次获取当前行的一个列，自动下移一行
        for(int j=0;j<mysql_num_fields(res);++j)
        {
            cout<<*(row+j)<<'\t';
        }
        cout<<'\n';
    }
    cout << "connect success" << endl;
    mysql_close(my);
    return 0;
}
```
---
获取属性
```c++
//属性
    MYSQL_FIELD* mysql_array = mysql_fetch_fields(res);
    for(int i = 0; i < fields; ++i)
    {
        cout << mysql_array[i].name << '\t';
    }
    cout<<'\n';
```

>由于`MYSQL_RES *res = mysql_store_result(my);`中`res`为了存储数据开辟了大量的空间，不用的时候，需要释放掉，使用`mysql_free_result(res);`释放

>res是结果集，存储select的结果；使用`mysql_store_result`能将结果存储到结果集