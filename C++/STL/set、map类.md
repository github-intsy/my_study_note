    set的中序遍历的有序的，因为它的底层是一个红黑树(平衡搜索树)
    set中不能存储相同的两个数据
    set本身所带的find函数时间复杂度是logN
    算法库里面的find时间复杂度是O(N)
    因为set的find是按照它本身的特性去查找的,算法库的find则是挨个查找

    map中要储存两个值，用迭代器遍历的时候不知道指向哪个值，所以就用pair来储存
    map中的insert插入元素有返回值
    为pair<iterator，bool>
    如果插入成功返回该元素的iterator，true      insert的返回值为pair<iterator，bool>
    如果存在key，则返回iterator和false
    map中的operator的底层使用insert实现的
    map中储存数据是用pair的自定义类型
    pair里面可以储存两个数据,因为c++规定只能返回一个数据，所以就把两个数据打包到pair里面一起储存和调用
### set
- set特点: 快
- set的增删查都是O(logN)
- set内的数据不允许修改
#### pair
```c++
template<class T1, class T2>
pair<T1, T2> make_pair(T1 x, T2 y)
{
    return (pair<T1, T2>(x1, x2));
}
```
    pair构造函数,构造一个匿名对象
    函数模版构造一个pair对象
    
    序列式容器: vector/list/string/deque
    关联式容器: map/set/unordered map/unordered set
#### map使用
```c++
string strs[] = {"西瓜","樱桃","西瓜","苹果","西瓜","西瓜","西瓜","苹果"};
map<string, int> countMap;
for(auto& str : strs)   //使用pair对象
{
    pair<map<string, int>::iterator, bool> ret = countMap.insert(mack_pair(str, 1));
    if(ret.second == false)
    {
        ret.first->second++;
    }
}
mappde_type& operator[] (const key_type& k)
{
    return (*((this->insert(make_pair(k,mapped_type()))).first)).second;
    //(this->insert(make_pair(k,mapped_type())))                pair<iterator, bool>
    //((this->insert(make_pair(k,mapped_type()))).first)        iterator
    //*((this->insert(make_pair(k,mapped_type()))).first))      pair<key_type, mapped_type>
    //(*((this->insert(make_pair(k,mapped_type()))).first)).second      mappd_type

    //为什么这里不用find来实现呢?
    //原因: 假设用find, 如果map中没有这个k, 如何返回

    //这里用insert
    //1. 如果k不在map中,则插入pair<k, mapped_type()>, 再返回映射对象的引用
    //2. 如果k在map中, 则插入失败, 返回k所在节点中映射对象的引用
}
for(auto& str : strs)   //使用operator[]
{
    countMap[str]++;
    countMap["香蕉"];       //插入
    countMap["香蕉"] = 1;   //修改
    cout << countMap["香蕉"] << endl;   //查找
    countMap["哈密瓜"] = 5;     //插入+修改
}
for(auto& e: countMap)
{
    cout << e.first << ":" << e.second << endl;
}
```
#### map的operator[]三层作用
1. 插入
2. 查找k对应的映射对象
3. 修改k对应的映射对象

一般使用operator[]去
1. 插入+修改
2. 修改

一般不会用他去做查找, 因为如果key不在会插入数据
### map
1. 增   insert + operator[]
2. 删   erase
3. 查   find
4. 改   operator[]
5. 遍历 iterator + 范围for

要注意的是map中存的是pair<k,v>键值对

遍历出来的数据是按k排序的, 因为底层是搜索树, 遍历走的是树的中序
### multiset
    位于set的库中, 跟set的区别就是multiset允许键值冗余
`multimap<int, string, greater<int>> countSet;`
    multiset排序的底层是less,从小开始排序,使用greater会将数据由大到小排序输出