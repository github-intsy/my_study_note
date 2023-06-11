    set的中序遍历的有序的，因为它的底层是一个红黑树(平衡搜索树)
    set中不能存储相同的两个数据
    set本身所带的find函数时间复杂度是logN
    算法库里面的find时间复杂度是O(N)
    因为set的find是按照它本身的特性去查找的,算法库的find则是挨个查找

    map中要储存两个值，用迭代器遍历的时候不知道指向哪个值，所以就用pair来储存
    map中的insert插入元素有返回值
    为pair<iterator，bool>
    如果插入成功返回该元素的iterator，true      insert的返回值为pair<iterator，bool>
    如果存在key，则返回iterator和false
    map中的operator的底层使用insert实现的
    map中储存数据是用pair的自定义类型
    pair里面可以储存两个数据,因为c++规定只能返回一个数据，所以就把两个数据打包到pair里面一起储存和调用
### set
- set特点: 快
- set的增删查都是O(logN)
- set内的数据不允许修改
#### pair
```c++
template<class T1, class T2>
pair<T1, T2> make_pair(T1 x, T2 y)
{
    return (pair<T1, T2>(x1, x2));
}
```
    pair构造函数,构造一个匿名对象
    函数模版构造一个pair对象
    
    序列式容器: vector/list/string/deque
    关联式容器: map/set/unordered map/unordered set
#### map使用
```c++
string strs[] = {"西瓜","樱桃","西瓜","苹果","西瓜","西瓜","西瓜","苹果"};
map<string, int> countMap;
for(auto& str : strs)   //使用pair对象
{
    pair<map<string, int>::iterator, bool> ret = countMap.insert(mack_pair(str, 1));
    if(ret.second == false)
    {
        ret.first->second++;
    }
}
mappde_type& operator[] (const key_type& k)
{
    return (*((this->insert(make_pair(k,mapped_type()))).first)).second;
    //(this->insert(make_pair(k,mapped_type())))                pair<iterator, bool>
    //((this->insert(make_pair(k,mapped_type()))).first)        iterator
    //*((this->insert(make_pair(k,mapped_type()))).first))      pair<key_type, mapped_type>
    //(*((this->insert(make_pair(k,mapped_type()))).first)).second      mappd_type

    //为什么这里不用find来实现呢?
    //原因: 假设用find, 如果map中没有这个k, 如何返回

    //这里用insert
    //1. 如果k不在map中,则插入pair<k, mapped_type()>, 再返回映射对象的引用
    //2. 如果k在map中, 则插入失败, 返回k所在节点中映射对象的引用
}
for(auto& str : strs)   //使用operator[]
{
    countMap[str]++;
    countMap["香蕉"];       //插入
    countMap["香蕉"] = 1;   //修改
    cout << countMap["香蕉"] << endl;   //查找
    countMap["哈密瓜"] = 5;     //插入+修改
}
for(auto& e: countMap)
{
    cout << e.first << ":" << e.second << endl;
}
```
#### map的operator[]三层作用
1. 插入
2. 查找k对应的映射对象
3. 修改k对应的映射对象

一般使用operator[]去
1. 插入+修改
2. 修改

一般不会用他去做查找, 因为如果key不在会插入数据
### map
1. 增   insert + operator[]
2. 删   erase
3. 查   find
4. 改   operator[]
5. 遍历 iterator + 范围for

要注意的是map中存的是pair<k,v>键值对

遍历出来的数据是按k排序的, 因为底层是搜索树, 遍历走的是树的中序
### multiset
    位于set的库中, 跟set的区别就是multiset允许键值冗余
`multimap<int, string, greater<int>> countSet;`
    multiset排序的底层是less,从小开始排序,使用greater会将数据由大到小排序输出

### set
    set和map的底层使用了同一颗红黑树
