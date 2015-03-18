package com.google.appinventor.components.runtime;
/*
 * DUE Board pin                    |  PORT  | Label
 * ---------------------------------+--------+-------
 *   0                              |  PA10  | "RX0",pwm
 *   1                              |  PA9   | "TX0",pwm
 *   2                              |  PD4   |
 *   3       USART2_TX(remap)       |  PD5   |
 *   4       USART2_RX(remap)       |  PD6   |
 *   5                              |  PD7   |
 *   6       SPI3_SCK,I2S3_CK       |  PB3   |
 *   7       SPI3_MISO              |  PB4   |
 *   8  I2C1_SMBA,SPI3_MOSI,I2S3_SD |  PB5   |
 *   9       I2C1_SCL               |  PB6   |pwm
 *  10       I2C1_SDA               |  PB7   |pwm
 *  11       SDIO_D4                |  PB8   |pwm
 *  12       SDIO_D5                |  PB9   |pwm
 *  13                              |  PE0   | not LED 
 *  14                              |  PD3   |
 *  15       USART5_RX,SDIO_CMD     |  PD2   | 
 *  16       CAN1_TX(remap)         |  PD1   |
 *  17       CAN1_RX(remap)         |  PD0   | 
 *  18       USART5_TX,SDIO_CK      |  PC12  | 
 *  19       USART4_RX,SDIO_D3      |  PC11  | 
 *  20       USART4_TX,SDIO_D2      |  PC10  | 
 *  21       SPI3_NSS,I2S3_WS       |  PA15  |
 *  22                              |  PE3   |
 *  23                              |  PE2   |
 *  24                              |  PA8   |
 *  25       SDIO_D1                |  PC9   |pwm
 *  26       SDIO_D0                |  PC8   |pwm
 *  27       I2S3_MCK               |  PC7   |pwm
 *  28       I2S2_MCK               |  PC6   |pwm
 *  29       TIM4_CH4(remap)        |  PD15  |
 *  30       TIM4_CH3(remap)        |  PD14  |
 *  31       TIM4_CH2(remap)        |  PD13  |
 *  32       TIM4_CH1(remap)        |  PD12  |
 *  33                              |  PD11  |
 *  34                              |  PD10  |
 *  35       USART3_RX(remap)       |  PD9   |
 *  36       USART3_TX(remap)       |  PD8   |
 *  37       SPI2_MOSI,I2S2_SD      |  PB15  |
 *  38       SPI2_MISO              |  PB14  |
 *  39       SPI2_SCK,I2S2_CK       |  PB13  |
 *  40  SPI2_NSS,I2S2_WS,I2C2_SMBA  |  PB12  |
 *  41       I2C2_SDA,USART3_RX     |  PB11  |
 *  42       I2C2_SCL,USART3_TX     |  PB10  |
 *  43                              |  PE5   |
 *  44                              |  PE6   |
 *  45                              |  PE7   |
 *  46                              |  PE8   |
 *  47                              |  PE9   |
 *  48                              |  PE10  |
 *  49                              |  PE11  |
 *  50                              |  PE12  |
 *  51                              |  PE13  |
 *  52                              |  PE14  |
 *  53                              |  PE15  |
 *  54                              |  PC0   | "A0"
 *  55                              |  PC1   | "A1"
 *  56                              |  PC2   | "A2"
 *  57                              |  PC3   | "A3"
 *  58                              |  PA0   | "A4"
 *  69                              |  PA1   | "A5"
 *  60       USART2_TX              |  PA2   | "A6",pwm
 *  61       USART2_RX              |  PA3   | "A7",pwm
 *  62       DAC_OUT1,SPI1_NSS      |  PA4   | "A8"
 *  63       DAC_OUT2,SPI1_SCK      |  PA5   | "A9"
 *  64       SPI1_MISO              |  PA6   | "A10"
 *  65       SPI1_MOSI              |  PA7   | "A11"
 *  66                              |  PC4   | "A12"
 *  67                              |  PC5   | "A13"
 *  68                              |  PB0   | "A14",pwm
 *  69                              |  PB1   | "A15",pwm
 *  70                              |  PC14  | "SDA1"
 *  71                              |  PC15  | "SCL1"
 *  72                              |  PE4   | LED AMBER "L"
 *  73                              |  PB2   |BOOT1
not support
           *  73                              |  PA21  | LED AMBER "TX"
           *  74                        MISO  |  PA25  |
           *  75                        MOSI  |  PA26  |
           *  76                        SCLK  |  PA27  |
           *  77                        NPCS0 |  PA28  |
           *  78                        NPCS3 |  PB23  | unconnected!
 *
 * USB pin                          |  PORT
 * ----------------                 +--------
 *  USBDM                           |  PA11
 *  USBDP                           |  PA12
 *  USBDIS                          |  PC13
 */
