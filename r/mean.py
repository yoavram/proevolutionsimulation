import gzip 
import os
import os.path
import glob
import pandas
        
os.chdir("/groups/lilach_hadany/yoavram/workspace/proevolutionsimulation/output/")
folders = glob.glob('invasion*')
count = 100

for fold in folders:
    print fold
    os.chdir(fold)
    for filename in glob.glob("*.csv.gz"):
	if count == 10:
		print "Exiting"
		exit()
	outname = "mean."+filename[:filename.find(".gz")]
	if not os.path.exists(outname):
            	print filename
            	fin = gzip.open(filename)
            	df = pandas.DataFrame.from_csv(fin)
            	mdf = pandas.DataFrame(df.mean(0))
            	mdf = mdf.transpose()
            	mdf.to_csv(outname, index=False)
	    	count += 1
    os.chdir("..")

print "Finished averaging records"
