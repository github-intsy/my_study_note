```c++
int a = 0;//全局
static int b = 10;//全局
int main()
{
    static int c = 1;//局部
    return 0;
}
```
##### 作用域
    上述代码中,a和b的作用域是全局的,在main函数之前就会初始化,在哪都能使用
    c的作用域是在main函数中,出了main函数就销毁了
#####
    值得注意的是,a可以在所有文件中调用
    而b只能在当前文件被调用
    ->全局变量和static变量的区别