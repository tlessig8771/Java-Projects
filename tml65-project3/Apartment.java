
public class Apartment
{
	private String Address;
	private String Number; 			//initialize variables
	private String City; 
	private int Zip; 
	public double Rent;
	private double Footage;
	
	public Apartment(String address, String number, String city, int zip, double rent, double footage)
	{
		Address = address;
		Number = number;
		City = city;
		Zip = zip;
		Rent = rent;
		Footage = footage;
	}
	
	public String getAddress()
	{
		return Address;
	}
	
	public String getNum()
	{
		return Number;
	}
	
	public String getCity()
	{
		return City;
	}
	
	public int getZip()
	{
		return Zip;
	}
	
	public double getRent()
	{
		return Rent;
	}
	
	public double getFootage()
	{
		return Footage;
	}
	
	public String toString()
	{
		String apt = "\t"+getAddress() + "\n\tApt " + getNum() + "\n\t"+ getCity() + ", " 
	+ getZip() + "\n\tRent: " + getRent() + "\n\tSquare Footage: " + getFootage()+"\n";
		
		return apt;
	}
	
	public int hashCode()	//hashcode method to get unique codes 
	{
		String uniqueHash = Address + Number + Zip;
		return uniqueHash.hashCode();
	}
}
