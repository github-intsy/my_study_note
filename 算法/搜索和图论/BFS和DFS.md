## 区别
|搜索方式|数据结构|所需空间|性质(所有权重都是1)|
|---|---|---|---|
|DFS|stack|O(logN)|不具有最短性|
|BFS|queue|O(2^h)|具有最短路性|

### 不带剪枝问题
```c++
#include<iostream>
using namespace std;
const int N = 10;
//计算u->n所经历的路径pat
int pat[N], n;
bool st[N];
void dfs(int u)
{
	if (u == n)//结束条件判断
	{
		for (int i = 0; i < n; ++i) printf("%d ", pat[i]);
		puts("");
		return;
	}
	for (int i = 1; i <= n; ++i)//枚举符合条件的数1~n
	{
		if (!st[i])//符合条件,没被使用
		{
			st[i] = true;//使用
			pat[u] = i;//填充
			dfs(u + 1);//递归
			pat[u] = 0;
			st[i] = false;//回溯
		}
	}
}
int main()
{
	cin >> n;
	dfs(0);//每次递归,都会在pat的每一位上填充数据
	return 0;
}
```
### 带剪枝问题
当前坐标为$x,y$, 想要计算$x,y$的两条斜线, 根据平面直角坐标系计算, $y-x$和$y+x$, 由于坐标需要正数, 所以就给$y-x$以+n的偏移量
最后为$y-x+n$和$y+x$

```c++
#include<iostream>
using namespace std;
const int N = 20;
char g[N][N];
int n;
bool col[N], dg[N], udg[N];
int ans;
void dfs(int u)
{
	if (u == n)
	{
		for (int i = 0; i < n; ++i) puts(g[i]);
		puts("");
		return;
	}
	for (int i = 1; i <= n; ++i)
	{
		if (!col[i] && !dg[i+u] && !udg[n+u-i])
		{
			col[i] = dg[u + i] = udg[n + u - i] = true;
			g[u][i] = 'Q';
			dfs(u + 1);
			col[i] = dg[u + i] = udg[n + u - i] = false;
			g[u][i] = '.';
		}
	}
}
int main()
{
	cin >> n;
	for (int i = 0; i < n; ++i)
		for (int j = 0; j < n; ++j)
			g[i][j] = '.';
	dfs(0);
	return 0;
}
```

---
# BFS
使用队列存储当前节点, 将头结点出队列, 使符合条件的节点根据头结点入队列
```c++
int bfs()
{
	queue<PII> q;
	q.push({ 0,0 });
	memset(d, -1, sizeof d);
	d[0][0] = 0;
	int dx[4] = { 0,0,-1,1 }, dy[4] = { 1,-1,0,0 };
	while (!q.empty())
	{
		auto t = q.front();
		q.pop();
		for (int i = 0; i < 4; ++i)
		{
			int x = t.first + dx[i], y = t.second + dy[i];
			if (x >= 0 && x < n && y >= 0 && y < m && g[x][y] == 0 && d[x][y] == -1)
			{
				d[x][y] = d[t.first][t.second] + 1;
				q.push({ x,y });
			}
		}
	}
	return d[n - 1][m - 1];
}
```

