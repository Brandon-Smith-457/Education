/*
 * SuperDraw.cpp
 *
 *  Created on: Mar. 9, 2019
 *      Author: Brandon Smith
 */

#include "SuperDraw.h"

int main() {
	SuperDraw::SuperDraw *sd = new SuperDraw::SuperDraw();
	sd->newTicket(1);
	sd->newTicket(1);
	sd->newTicket(0);
	sd->newTicket(0);
	sd->newTicket(0);
	sd->newTicket(0);
	sd->newTicket(0);
	sd->newTicket(0);
	sd->newTicket(0);
	sd->newTicket(0);

	SuperDraw::SuperDraw sd2;
	sd2.newTicket(1);
	sd2.newTicket(1);
	sd2.newTicket(0);
	sd2.newTicket(0);
	sd2.newTicket(0);
	sd2.newTicket(0);
	sd2.newTicket(0);
	sd2.newTicket(0);
	sd2.newTicket(0);
	sd2.newTicket(0);

	SuperDraw::SuperDraw sd3(10);

	sd->printAllTicketNumbers();
	sd2.printAllTicketNumbers();
	sd3.printAllTicketNumbers();

	unsigned int myNumbers1[6] = {4, 38, 17, 10, 16, 41};
	sd->addSpecificSequence(myNumbers1);
	sd->verifySequence(myNumbers1);
	unsigned int myNumbers2[6] = {4, 38, 17, 10, 16, 41};
	sd->deleteSequence(myNumbers2);
	sd->verifySequence(myNumbers1);

	sd3.addSpecificSequence(myNumbers1);
	SuperDraw::SuperDraw sd4 = sd3;
	sd4.deleteSequence(myNumbers2);
	sd3.printAllTicketNumbers();
	sd4.printAllTicketNumbers();

	sd3.addSpecificSequence(myNumbers1);
	SuperDraw::SuperDraw sd5(sd3);
	sd5.deleteSequence(myNumbers2);
	sd3.printAllTicketNumbers();
	sd5.printAllTicketNumbers();

	delete sd;
	return 0;
}

namespace SuperDraw {
	ticket *ticketListHead;
	ticket *ticketListTail;

	//Setup random number generator
	std::default_random_engine generator(std::chrono::system_clock::now().time_since_epoch().count());
	std::uniform_int_distribution<int> distribution(1, 49);
	auto numberGen = std::bind(distribution, generator);

	SuperDraw::SuperDraw() {
		this->ticketListHead = NULL;
		this->ticketListTail = NULL;
	}

	SuperDraw::SuperDraw(int num) {
		//Initialize the head and tail to NULL
		this->ticketListHead = NULL;
		this->ticketListTail = NULL;

		//Generate the new tickets
		for (int i = 0; i < num; i++) {
			this->newTicket(0);
		}

		//Print out the numbers of all of the generated tickets
		std::cout << num << " new tickets were successfully generated." << std::endl << "The numbers are:" << std::endl;
		ticket *temp = this->ticketListHead;
		while (temp != NULL) {
			for (int i = 0; i < 5; i++) {
				std::cout << temp->numbers[i] << ", ";
			}
			std::cout << temp->numbers[5] << std::endl;
			temp = temp->next;
		}
	}

	SuperDraw::SuperDraw(const SuperDraw &copyFrom) {
		if (copyFrom.ticketListHead == NULL) {
			this->ticketListHead = NULL;
			this->ticketListTail = NULL;
		}
		else {
			this->ticketListHead = NULL;
			this->ticketListTail = NULL;
			ticket *temp = copyFrom.ticketListHead;
			while (temp != NULL) {
				if (copyFrom.ticketListHead == copyFrom.ticketListTail) {
					this->ticketListHead = (ticket*) malloc(sizeof(ticket));
					this->ticketListHead->next = NULL;
					for (int i = 0; i < 6; i++) {
						this->ticketListHead->numbers[i] = temp->numbers[i];
					}
					this->ticketListTail = this->ticketListHead;
				}
				else {
					if (this->ticketListHead == NULL) {
						this->ticketListHead = (ticket*) malloc(sizeof(ticket));
						this->ticketListHead->next = NULL;
						this->ticketListTail = this->ticketListHead;
					}
					else {
						this->ticketListTail->next = (ticket*) malloc(sizeof(ticket));
						this->ticketListTail = this->ticketListTail->next;
						this->ticketListTail->next = NULL;
					}
					for (int i = 0; i < 6; i++) {
						this->ticketListTail->numbers[i] = temp->numbers[i];
					}
				}
				temp = temp->next;
			}
		}
	}

