package assignment2;

public class Shelf {
	
	protected int height;
	protected int availableLength;
	protected int totalLength;
	protected Box firstBox;
	protected Box lastBox;

	public Shelf(int height, int totalLength){
		this.height = height;
		this.availableLength = totalLength;
		this.totalLength = totalLength;
		this.firstBox = null;
		this.lastBox = null;
	}
	
	protected void clear(){
		availableLength = totalLength;
		firstBox = null;
		lastBox = null;
	}
	
	public String print(){
		String result = "( " + height + " - " + availableLength + " ) : ";
		Box b = firstBox;
		while(b != null){
			result += b.id + ", ";
			b = b.next;
		}
		result += "\n";
		return result;
	}
	
	/**
	 * Adds a box on the shelf. Here we assume that the box fits in height and length on the shelf.
	 * @param b
	 */
	public void addBox(Box b){
		//Detach box 1 from any previous list (only really applies if you add a box that was previously in the shipping list)
		//before adding it to a new one
		b.next = null;
		b.previous = null;
		//If the list is empty
		if (this.firstBox == null && this.lastBox == null)
		{
			this.firstBox = this.lastBox = b;
		}
		//If the list contains only one element
		else if (this.firstBox == this.lastBox)
		{
			this.firstBox.next = this.lastBox = b;
			this.lastBox.previous = this.firstBox;
		}
		//If the list contains more than on element
		else
		{
			this.lastBox.next = b;
			b.previous = this.lastBox;
			this.lastBox = b;
		}
		//Update the available length field
		this.availableLength = this.availableLength - b.length;
	}
	
	/**
	 * If the box with the identifier is on the shelf, remove the box from the shelf and return that box.
	 * If not, do not do anything to the Shelf and return null.
	 * @param identifier
	 * @return
	 */
	public Box removeBox(String identifier){
		//Loop through the linked list starting from the firstBox
		Box b = this.firstBox;
		while (b != null)
		{
			//If the id matches
			if (b.id == identifier)
			{
				//If it's the only box in the list
				if (b.equals(this.firstBox) && b.equals(this.lastBox)) 
				{
					this.firstBox = null;
					this.lastBox = null;
				}
				//If it's the first element in the list
				else if (b.equals(this.firstBox))
				{
					this.firstBox = b.next;
					this.firstBox.previous = null;
				}
				//If it's the last element in the list
				else if (b.equals(this.lastBox))
				{
					this.lastBox = b.previous;
					this.lastBox.next = null;
				}
				//If it's an element within the list
				else
				{
					b.next.previous = b.previous;
					b.previous.next = b.next;
				}
				//Remove any connections b had with it's list
				b.next = null;
				b.previous = null;
				//Update the availableLength field
				this.availableLength = this.availableLength + b.length;
				return b;
			}
			//If the id doesn't match
			else
			{
				//Increment to the next element in the list
				b = b.next;
			}
		}
		return null;
	}
	
}
