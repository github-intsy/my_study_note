    priority_queue和queue都是容器适配器
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