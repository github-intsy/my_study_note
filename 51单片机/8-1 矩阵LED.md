### 74HC595
- OE接口上方有一个横线,代表需要接入低电平才有效,接入低电平, 发送势能, 才能使74HC595生效
- SER输入一位数据
- SERCLK = 0复位; = 1上升沿移位, 将数据在寄存器中向下移动一位, 需要手动复位和手动移位
- RCLK = 1, 将数据传送到I/O口, 需要手动置零和赋1
### 矩阵LED
- P0控制LED纵列, 0亮,1灭
- 74HC595逐字节控制横列的LED, 1亮0灭
### 动态图像
使用外部工具获得对应的横列数据, 通过对建立数组的范围控制, 范围读取数据, 达到逐帧的动态图像效果