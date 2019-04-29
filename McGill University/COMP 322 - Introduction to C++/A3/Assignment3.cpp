/*
 * Question 1: Standard Library Smart Pointers
 *
 * unique_ptr:
 * The unique pointer, as its name suggests, is a singleton pointer.  This means that
 * we are guaranteed that at most one copy of an object exists and the object is destroyed
 * when the scope containing the unique_ptr is left.  It allows for declaration instantiation,
 * re-assigning, and moving from one unique_ptr to another.  All three of these operations behave
 * according to the ideology of Smart Pointers in that they handle memory allocation and deletion for the programmer.
 *
 * shared_ptr:
 * The shared pointer keeps track of how many references to the pointer remain.
 * In doing so, it provides the programmer with a means of passing pointers outside of their initial creation scope without having
 * to worry at all about deleting the memory.  As soon as all references to the pointer no longer need them
 * (ie we leave the scopes of any local variables containing a reference to the shared pointer each time reducing the counter by 1)
 * then when we delete the Class that contained the shared reference at the start, the shared pointer will be deleted because
 * the counter will hit 0.
 *
 * weak_ptr:
 * Is called weak because it holds a "non-owning" reference to an object/pointer.  What this means
 * is that it cannot govern over the life-span of an object/pointer but simply tracks it meaning
 * that if the object/pointer has been destroyed elsewhere in the code then the weak_ptr has no say
 * in the matter.  In order for a weak_ptr to access an object, it needs to first be converted
 * into a shared_ptr.  The weak_ptr has multiple uses, another of which is the "break reference cycles".
 * What this means is that if we have a group of shared_ptr's somewhere in the code that we have forgotten
 * or "left behind" then having one of those share_ptr's be replaced by a weak_ptr would break the cycle,
 * allowing for the counter to hit 0.
 *
 * auto_ptr:
 * This was the first implemented smart pointer which was replaced by the unique_ptr due to
 * some design flaws.
 */

#include <iostream>
#include <string>
#include <exception>
#include <list>

using namespace std;

char *emergencyRelease = new char[16384]; //For when memory is overflowed

namespace SmartPointerSpace {
	template <typename T>
	class SmartPointer {
	public:
		SmartPointer();
		SmartPointer(T a);
		virtual ~SmartPointer();
		void setValue(T a);
		T getValue();
		friend SmartPointer<T> operator+(SmartPointer<T> &smrt_ptr, SmartPointer<T> &smrt_ptr2){
			SmartPointer<T> result(smrt_ptr.getValue() + smrt_ptr2.getValue());
			return result;
		}
		friend SmartPointer<T> operator-(SmartPointer<T> &smrt_ptr, SmartPointer<T> &smrt_ptr2) {
			SmartPointer<T> result(smrt_ptr.getValue() - smrt_ptr2.getValue());
			return result;
		}
		friend SmartPointer<T> operator*(SmartPointer<T> &smrt_ptr, SmartPointer<T> &smrt_ptr2) {
			SmartPointer<T> result(smrt_ptr.getValue() * smrt_ptr2.getValue());
			return result;
		}
	private:
		T *value;
	};

	template <typename T>
	SmartPointer<T>::SmartPointer() {
		try {
			value = new T(0);
		} catch (bad_alloc &e) {
			delete emergencyRelease;
			cout << e.what() << endl;
			exit(1);
		}
	}

	template <typename T>
	SmartPointer<T>::SmartPointer(T a) {
		try {
			if (a < ((T) 0)) throw "Error: Only positive numbers accepted, defaulting to 0\n";
			value = new T(a);
		} catch (bad_alloc &e) {
			delete emergencyRelease;
			cout << e.what() << endl;
			exit(1);
		} catch (const char *msg) {
			cout << msg;
			value = new T(0);
		}
	}

	template <typename T>
	SmartPointer<T>::~SmartPointer() {
		delete value;
	}

	template <typename T>
	void SmartPointer<T>::setValue(T a) {
		try {
			if (a < 0) throw "Error: Only positive numbers accepted, defaulting to 0\n";
			*value = a;
		} catch (const char *msg) {
			cout << msg;
			*value = (T) 0;
		}
	}

	template <typename T>
	T SmartPointer<T>::getValue() {
		T result = *value;
		return result;
	}
}

using namespace SmartPointerSpace;

