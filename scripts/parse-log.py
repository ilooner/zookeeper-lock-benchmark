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
        for key, value in dictionary.iteritems():
            writer.writerow([key.replace('^.*Results-Aggregated-','')])
            writer.writerow(value)
    
def writefinal(file, dictionary):
    with open(file, 'w') as target:
        writer=csv.writer(target, delimiter=',', quoting = csv.QUOTE_NONE,escapechar='\\')
        for key, value in dictionary.iteritems():
            print value
            writer.writerow(value)

if len(sys.argv) > 2 or len(sys.argv) <= 1:
   print "Please provide directory as the input!!"
   sys.exit()

if not os.path.isdir(sys.argv[1]):
   print "Provided input is not a directory!!!"
   sys.exit()

if os.path.isfile(sys.argv[1]+'/final-results') and os.access(sys.argv[1]+'/final-results', os.R_OK):
    os.remove(sys.argv[1]+'/final-results')

if os.path.isfile(sys.argv[1]+'/iops-output') and os.access(sys.argv[1]+'/iops-output', os.R_OK):
    os.remove(sys.argv[1]+'/iops-output')

d = dict()
for f in get_all_files_in_dir(sys.argv[1]):
   (test, testname) = get_test_name(f)
   for line in aggregate_finder(f):
       (key, value) = line.split(':')
       if key in d:
           d[key].append(clean(testname) + ',' + clean(value))
       else:
           d[key] = [clean(testname) + ',' + clean(value)]

write(sys.argv[1]+'/final-results', d)
testnamedict = dict()
for key, value in d.iteritems():
    results = [x.split(',') for x in value]
    for item in results:
        if key+item[0] in testnamedict:
            testnamedict[key+item[0]].append(item)
        else:
            testnamedict[key+item[0]] = [item]
#print testnamedict
output = dict()
for key, value in testnamedict.iteritems():
    output[key] = [sum([int(x[1]) for x in value]), max([int(x[3]) for x in value])]
#print output

total = dict()
for key, value in output.iteritems():
    item = key.split(' ')[0][:4]+key.split(' ')[1]
    if item in total:
        total[item].append(value)
    else:
        total[item] = [value]
#print total

final = dict()
for key, value in total.iteritems():
    totaltimeinsec = sum(int(x[1]) for x in value)/1000
    if totaltimeinsec==0:
        final[key] = [clean(key), 0]
    else:
        final[key] = [clean(key), min(int(x[0]) for x in value) / totaltimeinsec]
writefinal(sys.argv[1]+'/iops-output', final)
