### STL重点容器
- vector/list
- map/set
- unordered_map/unordered_set
#### 了解容器
- deque
- array
- forword_list
- bit_set
#### 迭代器重点
- 使用角度分析(重点)
  - 正向/反向 + const组合

- 功能的角度分析(了解)
  - 单向/双向/随机迭代器

- 单向迭代器
  - forword_list、unordered_map\unordered_set
  - 支持++
- 双向迭代器
  - list、map/set
  - 支持++、--
- 随机迭代器
  - string、deque、array、vector
  - 支持++、--、+、-

---
### 算法
#### 排列组合算法
- next_permutation()
  - 要求要处理的数据是有序的
  - 要求数据是升序
- prev_permutation()
  - 要求有序
  - 要求降序
  - 没有排完返回true
  - 排完返回false

```c++
int main()
{
    vector<int> v{1,2,3,4,5};
    do
    {
        for(auto e : v)
        {
            cout << e << " ";
        }
        cout << endl;
    } while(next_pormutation(v.begin(),v.end()));
    return 0;
}

```
---
### 适配器
stack和queue默认是对deque的封装转换而来,也可以是vector和list去适配\
priority_queue是对vector的适配,不过vector上面使用堆算法
### 仿函数
仿函数的本质就是一个类,并且重装了operator(),它的对象可以像函数一样使用
- priority_queue可以使用仿函数控制优先级
- map/set可以使用仿函数控制key的比较方式
- unordered_map/unordered_set可以使用仿函数控制key转成整形,去取模映射
- sort可以使用仿函数控制数据的比较大小的方式,控制升序还是降序
- 仿函数能做一些事,函数指针也可以,lamber表达式也可以(lamber表达式底层实现原理其实就是仿函数)

### 内存池
STL的空间配置器分为一级空间配置器和二级空间配置器

        申请内存x byte内存: 看x对应映射位置的哈希桶中有没有内存对象，如果有，就直接取走，
        如果没有就去内存池中切出一块20个对象内存,也就是x*20大小的内存，返回一个，剩下19个挂到这个位置下面
        下一次你再申请x byte大小内存，直接再这个位置下去取就可以。

        释放x byte内存对象,找到x对应映射的位置,将内存对象挂到对应位置下面就可以

        如果申请的不是8的整数倍,就向8对齐分配数据

- 一级空间配置器
  - 就是malloc和free的封装,并且处理失败抛异常机制
- 二级空间适配器
  - 二级空间配置器专门处理小于128字节的小块内存.
  - 如何才能提升小块内存的申请和释放的方式呢?
  - SGI-STL采用了内存池的技术来提高空间的速度以及减少额外的空间浪费,采用哈希桶的方式来提高用户获取空间的速度与高效管理
```c++
void* allocate(size_t n)
{
    if(n > 128)
    {
        //找一级空间配置器
    }
    else
    {
        //找二级空间配置器
    }
}
//主要解决: 效率问题,顺带解决碎片问题,自身也有内碎片
```
- 空间适配器申请的这些内存什么时候释放?
  - 进程结束的时候释放
- 一个进程中有一个空间配置器,进程中所有的容器需要内存,都找的是这个空间适配器