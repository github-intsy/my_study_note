### --k
```c++
class Solution {
public:
    int findKthLargest(vector<int>& nums, int k) {
        priority_queue<int> pq;
        for(int i = 0; i < nums.size(); ++i)
        {
            pq.push(nums[i]);
        }
        while(--k)
        {
            pq.pop();
        }
        return pq.top();
    }
};
```
### k--
```c++
class Solution {
public:
    int findKthLargest(vector<int>& nums, int k) {
        priority_queue<int> pq;
        for(int i = 0; i < nums.size(); ++i)
        {
            pq.push(nums[i]);
        }
        int i = k - 1;
        while(i--)
        {
            pq.pop();
        }
        return pq.top();
    }
};
```
    循环中--k是循环k-1次,k--循环k次