### 求字符串的子串下标
1. 暴力求解法,遍历原数组,与子串首元素相同就同步向后查找,时间复杂度`O(M*N)`
2. kmp算法

### KMP算法
- 子串带有next数组
- 遍历原数组,原数组指针为`i`,子串指针为`j`
- 查找到首元素就一起向后查找
- 如果发现不同元素,就根据next数组记录的数字操作
- 如果`next`**当前元素为2**, 则`i`不变,`j`指向的子串跳过开头两个元素开始和下一个`i`指向元素比较
- 只要比较元素不同,就读取next数组,变化`j`指向位置,其中`i`不变

```c++
int main()
{
	string a, b; //a长数组,b短
	vector<int> next;//下标数组
	cin >> a >> b;
	next.resize(b.size());
	for (int i = 1, j = 0; i < b.size(); ++i)	//设置next数组
	{
		while (j && b[i] != b[j]) j = next[j - 1];
		if (b[i] == b[j]) next[i] = ++j;
	}

	for (int i = 0, j = 0; i < a.size(); ++i)		//遍历原字符串
	{
		while (j && a[i] != b[j]) j = next[j - 1];
		if (a[i] == b[j]) ++j;
		if (j == b.size())					//符合条件
		{
			cout << i - j + 1 << endl;
			return 0;
		}//子串下标
	}
	cout << -1 << endl;	//不符合
	return 0;
}
```