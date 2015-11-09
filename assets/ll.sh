#!/system/bin/bash

#defined function
list_dirs(){
	for file in $1/*
	do
	if [ -f $file ];
	then echo $file
	else list_dirs $file
	fi
	done
}
list_dirs "."