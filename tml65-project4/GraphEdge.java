
public class GraphEdge 
{
	private final int copperSpeed = 230000000;
	private final int opticalSpeed = 200000000;
	private int Start;
	private int End;
	private String cableType;
	private int bandwidth;
	private int cableLength;
	private double time;
	String edgeProperties;
	
	public GraphEdge(int start, int end, String cType, int band, int length)
	{
		Start = start;
		End = end;
		cableType = cType;
		bandwidth = band;
		cableLength = length;
		
		if(cableType.equals("copper"))
			time = ((double) 1/copperSpeed * cableLength);
		else if(cableType.equals("optical"))
			time = ((double) 1/opticalSpeed * cableLength);	
	}
	
	public int getStart()
	{
		return Start;
	}
	
	public int getEnd()
	{
		return End;
	}
	
	public String getCableType()
	{
		return cableType;
	}
	
	public int getBandwidth()
	{
		return bandwidth;
	}
	
	public int getCableLength()
	{
		return cableLength;
	}
	
	public double getTime()
	{
		return time;
	}
	
	public String toString()
	{
		edgeProperties = Start+" "+End+" "+cableType+" "+bandwidth+" "+cableLength;
		
		return edgeProperties;
	}
}
