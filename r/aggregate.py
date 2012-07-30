import gzip 
import re
import os
import pickle

def filter_pop_csv(filename):
    return (filename.startswith('pop.') and filename.endswith('.csv'))
        
# TODO simr patterns
pattern = re.compile(r'^(?:(?P<subtitle>\w+)\.)?(?P<title>[\w-]*)\.mu\.(?P<mu>\d\.\d+)\.r\.(?P<r>\d\.\d+)\.pi\.(?P<pi_original>\d+)\.muM\.(?P<muM>\d+)\.rM\.(?P<rM>\d+)\.piM\.(?P<piM>\d+)\.apl\.(?P<apl>\d+)\.(?:tau\.(?P<tau>[\.\d]+)|tauS\.(?P<tauS>[\.\d]+)\.tauC\.(?P<tauC>[\.\d]+))\.pi\.(?P<pi>\d+)\.in\.(?P<in>\d\.\d+)\.envch\.(?P<envch>\d\.?\d*)\.start\.(?P<start>[\w]+)\.rb\.(?P<rb>[\w]+)(?:\.(?P<time>\d{4}-\w{3}-\d{1,2}_\d{2}-\d{2}-\d{2}-\d{3}_\w{3})\.(?P<filetype>\w+))?$')

os.chdir("/groups/lilach_hadany/yoavram/workspace/proevolutionsimulation/output/")
folders = os.listdir('.')
records = []

for fold in folders:
    print fold
    m=re.match(pattern,fold)
    if not m:
        print "*** Failed matching pattern"
        continue
    params = m.groupdict()
    files = os.listdir(fold)
    files = filter(filter_pop_csv, files)
    for filename in files:
        print filename
        stdout = os.popen("head -n1 "+fold+"/"+filename)
        head = stdout.read()
        if head.startswith("head"):
            print "*** Error with file:",err
            continue
        stdout = os.popen("tail -n1 "+fold+"/"+filename)
        tail = stdout.read()
        if tail.startswith("tail"):
            print "*** Error with file:",err
            continue

        fields = head.strip().split(',')
        values = tail.strip().split(',')
        
        record = dict(zip(fields,values))
        record.update(params)

        records.append(record)
        

print "Finished collecting record"
#df = pandas.DataFrame(records)
fout_name = "aggregation.pickle.gz"
fout = gzip.open(fout_name, 'wb')
pickle.dump(records, fout)
print "Written output list of dicts to file in pickle format",fout_name

