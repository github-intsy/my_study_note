### 远程库操作

|命令名称|作用|
|---|---|
|git remote -v|查看当前远程地址别名|
|git remote add 别名 远程地址|起别名|
|git push 别名 分支|推送本地分支上的内容到远程仓库|
|git clone 远程地址|将远程仓库的内容克隆到本地|
|git pull 远程库地址别名 远程分支名|将远程仓库对于分支最新内容拉取下来后与当前分支直接合并|
    git的拉取远程库动作会自动帮你提交到本地库
    克隆远程库的代码是不需要登录账号的
    因为被克隆的远程库的公共的