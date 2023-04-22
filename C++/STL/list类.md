    list类是指带头双向循环链表
### 为什么会有list?
    补充vector的缺点存在的

    vector缺点:
    1、头部和中部的插入删除效率低。O(N),因为需要挪动数据。
    2、插入数据空间不够需要增容。
    增容需要开新空间、拷贝数据、释放旧空间，会付出很大的代价。
    优点:
    1、支持下标的随机访问.
    间接的就很好的支持排序、二分查找、堆算法等等。


    list出现就是为了解决vector的缺陷

    优点
    1、list头部、中间插入不再需要挪动数据，效率高。O(1)
    2、list插入数据是新增节点，不需要增容。缺点
    缺点
    1、不支持随机访问。

    所以实际使用中vector和list是相辅相成的两个容器

### 迭代器失效
```c++
list<int>::iterator pos = find(lt.begin(), lt.end(), 3);
if(pos != lt.end())
{
    lt.insert(pos, 30);     //这里的insert以后pos失效了吗?
    lt.erase(pos);          //不失效
}
```
```c++
vector<int>::iterator pos = find(lt.begin(), lt.end(), 3);
if(pos != lt.end())
{
    lt.insert(pos, 30);     //这里的insert以后pos失效了吗?
    lt.erase(pos);          //失效
}
```
    迭代器失效了没什么事情,是去访问失效的迭代器就会出现问题

#### 迭代器失效的总结
1. vector的iterator, insert, push_back, erase都会导致失效
2. list的iterator,erase会导致失效

### list类兼容自定义类型
```c++
template<class T>

T* operator->()
{
    return &_node->_date;
    返回一个节点类型的地址,这样就可以通过这个地址调用自定义类型
}

cout << i->_year;
cout << (*i)._year; //也可以这样使用
理解:实际应该是i->->_year,但是为了可读性,编译器特殊化处理了一下
```

### 模拟实现
#### erase
```c++
iterator erase(iterator& it)
{
    Node* pos = it._node;
    Node* prev = pos->_prev;
    Node* next = pos->_next;
    delete pos;
    prev->_next = next;
    next->_prev = prev;
    return iterator(next);
}
//接受迭代器类型,获得节点类型的指针,用节点指针获得前后指针
//返回下一个节点的指针
```
#### node
```c++
template<class T>
struct __list_node
{
    __list_node<T>* _next;
    __list_node<T>* _prev;
    T _data;

    __list_node(const T& x = T())
        :_data(x)
        , _prev(nullptr)
        , _next(nullptr)
    {}
};
//自定义节点类型
```
#### iterator
```c++
template<class T, class Ref, class Ptr>
struct __list_iterator
{
    typedef __list_node<T> Node;
    typedef __list_iterator<T, Ref, Ptr> self;
    
    Node* _node;
    __list_iterator(Node* node = nullptr)
        :_node(node)
    {}
    
    Ref operator*()
    {
        return _node->_data;
    }

    self& operator++()
    {
        _node = _node->_next;
        return *this;
    }

    self operator++(int)
    {
        self tmp(*this);
        _node = _node->_next;
        return tmp;
    }

    self& operator--()
    {
        _node = _node->_prev;
        return *this;
    }

    self operator--(int)
    {
        self tmp = *this;
        _node = _node->_prev;
        return tmp;
    }

    bool operator!=(const self& it)
    {
        return _node != it._node;
    }
    //i->_year
    Ptr operator->()
    {
        return &_node->_data;
    }
};
//自定义的节点指针类型
//为什么要自定义节点指针类型?
//因为节点指针需要支持++,*等操作,所以需要使用operator函数重载
```
#### list类
```c++
template<class T>
class list
{
public:
    typedef __list_node<T> Node;
    typedef __list_iterator<T, T&, T*> iterator;
    typedef __list_iterator<T, const T&, const T*> const_iterator;
    list()
    {
        _head = new Node;
        _head->_prev = _head;
        _head->_next = _head;
    }

    void push_back(const T& x)
    {
        Node* tail = _head->_prev;
        Node* newnode = new Node;
        tail->_next = newnode;
        newnode->_prev = tail;
        newnode->_next = _head;
        _head->_prev = newnode;
        newnode->_data = x;
    }

    ~list()
    {
        clear();
        delete _head;
        _head = nullptr;
    }

    void clear()
    {
        iterator it = begin();
        while (it != end())
        {
            it = erase(it);
        }
    }

    iterator erase(iterator& it)
    {
        Node* pos = it._node;
        Node* prev = pos->_prev;
        Node* next = pos->_next;
        delete pos;
        //	pre nex
        prev->_next = next;
        next->_prev = prev;
        return iterator(next);
    }

    const_iterator cbegin()
    {
        return const_iterator(_head->_next);
    }

    const_iterator cend()
    {
        return const_iterator(_head);
    }

    iterator begin()
    {
        return iterator(_head->_next);
    }
    iterator end()
    {
        return iterator(_head);
    }
private:
    Node* _head;
};

struct Date
{
    size_t _year = 1;
    size_t _month = 1;
    size_t _day = 1;
};
```
#### iterator it和Node* cur
    当他们都指向同一个节点,那么在物理内存中他们都存的是这个节点地址,物理上都是一样的
    但是他们的类型不一样,他们的意义就不一样
    比如*cur是一个指针的解引用,取得值是节点
    比如*it是去调用迭代器的operator*, 返回值是节点的值
    类型决定了对空间的解释权
### vector和list的区别
#### vector是一个可动态增长的数组
##### 优点:
    随机访问operator[] -> 很好的支持排序,二分查找,堆算法等等
##### 缺点:
    头部或中间的插入删除效率低 + 空间不够了以后增容代价很大
#### list是一个带头双向循环的链表
##### 优点:
    任意位置插入删除数据效率很高, O(1)
##### 缺点:
    不支持随机访问
#### 总结
    vector和list是两个相辅相成,互补的容器
