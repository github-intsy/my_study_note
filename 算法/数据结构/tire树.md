### 概念
1. tire树根节点不存储有效数据,一般为0
2. tire存储多个字符串
3. 存储的字符串在末尾有而外节点标记
4. tire树不是二叉树,而是从一个节点分出26个字母之一存储

#### 模板
```c++
void insert(char str[])//插入
{
	int p = 0;
	for (int i = 0; str[i]; ++i)
	{
		int u = str[i] - 'a';
		if (!son[p][u]) son[p][u] = ++idx;
		p = son[p][u];
	}
	cnt[p]++;
}

int query(char str[])//查找
{
	int p = 0;
	for (int i = 0; str[i]; ++i)
	{
		int u = str[i] - 'a';
		if (!son[p][u]) return 0;
		p = son[p][u];
	}
	return cnt[p];
}
```
- son数组存储一个节点的子节点
- idx为指向当前节点的指针
- p代表层数,u代表存储的元素
- cnt在每个字符串最后的标志位