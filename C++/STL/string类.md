1. string类对象的常见构造

| (constructor)函数名称| 功能说明 |
| --- |---|
|string() (重点) |构造空的string类对象,即空字符串|
|string(const char* s) (重点)| 用C-string来构造string类对象|
|string(size_t n, char c) | string类对象中包含n个字符c|
|string(const string& s)| 拷贝构造函数|
```c++
void Teststring()
{
    string s1;                 //构造空的string类对象s1
    string s2("hello world");  //用C格式字符串构造string类对象s2
    string s3(s2);             //拷贝构造s3
}
```
```c++
推荐使用+=
int main()
{
    string s("123456");
    s.push_back('7');       //push_back只能在末尾加一个字符
    s.append("89");         //append只能在末尾加一串字符串
    s += '1';               //+=不仅可以在末尾加字符,而且可以加字符串
    s += "00000";
    cout << s << endl;      //123456789100000
    return 0;
}
```
2. string类对象的容量操作

|函数名称|功能说明|
|---|---|
|size (重点)|返回字符串有效字符长度|
|length|返回字符串有效字符长度|
|capacity|返回空间总大小|
|empty (重点)|检测字符串释放为空集,是则返回true,否则返回false|
|clear (重点)|清空有效字符|
--------------------------------------------------------
3. 迭代器
#### 
    迭代器（iterator）是一种可以遍历容器元素的数据类型。
    迭代器是一个变量，相当于容器和操纵容器的算法之间的中介。
    C++更趋向于使用迭代器而不是数组下标操作，
    因为标准库为每一种标准容器（如vector、map和list等）定义了一种迭代器类型，而只有少数容器（如vector）支持数组下标操作访问容器元素。
    可以通过迭代器指向你想访问容器的元素地址，通过*it打印出元素值。
    这和我们所熟知的指针极其类似。
    容器都有成员begin和end，其中begin成员复制返回指向第一个元素的迭代器（用*迭代器打印出元素值），
    而end成员返回指向容器尾元素的下一个位置的迭代器，它是一个不存在的元素位置
|迭代器名称|功能说明|
|--|--|
|string::iteraotor|正向迭代器,正序读写数据|
|string::const_iterator|常量正向迭代器,正序只读数据|
|string::reverse_iterator|反向迭代器,逆序读写数据|
|string::const_reverse_iterator|常量反向迭代器|
```c++
#include<iostream>
#include<string>
int main()
{
    string s1("hello world");
    string::iterator it = s1.begin();//迭代器
    while(it != s1.end())
    {
        cout << *it " ";
        ++it;
    }
    cout << endl;
}
//s1.begin()获得字符串开始的位置,用迭代器创建一个it,类似于指针指向每个元素
//迭代器遍历完字符串后的位置不会变,会指向最后一个元素的后一个元素
//s1.end()是最后一个字符的下一个位置
//迭代器不仅可以读数据,也可以写
```
#### c_str
    返回值:const char*
    获取字符串首地址,用c字符串的形式遍历
```c++
const char* str = s1.c_str();
while(*str)
{
    cout << *str << " ";
    ++str;
}
cout <<　endl;      //调用的string重载的operator<<      将对象中的所有字符都输出
cout << s1.c_str() << endl;//直接输出const char*        遇到\0就结束
```
#### find()
    从某个位置开始查找指定的字符
    查找失败返回string::npos
    成功找到就返回所求字符的所在位置的下标
```c++
string s1(string.txt);
size_t pos1 = s1.find('.');
if(pos1 != string::npos)
{
    cout << s1.substr(pos1) << endl;
}
npos是string里面的静态成员变量
如果substr只有一个传参,则默认从0开始查找
如果有两个参数,如: substr(2,4); 则从下标2查到下标4
```
### std::getline(string)
    可以输入带空格的字符串
```c++
1. istream& getline (istream& is, string& str, char delim);
2. istream& getline(istream& is, string& str);
```
```c++
示范
string s;
getline(cin,s);
```
#### string类的构造函数
    无参的构造函数一般都是默认传"",也就是没有有效字符,只有一个'\0'
    不可以给缺省值为nullptr

#### 深浅拷贝
    当我们没有编写拷贝构造函数时,编译器会给出默认构造函数,我们称这种拷贝为浅拷贝
    浅拷贝在拷贝对象时,会一个字节一个字节的单独拷贝
    这种拷贝在某种情况下可行,在需要拷贝指针的情况下时不行
    ->拷贝出两个指针指向同一个块空间,使得同一块空间被释放两次

####
    string类中存在capacity和size
    其中size->已经有多少个有效字符
    capacity->能存多少有效字符
    \0不是有效字符

#### 模拟实现一个string类
    string类想要支持增删查改,就需要在堆区储存数据.
    所以会将输入到栈区的数据拷贝到堆区,处理数据
    堆区扩容时不能2倍去增,因为不一定够
    使用in.get()可以在控制台获得输入的字符串之间的空格
    ch = in.get();

    ::swap();
    域作用限定符, 左边为空, 表示调用全局的
    在类的成员对象和成员方法前面都会默认加上this