public class Variant{
	
	public static String A8 = "A8";
	public static String C8 = "C8";
	public static String C9 = "C9";
	public static String C10 = "C10";
	public static String C11 = "C11";
	public static String C12 = "C12";
	public static String D2 = "D2";
	public static String B5 = "B5";
	public static String B6 = "B6";
	public static String B7 = "B7";
	public static String B8 = "B8";
	public static String B9 = "B9";
	public static String C0 = "C0";
	public static String C1 = "C1";
	public static String C2 = "C2";
	public static String C3 = "C3";
	public static String C7 = "C7";
	public static String C6 = "C6";
	public static String B15 = "B15";
	public static String B14 = "B14";
	public static String B13 = "B13";
	public static String B12 = "B12";
	public static String B11 = "B11";
	public static String B10 = "B10";
	public static String B1 = "B1";
	public static String B0 = "B0";
	public static String A7 = "A7";
	public static String A6 = "A6";
	public static String A5 = "A5";
	public static String A4 = "A4";
	public static String A3 = "A3";
	public static String A2 = "A2";
	public static String A1 = "A1";
	public static String A0 = "A0";
	public static String RED = "RED";
	public static String GREEN = "GREEN";
	public static String A = "A";
	public static String B = "B";
	public static String C = "C";
	
	
	public static int test = 0;
	static int[] digitalOutputData = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	static int[] digitalInputData  = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	static int[] digitalInputData1  = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	
	public Variant(){
		//init(hippoADKLooper);
	}
	
    public void init(){ 
    	
    } 

	public static int GetPortNumber(int pin)		
	{
		int portNumber = 0;
		//portNumber = (pin >> 3) & 0x0F;
		portNumber = pin / 8;
		return portNumber;
	}
	
	public static int GetPinValue(int pin,int value)		
	{
		int portNumber = 0;
		int pinvalue = 0;
		portNumber = pin / 8;
	    if (value == 0)
	        digitalOutputData[portNumber] &= ~(1 << (pin % 8));
	      else
	        digitalOutputData[portNumber] |= (1 << (pin % 8));
	    pinvalue = digitalOutputData[portNumber];
		return pinvalue;
	}
	
	public static int GetDigitalInputValue(int portNumber)		
	{
		int digitalInputValue = digitalInputData[portNumber];
	    //pinvalue = digitalOutputData[portNumber];
		return digitalInputValue;
	}
	
	public static int GetInitPinValue(int pin,int value)		
	{
		int portNumber = 0;
		int pinvalue = 0;
		portNumber = pin / 8;
	    if (value == 0)
	        digitalInputData1[portNumber] &= ~(1 << (pin % 8));
	      else
	        digitalInputData1[portNumber] |= (1 << (pin % 8));
	    pinvalue = digitalInputData1[portNumber];
		return pinvalue;
	}
	
	public static void SetDigitalOutputValue(int portNumber, int readValue)		
	{
		digitalOutputData[portNumber] = readValue;
	}
	
	public static void SetDigitalInputValue(int portNumber, int readValue)		
	{
		digitalInputData[portNumber] = readValue;
	}

	/*
	public static int GetAnalogWriteChanel(String pin)		
	{
		int analogWriteChanel = 0;
		if(pin.equals(C12)){
			analogWriteChanel = 0;
		}else if(pin.equals(D2)){
			analogWriteChanel = 1;
		}else if(pin.equals(B5)){
			analogWriteChanel = 2;
		}else if(pin.equals(B6)){
			analogWriteChanel = 3;
		}else if(pin.equals(C7)){
			analogWriteChanel = 4;
		}else if(pin.equals(C6)){
			analogWriteChanel = 5;
		}else if(pin.equals(B15)){
			analogWriteChanel = 6;
		}else if(pin.equals(B14)){
			analogWriteChanel = 7;
		}else if(pin.equals(B11)){
			analogWriteChanel = 8;
		}else if(pin.equals(B10)){
			analogWriteChanel = 9;
		}else if(pin.equals(B1)){
			analogWriteChanel = 10;
		}else if(pin.equals(B0)){
			analogWriteChanel = 11;
		}else if(pin.equals(A3)){
			analogWriteChanel = 12;
		}else if(pin.equals(A2)){
			analogWriteChanel = 13;
		}
		return analogWriteChanel;
	}
*/	
	
