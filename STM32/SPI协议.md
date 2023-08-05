### W25Q64指令集
- Write Enable写使能
  - 只有打开写使能才能写入数据
    - 交换一个指定寄存器地址`0x06`
- Read Status Register-1读状态寄存器
  - 查看寄存器是否为"忙",如果"忙",就不能写入数据
  - 查看最低位是不是1
  - 1表示还在忙,0表示芯片不忙了
    - 2个发送地址,先发送`0x05`指定地址,然后`0xff`交换数据
    - 交换的数据判断是否为忙
- Page Program页编程
  - 写入数据到一页中
  - 一页的数据是256个字节大小
    - 1. 交换数据地址选择寄存器`0x02`
    - 2. 发送三个字节的数据拼接成24位的地址用来在页中选择写入地址
```c
void W25Q64_PageProgram(uint32_t Address, uint8_t *DataArray, uint16_t Count)
{
    uint16_t i;
	
	W25Q64_WriteEnable();
	
	MySPI_Start();
	MySPI_SwapByte(W25Q64_PAGE_PROGRAM);
	MySPI_SwapByte(Address >> 16);  //注意向右移动,即将高位移动到最后8位来接收最高位数据
	MySPI_SwapByte(Address >> 8);
	MySPI_SwapByte(Address);
    //在指定地址开始,写入多个字节
	for (i = 0; i < Count; i ++)
	{
		MySPI_SwapByte(DataArray[i]);
	}
	MySPI_Stop();
	
	W25Q64_WaitBusy();
}
```
- Sector Erase擦除指定区域
  - 4kb是扇区擦除
    - 发送指令`0x20`
    - 发送三个字节的地址
- Read Data读取数据
    - 交换发送指令`0x03`
    - 发送三个字节地址(指定读取地址)
    - 可以开始连续读写,可以一直读写,也可以跨页读写
      - 发送`0xff`置换有用的数据到函数中存储

---
**注意**: 写入操作前,必须进行写使能\
涉及写入操作的时序有:\
扇形擦除和页编程

注意的是,写使能后会自动关闭使能

写入操作后,芯片会进入忙状态\
在每次写入后,调用一下WaitBusy等待忙状态

建议选择事前等待\
事前等待芯片是否处于忙状态,可以提高程序执行效率\
如果写入操作后没有写入操作,程序可以执行其他操作来等待芯片忙状态

写入数据前必须擦除\
flash存储器只能将0变为1,不能将1变为0\
所以如果不写入擦除,就会造成数据错误

#### 页地址规律
- 页地址的范围是xxxx00到xxxxFF
- 最后两位是页内地址,前面四位是页地址
- 该存储器不能执行跨页写入

---
### 硬件SPI外设