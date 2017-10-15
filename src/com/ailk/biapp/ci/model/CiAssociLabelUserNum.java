package com.ailk.biapp.ci.model;


public class CiAssociLabelUserNum
{

	private Long relUserNum1;
	private Long relUserNum2;
	private Long relUserNum3;
	private Long relUserNum4;
	private Long relUserNum5;
	private Long relUserNum6;
	private Long relUserNum7;
	private Long relUserNum8;
	private Long relUserNum9;
	private Long relUserNum10;

	public CiAssociLabelUserNum()
	{
	}

	public Long getRelUserNum1()
	{
		return relUserNum1;
	}

	public void setRelUserNum1(Long relUserNum1)
	{
		this.relUserNum1 = relUserNum1;
	}

	public Long getRelUserNum2()
	{
		return relUserNum2;
	}

	public void setRelUserNum2(Long relUserNum2)
	{
		this.relUserNum2 = relUserNum2;
	}

	public Long getRelUserNum3()
	{
		return relUserNum3;
	}

	public void setRelUserNum3(Long relUserNum3)
	{
		this.relUserNum3 = relUserNum3;
	}

	public Long getRelUserNum4()
	{
		return relUserNum4;
	}

	public void setRelUserNum4(Long relUserNum4)
	{
		this.relUserNum4 = relUserNum4;
	}

	public Long getRelUserNum5()
	{
		return relUserNum5;
	}

	public void setRelUserNum5(Long relUserNum5)
	{
		this.relUserNum5 = relUserNum5;
	}

	public Long getRelUserNum6()
	{
		return relUserNum6;
	}

	public void setRelUserNum6(Long relUserNum6)
	{
		this.relUserNum6 = relUserNum6;
	}

	public Long getRelUserNum7()
	{
		return relUserNum7;
	}

	public void setRelUserNum7(Long relUserNum7)
	{
		this.relUserNum7 = relUserNum7;
	}

	public Long getRelUserNum8()
	{
		return relUserNum8;
	}

	public void setRelUserNum8(Long relUserNum8)
	{
		this.relUserNum8 = relUserNum8;
	}

	public Long getRelUserNum9()
	{
		return relUserNum9;
	}

	public void setRelUserNum9(Long relUserNum9)
	{
		this.relUserNum9 = relUserNum9;
	}

	public Long getRelUserNum10()
	{
		return relUserNum10;
	}

	public void setRelUserNum10(Long relUserNum10)
	{
		this.relUserNum10 = relUserNum10;
	}

	public Long getRelUserNum(int i)
	{
		switch (i)
		{
		case 1: // '\001'
			return relUserNum1;

		case 2: // '\002'
			return relUserNum2;

		case 3: // '\003'
			return relUserNum3;

		case 4: // '\004'
			return relUserNum4;

		case 5: // '\005'
			return relUserNum5;

		case 6: // '\006'
			return relUserNum6;

		case 7: // '\007'
			return relUserNum7;

		case 8: // '\b'
			return relUserNum8;

		case 9: // '\t'
			return relUserNum9;

		case 10: // '\n'
			return relUserNum10;
		}
		return Long.valueOf(0L);
	}
}