	SuperDraw::~SuperDraw() {
		while (this->ticketListHead != NULL) {
			ticket *temp = this->ticketListHead;
			this->ticketListHead = this->ticketListHead->next;
			free(temp);
		}
		this->ticketListHead = NULL;
		this->ticketListTail = NULL;
	}

	void SuperDraw::newTicket(int verbose) {
		//Adding a node
		if (this->ticketListHead == NULL && this->ticketListTail == NULL) {
			this->ticketListHead = (ticket*) malloc(sizeof(ticket));
			this->ticketListHead->next = NULL;
			this->ticketListTail = this->ticketListHead;
		}
		else {
			this->ticketListTail->next = (ticket*) malloc(sizeof(ticket));
			this->ticketListTail = this->ticketListTail->next;
			this->ticketListTail->next = NULL;
		}

		//Generating the ticket numbers
		int i = 0;
		while (i < 6) {
			bool exists = false;
			unsigned int newNum = numberGen();
			for (int j = 0; j < i; j++) {
				exists = exists || this->ticketListTail->numbers[j] == newNum;
			}
			if (!exists) {
				this->ticketListTail->numbers[i] = newNum;
				i++;
			}
		}

		//Could hav implemented my own sorting algorithm like "bubble sort" or any other divide and conquer or ignorant algorithms, but std::sort runs in nlog(n) time so I just used it
		std::sort(this->ticketListTail->numbers, this->ticketListTail->numbers + 6);

		//Checking for Verbose
		if (verbose) {
			std::cout << "A new ticket was successfully generated.  The numbers are: ";
			for (int i = 0; i < 5; i++) {
				std::cout << this->ticketListTail->numbers[i] << ", ";
			}
			std::cout << this->ticketListTail->numbers[5] << std::endl;
		}
	}

	void SuperDraw::printAllTicketNumbers() {
		if (this->ticketListHead == NULL) return;
		ticket *temp = this->ticketListHead;
		int num = 0;
		while (temp != NULL) {
			num++;
			temp = temp->next;
		}
		std::cout << "We found " << num << " generated tickets." << std::endl << "The numbers are:" << std::endl;
		temp = this->ticketListHead;
		while (temp != NULL) {
			for (int i = 0; i < 5; i++) {
				std::cout << temp->numbers[i] << ", ";
			}
			std::cout << temp->numbers[5] << std::endl;
			temp = temp->next;
		}
	}

	void SuperDraw::addSpecificSequence(unsigned int *sequence) {
		//Adding a node
		if (this->ticketListHead == NULL && this->ticketListTail == NULL) {
			this->ticketListHead = (ticket*) malloc(sizeof(ticket));
			this->ticketListHead->next = NULL;
			this->ticketListTail = this->ticketListHead;
		}
		else {
			this->ticketListTail->next = (ticket*) malloc(sizeof(ticket));
			this->ticketListTail = this->ticketListTail->next;
			this->ticketListTail->next = NULL;
		}

		//Attaching sequence to tail
		for (int i = 0; i < 6; i++) {
			this->ticketListTail->numbers[i] = sequence[i];
		}
	}

	//I AM ASSUMING THAT ORDER MATTERS FOR SEQUENCE VERIFICATION.
	void SuperDraw::verifySequence(unsigned int *sequence) {
		if (this->ticketListHead == NULL) return;
		ticket *temp = this->ticketListHead;
		bool exists;
		while (temp != NULL) {
			exists = true;
			for (int i = 0; i < 6; i++) {
				exists = exists && temp->numbers[i] == sequence[i];
			}

			if (exists) {
				break;
			}
			temp = temp->next;
		}
		if (exists) std::cout << "This sequence has been found!" << std::endl;
		else std::cout << "The sequence could not be found!" << std::endl;
	}

	void SuperDraw::deleteSequence(unsigned int *sequence) {
		if (this->ticketListHead == NULL) return;
		ticket *temp = this->ticketListHead;
		ticket *tempPrevious = NULL;
		bool exists;
		while (temp != NULL) {
			exists = true;
			for (int i = 0; i < 6; i++) {
				exists = exists && temp->numbers[i] == sequence[i];
			}

			if (exists) {
				if (temp == this->ticketListHead) {
					this->ticketListHead = this->ticketListHead->next;
					free(temp);
				}
				else if (temp == this->ticketListTail) {
					tempPrevious->next = NULL;
					free(temp);
				}
				else {
					tempPrevious->next = temp->next;
					free(temp);
				}
				break;
			}
			tempPrevious = temp;
			temp = temp->next;
		}
		if (exists) std::cout << "This sequence has been deleted" << std::endl;
		else std::cout << "The sequence could not be found!" << std::endl;
	}
} /* namespace Assignment1 */











