## 朴素dijkstra
1. `dist[1] = 0, dist[i] = INT_MAX`只有起点的距离确定为0, 其余点为无穷大
2. `s数组`当前已经确定最短距离的点\
   循环`1~n`的点, `t = 找到不在s中的,距离最近的点`\
   `s = t`\
   用`t`更新其他点的距离

简单来说
1. 初始化距离
2. 循环所有点, 更新最短路


如果存在自环(所有边为正数), 则不会出现在最短路中\
如果有重边, 就更新为最短的即可
## 变量含义
|变量|含义|
|---|---|
|dist[N]|表示从1号点走到每个点的当前最短距离是多少|
|st[N]|表示每个点的最短路是不是已经确定了|

```c++
int dijkstra()
{
	memset(dist, 0x3f, sizeof dist);
	dist[1] = 0;//添加第一个点
	for (int i = 0; i < n; ++i)
	{
		int t = -1;
		//查找当前点连通的最短路
		for (int j = 1; j <= n; ++j)
			if (!st[j] && (t == -1 || dist[t] > dist[j]))
				t = j;

		st[t] = true;//存储当前最短路

		//更新是原点到j的距离短, 还是原点到t再到j的距离短
		for (int j = 1; j <= n; ++j)
			dist[j] = min(dist[j], dist[t] + g[t][j]);
	}
	if (dist[n] == 0x3f3f3f3f) return -1;
	return dist[n];
}
```

---
## 堆优化版dijkstra
|变量|作用|
|---|---|
|pair<int,int>|first距离, second编号|
|w[i]|记录邻接矩阵权重(距离)|

```c++
int dijkstra()
{
	memset(dist, 0x3f, sizeof dist);
	dist[1] = 0;//初始化距离

	priority_queue<PII, deque<PII>, greater<PII>> heap;//建堆
	heap.push({ 0,1 });//距离,编号. 将第一个点设置为起点
	while (heap.size())
	{
		auto t = heap.top();//获得堆顶元素
		heap.pop();
		int ver = t.second, distance = t.first;
		if(st[ver]) continue;//防止出现重复元素
		st[ver] = true;
		for (int i = h[ver]; i != -1; i = ne[i])//顺着路径走
		{
			int j = e[i];
			if (dist[j] > distance + w[i])//更新当前较小的点
			{
				dist[j] = distance + w[i];
				heap.push({ dist[j], j });
			}
		}
	}
	if (dist[n] == 0x3f3f3f3f) return -1;
	return dist[n];
}
```