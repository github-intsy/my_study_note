- cin >> a
  - cin.operator>>(a)
- cout << a
  - cout << operator(a)
- c++中cout和cin能自动识别对象类型,因为本质他是函数重载实现的

---
```c++
int main()
{
    //读写一个结构体
    info win = {"小明", 18};

    ofstream ofs("test.txt");   //fopen("W")
    ofs << win.name << endl;
    ofs << win.age << endl;     //这里必须要求换行,如果没有换行识别不出来
    //所以需要在类的每个元素之间增加空格或者换行
    ofs.close;

    info rin;
    ifstream ifs("test.txt");
    ifs >> rin.name;
    ifs >> rin.age;

    return 0;

}
```
---
将其他类型转换成string类型的字符串
```c++
#include<sstream>
int main()
{
    info win = {"小明", 18};
    ostringstream ost;
    ost << win.name;
    ost << win.age;

    string str1 = ost.str();
    cout << str1 << endl;
    return 0;
}
输出: 小明18
```
```c++
#include<sstream>
int main()
{
    //序列化字符串
    info win = {"小明", 18};
    ostringstream ost;
    ost << win.name;
    ost << win.age;

    //网络另一端接收后可以解析数据
    //反序列化
    istringstream ist;
    ist.str(str1);
    info rin;
    ist >> rin.name;
    ist >> rin.age;
    return 0;
}
最后将解析出来的数据储存到rin的结构体中
```