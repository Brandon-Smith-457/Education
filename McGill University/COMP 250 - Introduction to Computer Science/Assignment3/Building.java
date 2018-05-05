package assignment3;

public class Building {

	OneBuilding data;
	Building older;
	Building same;
	Building younger;
	
	public Building(OneBuilding data){
		this.data = data;
		this.older = null;
		this.same = null;
		this.younger = null;
	}
	
	public String toString(){
		String result = this.data.toString() + "\n";
		if (this.older != null){
			result += "older than " + this.data.toString() + " :\n";
			result += this.older.toString();
		}
		if (this.same != null){
			result += "same age as " + this.data.toString() + " :\n";
			result += this.same.toString();
		}
		if (this.younger != null){
			result += "younger than " + this.data.toString() + " :\n";
			result += this.younger.toString();
		}
		return result;
	}
	
	public Building addBuilding (OneBuilding b){
		//If added is the same age as this
		if (b.yearOfConstruction == this.data.yearOfConstruction)
		{
			//If this is the leaf
			if (this.same == null)
			{
				//If added is taller than this
				if (b.height > this.data.height)
				{
					//Add a new Building in the same branch
					this.same = new Building(b);
					this.same.data = this.data;
					this.data = b;
				}
				//If added is shorter than or equal to this
				else
				{
					this.same = new Building(b);
				}
			}
			//If this is not the leaf
			else
			{
				//If added is taller than this
				if (b.height > this.data.height)
				{
					Building added = new Building(b);
					added.same = this.same;
					this.same = added;
					added.data = this.data;
					this.data = b;
				}
				//If added is shorter than this but taller than what's below this
				else if (b.height < this.data.height && b.height > this.same.data.height)
				{
					Building added = new Building(b);
					added.same = this.same;
					this.same = added;
				}
				//If added is shorter than what's below this
				else
				{
					this.same.addBuilding(b);
				}
			}
		}
		
		//If added is older than this
		else if (b.yearOfConstruction < this.data.yearOfConstruction)
		{
			//If this is a leaf
			if (this.older == null)
			{
				this.older = new Building(b);
			}
			//If this is not a leaf
			else
			{
				this.older.addBuilding(b);
			}
		}
		
		//If added is younger than this
		else
		{
			//If this is a leaf
			if (this.younger == null)
			{
				this.younger = new Building(b);
			}
			//If this is not a leaf
			else
			{
				this.younger.addBuilding(b);
			}
		}
		return this;
	}
	
	public Building addBuildings (Building b){
		//Add this to the Building tree called from
		this.addBuilding(b.data);
		//Loop through the rest of the b tree
		if (b.older != null)
		{
			this.addBuildings(b.older);
		}
		if (b.same != null)
		{
			this.addBuildings(b.same);
		}
		if (b.younger != null)
		{
			this.addBuildings(b.younger);
		}
		return this;
	}
	
	public Building removeBuilding (OneBuilding b){
		//If what we want to remove is a leaf
		if (this.older != null)
		{
			if (this.older.data.equals(b) && this.older.older == null && this.older.same == null && this.older.younger == null)
			{
				//Remove the leaf
				this.older = null;
			}
		}
		if (this.same != null)
		{
			if (this.same.data.equals(b) && this.same.older == null && this.same.same == null && this.same.younger == null)
			{
				//Remove the leaf
				this.same = null;
			}
		}
		if (this.younger != null)
		{
			if (this.younger.data.equals(b) && this.younger.older == null && this.younger.same == null && this.younger.younger == null)
			{
				//Remove the leaf
				this.younger = null;
			}
		}
		
		//If what we want to remove is the root of "this"
		if (this.data.equals(b))
		{
			if (this.same != null)
			{
				//Put this.same's data in this, and then point this.same to this.same.same
				this.data = this.same.data;
				this.same = this.same.same;
			}
			else if (this.same == null && this.older != null)
			{
				//If there is only the older branch, update the necessary fields of this
				if (this.younger == null)
				{
					this.data = this.older.data;
					this.same = this.older.same;
					this.younger = this.older.younger;
					this.older = this.older.older;
				}
				//If there are both older and younger branches, store the younger, then update the fields of this, then add on younger
				else
				{
					Building temp = this.younger;
					this.data = this.older.data;
					this.same = this.older.same;
					this.younger = this.older.younger;
					this.older = this.older.older;
					this.addBuildings(temp);
				}
			}
			else if (this.same == null && this.older == null && this.younger != null)
			{
				//If there is only the younger branch, update this with the younger branches fields
				this.data = this.younger.data;
				this.older = this.younger.older;
				this.same = this.younger.same;
				this.younger = this.younger.younger;
			}
			else if (this.same == null && this.older == null && this.younger == null)
			{
				//If this is not even a tree, make the data null
				this.data = null;
			}
		}
		
		//Search through the tree
		if (this.older != null)
		{
			this.older.removeBuilding(b);
		}
		if (this.same != null)
		{
			this.same.removeBuilding(b);
		}
		if (this.younger != null)
		{
			this.younger.removeBuilding(b);
		}
		
		return this;
	}

