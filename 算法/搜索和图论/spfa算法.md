## 概念
用queue存储节点, 每次取出queue中的值, 迭代值的所有出边, 如果当前边变小, 则该边之后的所有值都有可能变小, 所以就将其加入队列中迭代

```c++
int spfa()
{
	memset(dist, 0x3f, sizeof dist);
	dist[1] = 0;
	st[1] = true;
	queue<int> q;
	q.push(1);//初始化队列, 将第一个元素加入队列
	while (q.size())
	{
		int t = q.front();
		q.pop();//宽搜思路, 取出所有元素
		st[t] = false;//取出后取消锁定
		for (int i = h[t]; i != -1; i = ne[i])
		{
			int j = e[i];//迭代
			if (dist[j] > dist[t] + w[i])
			{
				dist[j] = dist[t] + w[i];//变小
				if (!st[j])
				{
					st[j] = true;//锁定并加入队列
					q.push(j);
				}
			}
		}
	}
	if (dist[n] == 0x3f3f3f3f) return -1;
	return dist[n];
}
```