```c++
template<class K>
class set
{
    struct SetkOfT
    {
        const K& operator()(const K& k)
        {
            return k;
        }
    };

public:
    bool Insert(const K& k)
    {
        return _rb.insert(k);
    }
    
    void show()
    {
        _rb.show();
    }

private:
    RBTree<K, K, SetkOfT> _rb;
};
```
### map
```c++
template<class K, class V>
class map
{
    struct MapKOfV
    {
        const K& operator()(const pair<K,V>& p)
        {
            return p.first;
        }
        
    };
public:

    bool Insert(const pair<K,V>& t)
    {
        return _rb.insert(t);
    }
    
    void show()
    {
        _rb.show();
    }

private:
    RBTree<K, pair<K, V>, MapKOfV> _rb;
};
```
### RBTree(set & map底层)
```c++
enum Color
{
	BLACK,
	RED
};

template<class T>
struct RBTreeNode
{
	RBTreeNode(const T& t)
		:_left(nullptr)
		, _right(nullptr)
		, _parent(nullptr)
		, _data(t)
		, _col(RED)
	{}
	RBTreeNode<T>* _left;
	RBTreeNode<T>* _right;
	RBTreeNode<T>* _parent;
	T _data;
	Color _col;
	
};

template<class K, class T, class KofT>
class RBTree
{
	typedef RBTreeNode<T> Node;
public:
	RBTree(Node* n = nullptr)
		:_root(n)
	{}

	bool insert(const T& t)
	{
		if (_root == nullptr)
		{
			_root = new Node(t);
			_root->_col = BLACK;
			return true;
		}

		Node* cur = _root;
		Node* parent = nullptr;
		KofT koft;
		while (cur)
		{
			if (koft(cur->_data) > koft(t))
			{
				parent = cur;
				cur = cur->_left;
			}
			else if (koft(cur->_data) < koft(t))
			{
				parent = cur;
				cur = cur->_right;
			}
			else
			{
				return false;
			}
		}
		//将新节点连接起来,默认是红色
		cur = new Node(t);
		if (koft(parent->_data) < koft(t))
		{
			parent->_right = cur;
			cur->_parent = parent;
		}
		else
		{
			parent->_left = cur;
			cur->_parent = parent;
		}
		
		//判断parent是grandfather的左节点还是右节点
		while (parent->_col == RED)
		{
			//未解决->如果树的深度为2->如何解决
			Node* grandfather = parent->_parent;
			if (!grandfather)
			{
				return true;
			}
			//如果是左节点
			if (parent == grandfather->_left)
			{
				Node* uncle = grandfather->_right;

				//三种情况
				//1. u存在且为红
				if (uncle && uncle->_col == RED)
				{
					parent->_col = uncle->_col = BLACK;
					grandfather->_col = RED;

					cur = grandfather;
					parent = cur->_parent;
				}
				//uncle不存在 or uncle存在且为黑
				else
				{
					//情况3: 双旋 -> 变为单旋
					if (cur == parent->_right)
					{
						RotateL(parent);
						swap(parent, cur);
					}

					//情况2: 有可能是情况3转变过来的
					RotateR(grandfather);
					grandfather->_col = RED;
					parent->_col = BLACK;
					
					break;
				}
			}
			else if(parent == grandfather->_right)
			{
				Node* uncle = grandfather->_left;
				//情况1: uncle存在且为红
				if (uncle && uncle->_col == RED)
				{
					parent->_col = uncle->_col = BLACK;
					grandfather->_col = RED;
					
					cur = grandfather;
					parent = cur->_parent;
				}
				//2. uncle不存在,或者为黑
				//情况2和情况3
				else
				{
					if (cur == parent->_left)
					{
						RotateR(parent);
						swap(cur, parent);
					}

					RotateL(grandfather);
					if (uncle)
					{
						uncle->_col = RED;
					}
					break;
				}
			}
		}
		_root->_col = BLACK;
		return true;
	}

	void RotateL(Node* parent)
	{
		Node* subR = parent->_right;
		Node* subRL = subR->_left;
		Node* prev = parent->_parent;

		parent->_right = subRL;
		subRL = parent;
		if (subRL)
			subRL->_parent = parent;
		subR->_left = parent;
		parent->_parent = subR;

		if (parent == _root)
		{
			subR->_parent = nullptr;
			_root = subR;
		}
		else
		{
			if (prev->_left == parent)
			{
				prev->_left = subR;
			}
			else
			{
				prev->_right = subR;
			}
			subR->_parent = prev;
		}
	}
	void RotateR(Node* parent)
	{
		Node* subL = parent->_left;
		Node* subLR = subL->_right;
		parent->_left = subLR;
		//两种情况需要单独处理
		//1.旋转节点为空
		//2.旋转后的subL有可能为根,有可能不为根
		if (subLR)
			subLR->_parent = parent;
		subL->_right = parent;
		Node* prev = parent->_parent;
		parent->_parent = subL;
		if (parent == _root)
		{
			subL->_parent = prev;
			_root = subL;
		}
		else
		{
			if (prev->_left == parent)
			{
				prev->_left = subL;
			}
			else
			{
				prev->_right = subL;
			}
			subL->_parent = prev;
		}
	}
	
	void _show(Node* root)
	{
		if (root == nullptr)
			return;
		static KofT koft;
		_show(root->_left);
		cout << koft(root->_data) << ' ';
		_show(root->_right);
	}

	void show()
	{
		_show(_root);
	}


private:
	Node* _root;
	
};
```
### map & set iterator
```c++
typedef RBTree<K，K，SetKeyOfT>::iterator iterator;
//编译器报错,因为重命名的是一个模版,并没有实例化出对象,而在调用该条语句的时候会查找对应的对象,所以就会报错

//要想解决,就在前面加上tempname
//tempname有个特殊的作用,就是告诉编译器这是一个类型的名称,现在不用找,在后面再去找
typedef template RBTree<K,K,SetKeyOfT>::iterator iterator;
```
    iterator的operator++,就是模拟二叉树的中序遍历
#### 遍历条件
1. 如果右不为空，中序的下一个就是右子树的最左节点
2. 如果右为空，表示_node所在的子树已经放完成,在一个节点在他的祖先中找
3. 如果右为空,就判断当前节点是父节点的左节点还是右节点,如果是右节点,就找到它是祖先左边的那个节点.然后再传递到右子树中找到右子树最左边的那个节点.