int main() {

	//Question 2:
	SmartPointer<int> sPointer(11);
	cout << sPointer.getValue() << endl;

	SmartPointer<int> sPointer2;
	sPointer2.setValue(133);
	cout << sPointer2.getValue() << endl;

	/*
	 * Question 3: The memory can be overflowed in main when allocating space for the
	 *             object itself so technically if I were to want proper memory checks
	 *             they need to also be added to main as shown in the following commented
	 *             section.
	 */

	/*  ONLY UNCOMMENT THIS IF YOU DON'T MIND YOUR RAM GETTING FILLED UP
	list<SmartPointer> sPointers;
	while (true) {
		try {
			SmartPointer temp(1);
			sPointers.push_front(temp);
		} catch (bad_alloc &e) {
			delete emergencyRelease;
			cout << e.what() << endl;
			exit(1);
		}
	}
	*/

	SmartPointer<int> sPointer3(-1);
	cout << sPointer3.getValue() << endl;
	sPointer3.setValue(-1);
	cout << sPointer3.getValue() << endl;

	//Question 4:
	SmartPointer<float> sPointer4;
	sPointer4.setValue(13.31);
	cout << sPointer4.getValue() << endl;

	SmartPointer<int> sPointer5 = sPointer + sPointer2;
	cout << sPointer5.getValue() << endl;
	cout << sPointer.getValue() << " " << sPointer2.getValue() << endl;

	SmartPointer<float> sPointer6 = sPointer4 - sPointer4;
	cout << sPointer4.getValue() << endl;

	SmartPointer<double> sPointer7(9.999);
	cout << sPointer7.getValue() << endl;

	SmartPointer<double> sPointer8 = sPointer7 * sPointer7;
	cout << sPointer8.getValue() << endl;

	// For SmartPointer class
	cout << "Testing SmartPointer class" << endl;

	// Testing Constructors
	cout << "Creating a SmartPointer of type int with value 11" << endl;
	SmartPointer<int> SmartIntPointer1(11);
	cout << "SmartIntPointer1 = " << SmartIntPointer1.getValue() << endl;

	cout << "Creating a SmartPointer of type int with value -1" << endl;
	SmartPointer<int> SmartIntPointer(-1);

	cout << "Creating a SmartPointer of type int with no value provided" << endl;
	SmartPointer<int> SmartIntPointer2;
	cout << "SmartIntPointer2 = " << SmartIntPointer2.getValue() << endl;

	// Testing Setter & Getter
	cout << "Setting value of SmartIntPointer2 to 5" << endl;
	SmartIntPointer2.setValue(5);
	cout << "SmartIntPointer2 = " << SmartIntPointer2.getValue() << endl;

	cout << "Creating a SmartPointer of type float with no value provided" << endl;
	SmartPointer<float> SmartFloatPointer1;
	cout << "SmartFloatPointer1 = " << SmartFloatPointer1.getValue() << endl;

	cout << "Setting value of SmartFloatPointer1 to 1.5" << endl;
	SmartFloatPointer1.setValue(1.5);
	cout << "SmartFloatPointer1 = " << SmartFloatPointer1.getValue() << endl;

	cout << "Creating a SmartPointer of type float with no value provided" << endl;
	SmartPointer<float> SmartFloatPointer2;
	cout << "SmartFloatPointer2 = " << SmartFloatPointer2.getValue() << endl;

	cout << "Setting value of SmartFloatPointer2 to 2.5" << endl;
	SmartFloatPointer2.setValue(2.5);
	cout << "SmartFloatPointer2 = " << SmartFloatPointer2.getValue() << endl;

	SmartPointer<float> SmartFloatPointer3 = SmartFloatPointer1 + SmartFloatPointer2;
	cout << "SmartFloatPointer1 + SmartFloatPointer2 = " << SmartFloatPointer3.getValue() << endl;

	cout << SmartFloatPointer2.getValue() << " " << SmartFloatPointer1.getValue() << endl;
	SmartPointer<float> SmartFloatPointer4 = SmartFloatPointer2 - SmartFloatPointer1;
	cout << "SmartFloatPointer2 - SmartFloatPointer1 = " << SmartFloatPointer4.getValue() << endl;

	SmartPointer<float> SmartFloatPointer5 = SmartFloatPointer1 * SmartFloatPointer2;
	cout << "SmartFloatPointer1 * SmartFloatPointer2 = " << SmartFloatPointer5.getValue() << endl;

	// For handling arrays
	cout << "Testing arrays" << endl;

	//
	// add the needed code that shows how you use your class to create an array of multiple elements of a certain type.
	// provide all the necessary test code that shows the different use cases of your code
	return 0;
}
