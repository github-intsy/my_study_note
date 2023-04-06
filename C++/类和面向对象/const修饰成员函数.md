###### 代码
```c++
    class Date
    {
    public:
        void Print()
        {
            cout << _year << '-' << _month << '-' << _day << endl;
        }
    private:
        int _year;
        int _month;
        int _day;
    };

    void f(const Date& d)
    {
        d.Print();          //不行->this指针的创建引起了权限的放大
    }
    int main()
    {
        Date d1(2023,3,3);
        return 0;
    }
```
    优化方案:使用const修饰成员函数
```C++
    void Print() const
    {
        cout << _year << '-' << _month << '-' << _day << endl;
        _year = 10; (X)
        //不能修改成员变量了,因为const修饰保护了*this
    }

const Date* d1;         ->指向的对象
Date const *d2;         ->指向的对象
Date* const d3;         ->指针本身
```
const在*之前修饰的是指向的内容
在*之后修饰的是指针本身
权限只能缩小使用,不能放大调用
结论:什么时候需要把成员方法加上const?只要这个成员方法不修改成员对象就可以加上const