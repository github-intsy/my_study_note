### map/set 和 unordered map/unordered_set 有什么区别和联系?
1. 他都可以可以实现key和key/value的搜索场景，并且功能和使用基本一样。
2. map/set的底层是使用红黑树实现的，遍历出来是有序的，增删查改的时间复杂度是0(logN)
3. unordered map/unordered set的底层是使用哈希表实现的，遍历出来是无序的，增删查改的时间复杂度是0(1)
4. map/set是双向迭代器 unordered_map和unorder_set是单向迭代器
    
    搜案树还取决于树的高度，也就是说数据量越大效率会逐步降低。而哈希通过映射关系直接进行查找，效率非常高，那么哈希最大的问题就是如何解决哈希冲突
    
        我们会发现: 插入和查找31效率都很低，
        所以可以分析出结论，影响哈希表效率的是哈希冲突哈希冲突越多，效率越低。

    哈希冲突: 不同的值映射到了相同的位置


    闭散列:也叫开放定址法，当发生哈希冲突时，如里哈希表未被装满，说明在哈希表中必然还有空位置，那么可以把key存放到冲突位置中的"下一个”空位置中去。

    下一个空位置可以是下一个,也可以是下一个的2次方
#### 开放定制法:
1. 线性探测，依次往后去找一个空位置:
2. 二次探测，按2次方往后找空位置