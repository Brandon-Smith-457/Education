SRCS=src/movie_recommender.c src/distances.c src/preferences.c
HEAD=include/distances.h include/preferences.h
INCDIR=-Iinclude

movie_recommender: ${SRCS} ${HEAD}
	gcc ${INCDIR} ${SRCS} -o movie_recommender

clean:
	rm movie_recommender