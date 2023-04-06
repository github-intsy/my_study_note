#####
    既然已经有了malloc和free,new和delete的意义何在?
    1. 对于上面内置类型,他们的效果是一样的
    2. 对于自定义类型,效果就不一样.
    3. malloc只申请空间.new申请空间+构造函数初始化
```c++
class
{
public:
    A()
    {
        _a = 0;
        cout << "A()" << endl;
    }
    ~A()
    {
        cout << "~A()" << endl;
    }
private:
    int _a;
};

int main()
{
    A* p3 = (A*)malloc(sizeof(A));//申请空间
    A* p4 = new A;                //申请空间+构造函数初始化

    free(p3);//释放空间
    delete p4;//析构函数+释放空间
    return 0;
}
```
##### operator new 和 malloc的区别
    结论:
    使用方式都一样,处理错误的方式不一样
    malloc失败返回0
    operator new失败抛异常
```c++
int main()
{
    A* p1 = (A*)malloc(sizeof(A));
    A* p3 = (A*)operator new (sizeof(A));

    size_t size = 2;
    void* p4 = malloc(size*1024*1024*1024);
    cout << p4 << endl;//失败返回0

    try
    {
        void* p5 = operator new(size*1024*1024*1024);
        cout << p5 endl;//失败抛异常(面向对象处理错误的方式)
    }
    catch
    {
        cout << e.what() << endl;
    }
}
```
#### 深入了解new
    operaotr new是malloc的优化版本
    new是operator new的优化版本
```c++
operator new == malloc + 失败抛异常
new == operator new + 析构函数
所以new和malloc不一样的地方:
1. 调用析构函数初始化
2. 失败了抛异常
```
#### delete
    delete和free不一样的地方:
    1. 调用析构函数清理
    2. operator free和 free没有区别
    3. 因为释放空间失败都是直接终止进程,是因为要和operator new成对出现才出现的operator free
```c++
int main()
{
    A* p1 = new A;
    delete p1;

    //想模仿上面的行为
    A* p2 = (A*)operator new(sizeof(A));
    //对已经存在的一块空间调用析构函数初始化. 定位new/replacement new
    new(p2)A(10);   //new(空间的指针)类型(参数)

    p2->~A();
    operator delete(p2);
    
    return 0;
}