|(constructor)构造函数声明|接口说明|
|--|--|
|vector() (重点)|无参构造|
|vector (size_type n, const value_type& val = value_type())|构造并初始化n个val|
|vector (const vector& x) (重点)|拷贝构造|
|vector (inputlterator first, inputlterator last);|使用迭代器进行初始化构造|
    我们实际中不会直接定义const对象,因为没意义
    const对象基本都是在传参的过程中产生的
    而引用传参如果不改变对象,基本都会加const

### vector::insert
```c++
vector<int> v1;
push_back(1);
push_back(2);
push_back(3);
//运行结果:1,2,3
v1.insert(v1.bengin(),0);
//运行结果:0,1,2,3
v1.erase(v.bengin());
//运行结果:1,2,3