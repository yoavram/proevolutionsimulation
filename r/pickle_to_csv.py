from gzip import open
import pickle, pandas
# this is used to convert a pickle file containing a list of dicts to a csv file containing an R-valid data frame
# all files compressed with gzip - to cancel this just remove the import and change the file names

#input_filename = "aggregation_all.pickle.gz"
#output_filename = "aggregation_all.csv.gz"

def pickle_to_csv(input_filename,output_filename):
    fin = open(input_filename)
    inp = pickle.load(fin)
    fin.close()
    df = pandas.DataFrame(inp)
    fout = open(output_filename, 'wb')
    df.to_csv(fout)
    fout.close()

