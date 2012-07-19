cat("Starting\n")
setwd("~/workspace/proevolutionsimulation/output")
output <- matrix(0,0,26)
for (folder in dir(pattern="*envch*")) { # pattern used to avoid ess folders
setwd(folder)
cat(folder)
cat('\n')
files <- dir(pattern="pop.*.csv") # filenames
splits <- strsplit(files, split=".", fixed=T) # split filenames on dots, used for subtitle, rb, start
params <- strsplit(files, split="\\D+\\.") # split on non-digits followed by dot, used for numberical parameters
for (i in seq(length(files))) {
fin <- files[i]
cat(fin)
cat('\n')
split <- splits[[i]]
#cat(paste("split",split,sep='\n'))
title <- split[2] 
param <- as.numeric(params[[i]][2:12])
split.tail <- tail(split, 5)
#cat(paste("split.tail",split.tail,sep='\n'))
start <- split.tail[1]
rb <- split.tail[3]
data <- read.csv(fin, header=T)
last.row <- tail(data, 1)
v <- c("pop", title, param, start, rb, as.numeric(last.row)) # data record
#cat(paste('\n',v,'\n'))
output <- rbind(output, v)
}
setwd("..")
}
output <- as.data.frame(output)
names(output) <- c("title","subtitle","mu","r","pi.original","muM","rM","piM","apl","tau","pi","in","envch","start","rb",names(last.row))
#cat(names(output))
setwd("~/workspace/proevolutionsimulation")
fout <- gzfile("aggregation.csv.gz")
write.csv(output, fout, row.names=F, quote=F)
cat("\nWriting to aggregation.csv.gz\n")
