可以自动推导变量
```c++
int a = 10;
auto b = a;
//此时b的类型就是a的类型int

cout << typeid(a).name() << endl;

typeid(a).name() // 可以查看变量或者对象的类型

可以auto& c = a;
    auto* d = &a;//int*
    auto e = &a; //int*
```
可以一行同时定义多个变量
```c++
auto a = 1,b = 2;   //可以
auto c = 1,d = 2.0; //不行

参数不能使用auto
void Swap(auto a, auto b) // 不行

参数不能使用数组来推理
auto a[] = {1,2,3,4,5}; // 不行
```
### auto实例:
```c++
std::map<std::string, std::string> dict;

std::map<std::string, std::string>::iterator it1 = dict,begin();
auto it2 = dict, begin(); //使用auto来进行优化
```