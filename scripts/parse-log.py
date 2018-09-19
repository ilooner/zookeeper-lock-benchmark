#!/usr/bin/env python

import os,sys,csv,re

def aggregate_finder(file):
    fl = open(file, 'r')
    for line in fl:
        if 'Results-Aggregated' in line:
             yield re.sub('^.*Results-Aggregated-', '', line)

def clean(x):
    if x.endswith("\r\n"): return x[:-2]
    if x.endswith("\n") or x.endswith("\r"): return x[:-1]
    return x

def get_test_name(file):
    fl = open(file, 'r')
    for line in fl:
        if 'TestName' in line:
            return line.replace('^.*TestName','TestName').split(':')

def get_all_files_in_dir(path):
    for root, dirs, files in os.walk(path):
        for file_ in files:
            yield os.path.join(root, file_)

def write(file, dictionary):
    with open(file, 'w') as target:
        writer=csv.writer(target, delimiter='\n', quoting = csv.QUOTE_NONE,escapechar='\\')
        for key, value in d.iteritems():
            writer.writerow([key.replace('^.*Results-Aggregated-','')])
            writer.writerow(value)
    

if len(sys.argv) > 2 or len(sys.argv) <= 1:
   print "Please provide directory as the input!!"
   sys.exit()

if not os.path.isdir(sys.argv[1]):
   print "Provided input is not a directory!!!"
   sys.exit()

d = dict()
for f in get_all_files_in_dir(sys.argv[1]):
   (test, testname) = get_test_name(f)
   for line in aggregate_finder(f):
       (key, value) = line.split(':')
       if key in d:
           d[key].append(clean(testname) + ':' + clean(value))
       else:
           d[key] = [clean(testname) + ':' + clean(value)]
write(sys.argv[1]+'/final-results', d)
