import gzip 
import re
import os
import pickle
import glob
        
# final.pop.invasion.pop.3.mu.0.003.r.0.003.pi.1001.apl.11.pi.0.tau.10.rho.1.in.0.5.envch.0.00000002.start.false.rb.false.2012-Aug-10_00-12-50-001_IDT.csv

pattern = re.compile(r"^final.(?P<filter>\w+)\.(?P<jobname>[\w-]*)\.pop\.(?P<pop>\d+)\.mu\.(?P<mu>\d\.\d+)\.r\.(?P<r>\d\.\d+)\.pi\.(?P<pi_original>\d+)\.apl\.(?P<apl>\d+)\.pi\.(?P<pi>\d+)\.tau\.(?P<tau>[\.\d]+)\.rho\.(?P<rho>\d+)\.in\.(?P<in>\d\.\d+)\.envch\.(?P<envch>\d\.?\d*)\.start\.(?P<start>[\w]+)\.rb\.(?P<rb>[\w]+)(?:\.(?P<time>\d{4}-\w{3}-\d{1,2}_\d{2}-\d{2}-\d{2}-\d{3}_\w{3})\.(?P<filetype>\w+))?$")

os.chdir("/groups/lilach_hadany/yoavram/workspace/proevolutionsimulation/output/")
folders = glob.glob('invasion*')
records = []

for fold in folders:
    print fold
    os.chdir(fold)
    for filename in glob.glob("final.*.csv"):
	print filename
	m = re.match(pattern, filename)
	if not m:
        	print "*** Failed matching pattern"
        	continue
    	params = m.groupdict()
	fin = open(filename)
        head = fin.readline() 
        tail = fin.readline() 
        fields = head.strip().split(',')
        values = tail.strip().split(',')
        
        record = dict(zip(fields,values))
        record.update(params)

        records.append(record)
    os.chdir("..")

print "Finished collecting record"
#df = pandas.DataFrame(records)
fout_name = "aggregation.pickle.gz"
fout = gzip.open(fout_name, 'wb')
pickle.dump(records, fout)
print "Written output list of dicts to file in pickle format",fout_name