	public static int GetAnalogWriteChanel(String pin)		
	{
		int analogWriteChanel = 0;
		if(pin.equals(A8)){
			analogWriteChanel = 0;
		}else if(pin.equals(C8)){
			analogWriteChanel = 1;
		}else if(pin.equals(C9)){
			analogWriteChanel = 2;
		}else if(pin.equals(B6)){
			analogWriteChanel = 3;
		}else if(pin.equals(B7)){
			analogWriteChanel = 4;
		}else if(pin.equals(B8)){
			analogWriteChanel = 5;
		}else if(pin.equals(B9)){
			analogWriteChanel = 6;
		}else if(pin.equals(C7)){
			analogWriteChanel = 7;
		}else if(pin.equals(C6)){
			analogWriteChanel = 8;
		}else if(pin.equals(B15)){
			analogWriteChanel = 9;
		}else if(pin.equals(B14)){
			analogWriteChanel = 10;
		}else if(pin.equals(B11)){
			analogWriteChanel = 11;
		}else if(pin.equals(B10)){
			analogWriteChanel = 12;
		}else if(pin.equals(B1)){
			analogWriteChanel = 13;
		}else if(pin.equals(B0)){
			analogWriteChanel = 14;
		}
		return analogWriteChanel;
	}
	
	public static int RemapAnalog(String pin)		
	{
		int i = 0;
		if(pin.equals(C0)){
			i = 0;
		}else if(pin.equals(C1)){
			i = 1;
		}else if(pin.equals(C2)){
			i = 2;
		}else if(pin.equals(C3)){
			i = 3;
		}else if(pin.equals(B1)){
			i = 4;
		}else if(pin.equals(B0)){
			i = 5;
		}else if(pin.equals(A7)){
			i = 6;
		}else if(pin.equals(A6)){
			i = 7;
		}else if(pin.equals(A5)){
			i = 8;
		}else if(pin.equals(A4)){
			i = 9;
		}else if(pin.equals(A3)){
			i = 10;
		}else if(pin.equals(A2)){
			i = 11;
		}else if(pin.equals(A1)){
			i = 12;
		}else if(pin.equals(A0)){
			i = 13;
		}
		return i;
	}
	public static int Remap(String pin)		
	{
		int i = -1;
		if(pin.equals(A8)){
			i = 3;
		}else if(pin.equals(C8)){
			i = 4;
		}else if(pin.equals(C9)){
			i = 5;
		}else if(pin.equals(C10)){
			i = 6;
		}else if(pin.equals(C11)){
			i = 7;
		}else if(pin.equals(C12)){
			i = 8;
		}else if(pin.equals(D2)){
			i = 9;
		}else if(pin.equals(B5)){
			i = 10;
		}else if(pin.equals(B6)){
			i = 11;
		}else if(pin.equals(B7)){
			i = 12;
		}else if(pin.equals(B8)){
			i = 13;
		}else if(pin.equals(B9)){
			i = 14;
		}else if(pin.equals(C0)){
			i = 15;
		}else if(pin.equals(C1)){
			i = 16;
		}else if(pin.equals(C2)){
			i = 17;
		}else if(pin.equals(C3)){
			i = 18;
		}else if(pin.equals(C7)){
			i = 22;
		}else if(pin.equals(C6)){
			i = 23;
		}else if(pin.equals(B15)){
			i = 24;
		}else if(pin.equals(B14)){
			i = 25;
		}else if(pin.equals(B13)){
			i = 26;
		}else if(pin.equals(B12)){
			i = 27;
		}else if(pin.equals(B11)){
			i = 28;
		}else if(pin.equals(B10)){
			i = 29;
		}else if(pin.equals(B1)){
			i = 30;
		}else if(pin.equals(B0)){
			i = 31;
		}else if(pin.equals(A7)){
			i = 32;
		}else if(pin.equals(A6)){
			i = 33;
		}else if(pin.equals(A5)){
			i = 34;
		}else if(pin.equals(A4)){
			i = 35;
		}else if(pin.equals(A3)){
			i = 36;
		}else if(pin.equals(A2)){
			i = 37;
		}else if(pin.equals(A1)){
			i = 38;
		}else if(pin.equals(A0)){
			i = 49;
		}else if(pin.equals(RED)){
			i = 40;
		}else if(pin.equals(GREEN)){
			i = 41;
		}else if(pin.equals(A)){
			i = 0;
		}else if(pin.equals(B)){
			i = 1;
		}else if(pin.equals(C)){
			i = 2;
		}
		return i;
	}
} 