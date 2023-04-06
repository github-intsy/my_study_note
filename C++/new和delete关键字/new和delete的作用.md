### new和delete
    new关键词的作用是在堆上开辟一个空间
    在开辟一个空间和需要使用delete释放申请的空间
    c++创造new和delete是为了弥补c语言中malloc的缺陷

    1. new会调用构造函数,失败抛异常,malloc失败了返回0.  使用效果
    2. malloc是一个函数,new是一个操作符 概念性质
    3. malloc用法:参数传字节数,返回值是void*.new后面跟申请对象的类型,返回值是类型的指针 使用方法
#### c的写法
```c
int* p1 = (int*)malloc(sizeof(int));//开辟一个int大小的空间
int* p2 = (int*)malloc(sizeof(int)*10);//开辟10个int大小的空间
//在堆上开辟空间

free(p1);
free(p2);//释放空间
p1 = p2 = NULL;
//将开辟的空间还给操作系统
```
#### c++的写法
```c++
int* p1 = new int;///开辟一个int大小的空间
int* p2 = new int[10];////开辟10个int大小的空间
//在堆上开辟空间

delete p1;
delete[] p2;
//释放空间
```
*new和delete在创建和销毁一片空间时,会连续调用n次构造函数和析构函数*