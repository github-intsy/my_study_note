
## 概念
有负权边的最短路不一定存在\
只有`1~n`的路径中存在负环,才使得最短路不存在

`1~n中,最多经过不超过k条路径`的最短路, 只能由bellmen-ford算法来完成

bellmen-ford算法需要一个备份数组, 因为在迭代更新数据内容时可能出现串联的现象. 如何不发生串联, 迭代时使用上一次迭代的结果
```c++
int bellmen_ford()
{
	memset(dist, 0x3f, sizeof dist);//初始化数组
	dist[1] = 0;
	for (int i = 0; i < k; ++i)//最多走k次
	{
		memcpy(backup, dist, sizeof dist);//存储上一次迭代的结果
		for (int j = 0; j < m; ++j)//遍历所有的点
		{
			//获取上一次的结果和这次结果的最小值,防止出现串联
			int a = edges[j].a, b = edges[j].b, w = edges[j].w;
			dist[b] = min(dist[b], backup[a] + w);
		}
	}
	if (dist[n] > 0x3f3f3f3f / 2) return -1;//有可能出现负权边造成大小不匹配
	return dist[n];
}
```