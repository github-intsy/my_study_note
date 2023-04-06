实体化->就是用自己定义的类型定义出对象
1.内置类型,基本类型  int/char/double
2.自定义类型,  class/struct

##### 代码
```c++
    #include<iostream>
    using namespace std;

    class Data
    {
    public:
        void Print()
        {
            cout << _a << endl;     //崩溃
        }

        void Show()
        {
            cout << "show()" << endl;//正常运行
        }
    private:
        int _a;
    }

    int main()
    {
        A* p = nullptr;
        p->Print();         //为什么?->结构体指针p是指向空的,直接调用私有的空间需要使用this指针
                            //而传递的this指针指向空
        p->Show();          //show方法只是直接调用show方法打印,没有访问私有对象
        return 0;
    }
```
this指针存放在哪里?(也就是存在进程地址空间的哪一块区域?)
答:栈上的,因为他是一个形参(ps: vs下是在exc这个寄存器,来传递)

##### 上面代码的实际情况
```c++
    int main()
    {
        A* p = nullptr;
        p->Print(&p);
        p->Show(&p);
        return 0;
    }

    void Print(A* this)
    {
          cout << this -> _a << endl;     //崩溃
    }
```