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
#### 步骤(SPI_Init)
1. 开启时钟,开启SPI和GPIO的时钟
2. 初始化GPIO口
   1. SCK、MOSI是由硬件外设控制的输出信号
   2. 所以配置为复用推完输出
   3. MISO是硬件外设的输入信号
   4. 配置为上拉输入
   5. 因为输入设备可以有多个
   6. 所以不存在复用输入
   7. 普通GPIO口可以输入,外设也可以输入
   8. SS是软件配置输出信号,所以配置为通用推挽输出
3. 配置SPI外设
4. 开关控制SPI_Cmd使能

#### 主要函数
|函数名|作用|
|---|---|
|SPI_I2S_SendData|写DR数据寄存器|
|SPI_I2S_ReceiveData|写DR数据寄存器|

#### 初始化函数细节
1. 开启SPI1时钟
   1. SPI1是APB2的外设
2. SS从机选择引脚是PA4,使用软件模拟
   1. 选择通用推挽输出
3. 配置SCK和MOSI,外设控制的输出
   1. 配置为复用推挽输出
   2. SCK-PA5
   3. MOSI-PA7
4. 配置MISO
   1. 配置成上拉输入模式
   2. MISO-PA6
5. 初始化SPI外设SPI_Init
   1. `SPI_Mode`选择SPI模式,决定当前设备是SPI的主机还是从机`SPI_Mode_Master`
   2. `SPI_Direction`选择双位全双工`SPI_Direction_2Lines_FullDuplex`
   3. `SPI_DataSize`配置8位还是16位数据帧
      1. 配置8位先行`SPI_DataSize_8b`
   4. `SPI_FirstBit`配置高位先行还是低位先行
      1. 高位先行`SPI_FirstBit_MSB`
   5. `SPI_BaudRatePrescaler`配置波特率的时钟频率
   6. `SPI_CPOL`配置SPI模式`SPI_CPOL_Low`
   7. `SPI_CPHA`配置SPI模式`SPI_CPHA_1Edge`第一个边沿开始采样`6,7组成模式0`
   8. `SPI_NSS`配置NSS模式,这里使用软件模拟
   9.  `SPI_CRCPolynomial`CRC校验的多项式,随便填一个数即可
6.  SPI_Cmd使能
7.  MySPI_W_SS函数,默认给SS输出高电平,默认不选择从机

#### 交换字节函数
1. 等待TXE为1,发送寄存器为空,如果寄存器不为空,就不着急写,`SPI_I2S_GetFlagStatus`,判断为`SPI_I2S_FLAG_TXE` != set
2. 软件写入数据至SPI_DR`SPI_I2S_SendData`,将要传输的数据传输给TDR
3. 接收移位完成时,会置标志位RXNE,所以只需要等待RXNE出现即可`SPI_I2S_GetFlagStatus`,选择`SPI_I2S_FLAG_RXNE` != set即可
4. 读取DR,从RDR里,把交换到的数据移动出来`SPI_I2S_ReceiveData`

注意: 硬件SPI必须发送的同时接收,如果不发送只接受数据,时序不会变化\
TXE和RXNE不需要手动清除,等待TXE标志位为1后,写DR,自动清除,等待RXNE标志位为1后,读TDR,自动清除