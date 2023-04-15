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