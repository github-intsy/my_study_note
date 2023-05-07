    stack类是一个FIFO的容器适配器
    这个容器没有迭代器,因为它要维持一个容器适配器的性质
    stack不能使用vector,因为vector没有提供pop_front

### 模拟实现
```c++
template<class T, class Container = list<T>>
class stack
{
public:
    void push(const T& val)
    {
        _con.push_back(val);
    }
    void pop()
    {
        _con.pop_back();
    }
    T& top()
    {
        return _con.back();
    }
    size_t size()
    {
        return _con.size();
    }
    bool empty()
    {
        return _con.empty();
    }

private:
    Container _con;
};
```
    实际中stack和queue是通过deque实现的
    deque支持任意位置插入删除,
    也支持随机访问,也就是说它既有vector的优点,也有list的优点
    看起来好像是可以替代vector和list实际上?
    不行

    为什么?
    因为随机访问情况不容乐观,所有它没法替代list和vector
    vector->sort->time:381
    deque->sort->time:1515