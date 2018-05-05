#!/bin/bash
DATA=`ls data/`
for f in $DATA
do
	mv data/$f query
	./movie_recommender query/$f data/*
	mv query/$f data
done
