|(constructor)构造函数声明|接口说明|
|--|--|
|vector() (重点)|无参构造|
|vector (size_type n, const value_type& val = value_type())|构造并初始化n个val|
|vector (const vector& x) (重点)|拷贝构造|
|vector (inputlterator first, inputlterator last);|使用迭代器进行初始化构造|
    我们实际中不会直接定义const对象,因为没意义
    const对象基本都是在传参的过程中产生的
    而引用传参如果不改变对象,基本都会加const

### vector::insert
```c++
vector<int> v1;
push_back(1);
push_back(2);
push_back(3);
//运行结果:1,2,3
v1.insert(v1.bengin(),0);
//运行结果:0,1,2,3
v1.erase(v.bengin());
//运行结果:1,2,3
```
    c++头文件中有algorithm库,里面包含了一些算法
    其中有find()
```c++
vector<int>::iterator pos = find(v.begin(), v.end(), 5);
//find的使用实例
//其中的源码就是使用模版和迭代器查找指定的元素
```
### 只出现一次的数字
    给定一个非空整数数组,除了某个数字出现一次以外,每个数字都出现两次,找出那个只出现一次的数字

    思路: 使用按位异或求解
    按位异或: 相同为0. 相异为1
```c++
clas Solution{
public:
    int singleNumber(vector<int>& nums)
    {
        int val = 0;
        for(auto e : nums)
        {
            val ^= e;
        }
        return val;
    }
};
```
#### 进阶
    数字都出现三次.只有一个出现一次.求出现一次的那个数字
    思路: 统计出32个位, 每个位统计1出现的次数
    每个位1的个数要么是3n, 要么是3n+1
    3n+1的个位就是只出现一次的位
```c++
clas Solution{
public:
    int singleNumber(vector<int>& nums)
    {
        int bitArray[32] = {0};
        for(auto e : nums)
        {
            for(size_t i = 0; i < 32; ++i)
            {
                if(e & (1 << i))
                {
                    ++bitArray[i];
                }
            }
        }
    }
};
```
### 模拟实现vector类
#### reserve(const size_t& n)
```c++
void reserve(const size_t& n)
{
    if (n > capacity())
    {
        T* tmp = new T[n];
        if (_start)
        {
            memcpy(tmp, _start, sizeof(T) * size());
            delete[] _start;
        }
        _end_of_storage = tmp + capacity();
        _finish = tmp + size();
        _start = tmp;
        //最后三行的顺序不可以改变,
        //因为size和capapcity都是通过同一组元素的地址相减得到的数据大小
        //如果改变,则指针位置不同,不可得到正确值,会乱码
    }
}
```