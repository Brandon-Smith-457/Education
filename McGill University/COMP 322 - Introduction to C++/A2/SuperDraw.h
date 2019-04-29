/*
 * SuperDraw.h
 *
 *  Created on: Mar. 9, 2019
 *      Author: Brandon Smith
 */

#ifndef SUPERDRAW_H_
#define SUPERDRAW_H_

#include <stdlib.h>
#include <random>
#include <functional>
#include <iostream>
#include <chrono>
#include <algorithm>

namespace SuperDraw {
	struct ticket {
		unsigned int numbers[6];
		ticket *next;
	};

	class SuperDraw {
	public:
		SuperDraw();
		SuperDraw(int num);
		SuperDraw(const SuperDraw &copyFrom);
		virtual ~SuperDraw();
		void newTicket(int verbose);
		void printAllTicketNumbers();
		void verifySequence(unsigned int *sequence);
		void addSpecificSequence(unsigned int *sequence);
		void deleteSequence(unsigned int *sequence);
	private:
		ticket *ticketListHead;
		ticket *ticketListTail;
	};
}
#endif /* SUPERDRAW_H_ */