	public int oldest(){
		//Travel down the older branch until we are at the oldest
		if (this.older != null)
		{
			return this.older.oldest();
		}
		return this.data.yearOfConstruction;
	}
	
	public int highest(){
		//temp to compare the returned heights with this.data.height
		int temp = -1;
		//Look down the older branch
		if (this.older != null)
		{
			//Return the higher of the two being compared
			temp = this.older.highest();
			if (temp > this.data.height)
			{
				return temp;
			}
			return this.data.height;
		}
		//Look down the younger branch
		if (this.younger != null)
		{
			//Return the higher of the two being compared
			temp = this.younger.highest();
			if (temp > this.data.height)
			{
				return temp;
			}
			return this.data.height;
		}
		//If there are no younger or older branches to check
		return this.data.height;
	}
	
	public OneBuilding highestFromYear (int year){
		//If the root is the year you're looking for
		if (this.data.yearOfConstruction == year)
		{
			return this.data;
		}
		//This is younger than desired year, and there is an older branch
		if (this.data.yearOfConstruction > year && this.older != null)
		{
			return this.older.highestFromYear(year);
		}
		//This is older than desired year, and there is a younger branch
		if (this.data.yearOfConstruction < year && this.younger != null)
		{
			return this.younger.highestFromYear(year);
		}
		//There is no such year in the tree
		return null;
	}
	
	public int numberFromYears (int yearMin, int yearMax){
		//Counter to keep track of how many in the year range
		int counter = 0;
		//If the min and max fields are invalid
		if (yearMin > yearMax)
		{
			return 0;
		}
		//If this' older branch exists
		if (this.older != null)
		{
			//Counter becomes what counter was plus 1 if the next Building in the older branch is within the year range
			counter = counter + this.older.numberFromYears(yearMin, yearMax);
		}
		//If this' same branch exists
		if (this.same != null)
		{
			//Counter becomes what counter was plus 1 if the next Building in the same branch is within the year range
			counter = counter + this.same.numberFromYears(yearMin, yearMax);
		}
		//If this' younger branch exists
		if (this.younger != null)
		{
			//Counter becomes what counter was plus 1 if the next Building in the younger branch is within the year range
			counter = counter + this.younger.numberFromYears(yearMin, yearMax);
		}
		//If this is within the year range increase counter by 1
		if (this.data.yearOfConstruction >= yearMin && this.data.yearOfConstruction <= yearMax)
		{
			counter++;
		}
		return counter;
	}
	
	//Helper method to add each element of two arrays together
	public static int[] addArrays(int[] arr, int[] arr2)
	{
		for (int i = 0; i < arr.length; i++)
		{
			arr[i] += arr2[i];
		}
		return arr;
	}
	
	public int[] costPlanning (int nbYears){
		int[] array = new int[nbYears];
		if (this.older != null)
		{
			//array becomes what array was plus the costs from this.older
			array = addArrays(array, this.older.costPlanning(nbYears));
		}
		if (this.same != null)
		{
			//array becomes what array was plus the costs from this.same
			array = addArrays(array, this.same.costPlanning(nbYears));
		}
		if (this.younger != null)
		{
			//array becomes what array was plus the costs from this.younger
			array = addArrays(array, this.younger.costPlanning(nbYears));
		}
		//If the Building is in the scope for necessary repairs add the cost to the correct indexed element of array
		if (this.data.yearForRepair >= 2018 && this.data.yearForRepair < (2018 + nbYears))
		{
			array[this.data.yearForRepair - 2018] += this.data.costForRepair;
		}
		return array;
	}
}