#### append()注意地方
```c++
void append(const char* str)
{
    size_t len = strlen(str);
    if (len + _size >= _capacity)
    {
        char* newstr = new char[len + _size + 1];
        strcpy(newstr, _str);
        delete[] _str;
        _str = newstr;
        _capacity = len + _size;
    }
    strcpy(_str + _size, str);
    _size += len;
}
以上代码报错,为什么?
strcpy是不安全的, 它在拷贝字符串的时候会把字符串的地址一起拷过去,
如果是new出来的空间,就会释放两次,导致程序崩溃

下面的push_back()也会报错
void push_back(const char& c)
{
    if (_size == _capacity)
    {
        size_t newcapacity = _capacity == 0 ? 4 : _capacity * 2;
        char* tmp = new char[newcapacity + 1];
        strcpy(tmp, _str);
        delete[] _str;
        _str = tmp;
        _capacity = newcapacity;
    }
    _str[_size++] = c;
}
```
```c++
改进后的代码
void append(const char* str)
{
    size_t len = strlen(str);
    if (len + _size >= _capacity)
    {
        char* newstr = new char[len + _size + 1];
        for (int i = 0; i <= _size; ++i)
        {
            newstr[i] = _str[i];
        }
        //不拷贝地址,直接拷贝值
        delete[] _str;
        _str = newstr;
        _capacity = len + _size;
    }
    strcpy(_str + _size, str);
    _size += len;
}
```
### 模拟实现string类代码
#### push_back()
```c++
void push_back(const char& c)
{
    if (_size == _capacity)
    {
        size_t newcapacity = _capacity == 0 ? 4 : _capacity * 2;
        reserve(newcapacity);
    }
    _str[_size++] = c;
    _str[_size] = '\0';
}
```
#### reserve()
```c++
void reserve(size_t n)
{//开辟指定大小的空间
    if (n > _capacity)
    {
        char* newstr = new char[n + 1];
        for (int i = 0; i <= _size; ++i)
        {
            newstr[i] = _str[i];
        }
        delete[] _str;
        _str = newstr;
        _capacity = n;
    }
}
```
#### append()
```c++
void append(const char* str)
{
    size_t len = strlen(str);
    if (len + _size >= _capacity)
    {
        reserve(len + _size);
    }
    strcpy(_str + _size, str);
    _size += len;
}
```
#### 构造,析构函数
```c++
//构造函数
string(const char* str = "")
    :_str(new char[strlen(str) + 1])
{
    strcpy(_str, str);
    _capacity = _size = strlen(str);
}

//析构函数
~string()
{
    delete[] _str;
    _str = nullptr;
    _capacity = _size = 0;
}
```
#### size()
```c++
size_t size()
{
    return _size;
}
```
#### c_str()
```c++
const char* c_str() const
{
    return _str;
}
```
#### operator[]
```c++
char& operator[](size_t i)
{
    return _str[i];
}
```
#### 迭代器
```c++
typedef char* iterator;
iterator begin()
{
    return _str;
}

iterator end()
{
    return _str + _size;
}
```
#### insert(size_t pos, const char& ch)
```c++
void insert(size_t pos, const char& ch)
{
    assert(pos <= _size);
    if (_capacity == _size)
    {
        size_t newcapacity = _capacity == 0 ? 4 : _capacity * 2;
        reserve(newcapacity);
    }
    size_t end = _size + 1;
    while (end > pos)//如果条件变为end>=pos,则多移动的数据会在下面被ch覆盖
    {
        _str[end] = _str[end - 1];
        --end;
    }
    
    _str[pos] = ch;
    ++_size;
}
```
#### insert(size_t pos, const char* str)
```c++
void insert(size_t pos, const char* str)
{
    size_t len = strlen(str);
    if (_size + len > _capacity)
    {
        reserve(len + _size);
    }
    size_t end = _size + len;
    //必须>=, 不然最后一个数据不能移动,会被覆盖
    while (end >= len + pos)
    {
        _str[end] = _str[end - len];
        --end;
    }
    size_t i = pos;
    size_t j = 0;
    //不能<=因为是以下标遍历的,不然就会多一个\0
    while (j < len)
    {
        _str[i++] = str[j++];
    }
    _size += len;
}
```
#### erase()
```c++
void erase(size_t pos, size_t len)//删除指定位置的元素
{
    assert(_size);
    //在pos位置删除长度为len的字符串
    //从前往后,依次向前挪动len个字符
    // pos    pos+len
    size_t end = pos + len;
    size_t begin = pos;
    while (end < _size)
    {
        _str[begin++] = _str[end++];
    }
    _size -= len;
}
```
#### operator重载比大小
```c++
bool operator<(const string& s)
{
    if (strcmp(_str, s._str) < 0)
        return true;
    else
        return false;
}

bool operator==(const string& s)
{
    if (strcmp(_str, s._str) == 0)
        return true;
    else
        return false;
}

bool operator<=(const string& s)
{
    return operator<(s) || operator==(s);
}

bool operator>(const string& s)
{
    return !operator<=(s);
}

bool operator>=(const string& s)
{
    return !operator<(s);
}

bool operator!=(const string& s)
{
    return !operator==(s);
}
```
#### 拷贝构造
```c++
传统写法
string(const string& s)
    :_str(new char[strlen(s._str) + 1])
{
    strcpy(_str, s._str);
}
```
```c++
现代写法
string(const string& s)
{
    string tmp(s._str);
    swap(tmp);
}
void swap(string& tmp)
{
    ::swap(_str, tmp._str);
    ::swap(_capacity, tmp._capacity);
    ::swap(_size, tmp._size);
}
::表示引用域外的swap函数
```
