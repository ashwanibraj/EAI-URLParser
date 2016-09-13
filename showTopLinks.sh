#!/bin/bash

limit=$1

if [ "$limit" -le 0 ]; then
	echo "Argument must be greater than 0."
elif [ "$limit" -gt 500 ]; then
	echo "Argument must be less than or equal to 500."
else
	curl -v --silent www.alexa.com/topsites 2>&1 | grep -i "siteinfo/[^']" | grep -P '[^>]+.\w+(?=</a>)' -o | head -n $limit

	((limit = limit-25))

	if [ "$1" -gt 25 ]; then
		((N = ($1-1)/25))
		for (( i=1; i<=N; i++ ))
		do
			curl -v --silent "www.alexa.com/topsites/global;$i" 2>&1 | grep -i "siteinfo/[^']" | grep -P '[^>]+.\w+(?=</a>)' -o | head -n $limit
			((limit = limit-25))
		done
	fi
fi