package assignment2;

public class Warehouse{

	protected Shelf[] storage;
	protected int nbShelves;
	public Box toShip;
	public UrgentBox toShipUrgently;
	static String problem = "problem encountered while performing the operation";
	static String noProblem = "operation was successfully carried out";
	
	public Warehouse(int n, int[] heights, int[] lengths){
		this.nbShelves = n;
		this.storage = new Shelf[n];
		for (int i = 0; i < n; i++){
			this.storage[i]= new Shelf(heights[i], lengths[i]);
		}
		this.toShip = null;
		this.toShipUrgently = null;
	}
	
	public String printShipping(){
		Box b = toShip;
		String result = "not urgent : ";
		while(b != null){
			result += b.id + ", ";
			b = b.next;
		}
		result += "\n" + "should be already gone : ";
		b = toShipUrgently;
		while(b != null){
			result += b.id + ", ";
			b = b.next;
		}
		result += "\n";
		return result;
	}
	
 	public String print(){
 		String result = "";
		for (int i = 0; i < nbShelves; i++){
			result += i + "-th shelf " + storage[i].print();
		}
		return result;
	}
	
 	public void clear(){
 		toShip = null;
 		toShipUrgently = null;
 		for (int i = 0; i < nbShelves ; i++){
 			storage[i].clear();
 		}
 	}
 	
 	/**
 	 * initiate the merge sort algorithm
 	 */
	public void sort(){
		mergeSort(0, nbShelves -1);
	}
	
	/**
	 * performs the induction step of the merge sort algorithm
	 * @param start
	 * @param end
	 */
	protected void mergeSort(int start, int end){
		//Get the index of the middle (average indexes Floored through int arithmetic)
		int mid = (start + end)/2;
		//Checks to ensure correct index inputs
		if (start < 0)
		{
			System.out.println("Please ensure that the starting index is greater than -1");
		}
		else if (end < 0)
		{
			System.out.println("Please ensure that the ending index is greather than -1");
		}
		//If start is smaller than end perform the mergeSort recursive algorithm
		if (start < end)
		{
			this.mergeSort(start, mid);
			this.mergeSort(mid + 1, end);
			this.merge(start, mid, end);
		}
		//Base case that if start and end are the same then it's trivially sorted
		else if (start == end)
		{
			return;
		}
		else
		{
			System.out.println("Please ensure that the starting index is smaller than the ending index.");
		}
	}
	
	/**
	 * performs the merge part of the merge sort algorithm
	 * @param start
	 * @param mid
	 * @param end
	 */
	protected void merge(int start, int mid, int end){
		//Declare a left and right array of Shelf to hold the two individually sorted lists
		int n1 = mid - start + 1;
		int n2 = end - mid;
		Shelf[] left = new Shelf[n1 + 1];
		Shelf[] right = new Shelf[n2 + 1];
		//Fill the left and right arrays with the correct values from the given arguments and the parent list
		for (int i = 0; i < n1; i++)
		{
			left[i] = this.storage[start + i];
		}
		for (int j = 0; j < n2; j++)
		{
			right[j] = this.storage[mid + j + 1];
		}
		//Sentinels to avoid checks at each iteration
		left[n1] = new Shelf(-1, 15);
		right[n2] = new Shelf(2147483647, 15);
		int i = 0;
		int j = 0;
		for (int k = start; k < end + 1; k++)
		{
			//Store the smaller of either index for every element of the parent list
			if (left[i].height <= right[j].height && left[i].height != -1)
			{
				this.storage[k] = left[i];
				i++;
			}
			else if (right[j].height != 2147483647)
			{
				this.storage[k] = right[j];
				j++;
			}
		}
	}
	
	/**
	 * Adds a box is the smallest possible shelf where there is room available.
	 * Here we assume that there is at least one shelf (i.e. nbShelves >0)
	 * @param b
	 * @return problem or noProblem
	 */
	public String addBox (Box b){
		//Variable to keep track of the difference between the box height and the shelf height (the value we wish to minimize)
		int tempHeightDiff = 1000;
		int tempIndex = -1;
		for (int i = 0; i < this.nbShelves; i++)
		{
			//Check if the box can fit
			if (this.storage[i].height >= b.height && this.storage[i].availableLength >= b.length)
			{
				//Check if the height difference is smaller
				if (tempHeightDiff > (this.storage[i].height - b.height))
				{
					tempHeightDiff = this.storage[i].height - b.height;
					tempIndex = i;
				}
			}
		}
		if (tempIndex == -1)
		{
			return problem;
		}
		this.storage[tempIndex].addBox(b);
		return noProblem;
	}
	
	/**
	 * Adds a box to its corresponding shipping list and updates all the fields
	 * @param b
	 * @return problem or noProblem
	 */
	public String addToShip (Box b){
		//Check to see if the Box b is an instance of subclass UrgentBox
		if (UrgentBox.class.isInstance(b))
		{
			//If the urgent shipping list is empty
			if (this.toShipUrgently == null)
			{
				this.toShipUrgently = (UrgentBox) b;
			}
			else
			{
				this.toShipUrgently.previous = b;
				b.next = this.toShipUrgently;
				this.toShipUrgently = (UrgentBox) b;
			}
			return noProblem;
		}
		//If the Box b is an instance of Box
		else if (Box.class.isInstance(b))
		{
			//If the list is empty
			if (this.toShip == null)
			{
				this.toShip = b;
			}
			else
			{
				this.toShip.previous = b;
				b.next = this.toShip;
				this.toShip = b;
			}
			return noProblem;
		}
		//How did you even pass something that isn't a Box or urgentBox through this function???
		else
		{
			return problem;
		}
	}
	
	/**
	 * Find a box with the identifier (if it exists)
	 * Remove the box from its corresponding shelf
	 * Add it to its corresponding shipping list
	 * @param identifier
	 * @return problem or noProblem
	 */
	public String shipBox (String identifier){
		for (int i = 0; i < this.nbShelves; i++)
		{
			//Only add the box to the shipping lists if there is a box with the given identifier
			Box b = this.storage[i].removeBox(identifier);
			if (b != null)
			{
				this.addToShip(b);
				return noProblem;
			}
		}
		return problem;
	}
	
	/**
	 * if there is a better shelf for the box, moves the box to the optimal shelf.
	 * If there are none, do not do anything
	 * @param b
	 * @param position
	 */
	public void moveOneBox (Box b, int position){
		//Essentially this is the same as the initial addBox algorithm only it needs an argument to keep track of the initial position the box is in
		//so that we may remove it to the more suitable position (if available)
		int tempHeightDiff = this.storage[position].height - b.height;
		int tempIndex = position;
		for (int i = 0; i < this.nbShelves; i++)
		{
			if (this.storage[i].height >= b.height && this.storage[i].availableLength >= b.length)
			{
				if (tempHeightDiff > (this.storage[i].height - b.height))
				{
					tempHeightDiff = this.storage[i].height - b.height;
					tempIndex = i;
				}
			}
		}
		if (tempIndex != position)
		{
			this.storage[position].removeBox(b.id);
			this.storage[tempIndex].addBox(b);
		}
	}
	
	/**
	 * reorganize the entire warehouse : start with smaller shelves and first box on each shelf.
	 */
	public void reorganize (){
		for (int i = 0; i < this.nbShelves; i++)
		{
			//Move each box one by one to a more desirable location (if available)
			Box b = this.storage[i].firstBox;
			while(b != null)
			{
				//Temporary box to get around moveOneBox removing the box from it's original linked list and hence b.next shall change
				Box temp = b.next;
				this.moveOneBox(b, i);
				b = temp;
			}
		}
	}
}