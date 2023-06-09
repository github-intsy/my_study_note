### queue类
    queue是一个FIFO容器

    priority_queue是容器适配器
### 容器适配器
    容器适配器 都不支持迭代器遍历，因为他们通常都包含一些特殊性质
    如果支持迭代器随便遍历，那他们无法很好的保持他的性质

### priority_queue
```c++
默认是大的优先级高
如果变成小的优先级高,应给在构造对象时给greater对象

priority_queue<int, vector<int>, greater<int>> pq;
```
### 仿函数
    他的对象可以像函数一样去使用
    优先级队列priority_queue默认是大堆
    但是内部的仿函数默认传的是less小于
    所以应该转换parent和child的位置
    优先级队列使用仿函数控制是大堆还是小堆
```c++
template<class T>
struct less
{
    bool operator()(const T& x1, const T& x2)
    {
        return x1 < x2;
    }
}

less<int> lessFun;
cout << lessFun(1, 2) << endl;
```
    容器:string/vector/list/deque
    适配器: stack/queue/priority_queue
    选代器: iterator/const iterator/reverse iterator/const reverse iterator
    算法:sort/find/reverse
    仿函数: less/greater
### 模拟实现
```c++
template<class T, class Container = vector<int>>
class priority_queue
{
public:
    priority_queue()
        :_con()
    {}

    

    void push(const T& val)
    {
        _con.push_back(val);
        _AdjustUp(_con.size() - 1);
    }

    void pop()
    {
        if (empty())
            return;
        swap(_con[0], _con[_con.size() - 1]);
        _con.pop_back();
        _AdjustDown(0);
    }

    const T& top() const
    {
        // 堆顶元素不允许修改，因为：堆顶元素修改可以会破坏堆的特性
        return _con.front();
    }

    bool empty()
    {
        return _con.empty();
    }

    void size()
    {
        return _con.size();
    }
private:

    void _AdjustUp(size_t child)
    {
        size_t parent = (child - 1) / 2;
        while (child > 0)
        {
            if (_con[parent] < _con[child])
            {
                swap(_con[parent], _con[child]);
                child = parent;
                parent = (child - 1) / 2;
            }
            else
            {
                break;
            }
        }
    }

    void _AdjustDown(int parent)
    {
        int child = parent * 2 + 1;

        while (child < _con.size())
        {
            if (child + 1 < _con.size() && _con[child] < _con[child + 1])
                ++child;
            if (_con[parent] < _con[child])
            {
                swap(_con[parent], _con[child]);
                parent = child;
                child = parent * 2 + 1;
            }
            else
            {
                break;
            }
        }
    }
    Container _con;
};
```
#### 改进版本
```c++
template<class T>
struct less
{
    bool operator()(const T& left, const T& right)
    {
        return left < right;
    }
};

template<class T>
struct greater
{
    bool operator()(const T& left, const T& right)
    {
        return left > right;
    }
};

template<class T, class Container = vector<T>, class Compare = less<T>>
class priority_queue
{
public:
    
    priority_queue()
        :_con()
    {}

    void push(const T& val)
    {
        _con.push_back(val);
        _AdjustUp(_con.size() - 1);
    }

    void pop()
    {
        if (empty())
            return;
        swap(_con[0], _con[_con.size() - 1]);
        _con.pop_back();
        _AdjustDown(0);
    }

    const T& top() const
    {
        // 堆顶元素不允许修改，因为：堆顶元素修改可以会破坏堆的特性
        return _con.front();
    }

    bool empty()
    {
        return _con.empty();
    }

    void size()
    {
        return _con.size();
    }
private:

    void _AdjustUp(size_t child)
    {
        Compare com;
        size_t parent = (child - 1) / 2;
        while (child > 0)
        {
            if (com(_con[parent] , _con[child]))
            {
                swap(_con[parent], _con[child]);
                child = parent;
                parent = (child - 1) / 2;
            }
            else
            {
                break;
            }
        }
    }

    void _AdjustDown(int parent)
    {
        Compare com;
        int child = parent * 2 + 1;

        while (child < _con.size())
        {
            if (com(child + 1 , _con.size()) && com(_con[child] , _con[child + 1]))
                ++child;
            if (com(_con[parent] , _con[child]))
            {
                swap(_con[parent], _con[child]);
                parent = child;
                child = parent * 2 + 1;
            }
            else
            {
                break;
            }
        }
    }
    Container _con;
};
```
### deque
    支持任意位置的插入删除也支持随机访问,也就是说它既有vector的优点,也有list的优点

    虽然看起来好像是可以替代vector和list的一个容器,但是实际上呢?
    随机访问不容乐观.所以他没有办法替代vector和list
    vector->sort->time:381
    deque->sort->time:1515

    缺点:
    1. 大量的operator[]的效率低
    2. 迭代器的遍历相对复杂,效率也有一些影响
#### deque是如何管理数据的?

    deque并不是真正连续的空间，而是由一段段连续的小空间拼接而成的，实际deque类似于一个动态的二维数组，

    通过创建多个大小为8的小数组,称之为buffer,然后通过中控映射的方法

    如何实现operator[]的随机访问呢?
    需要计算访问的第i个数据在哪个buffer,当数据量大了以后,访问效率就变低了

    中控映射->建立一个指针数组,记录小buffer的地址,然后通过迭代器iterator访问数据