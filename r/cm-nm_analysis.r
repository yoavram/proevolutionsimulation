library(ggplot2)
library(plyr)

alpha <- 0.05
x.lab <- expression(tau)
y.lab <- expression("Mean fixation probability " %+-% " SEM")
#adaptive.color <- "green"#which(colors()=="green")
#deleterious.color <- "red"#which(colors()=="red")
#neutral.color <- "blue"# which(colors()=="blue")

bar.color.choice <- function(x, p) {
  pt <- prop.test(sum(x>(1-1/100000)), length(x), p=p, conf.level=1-alpha)
  if (pt$p.value > alpha) {
    return(0)
  }
  if (pt$estimate < 0.5) {
    return(-1)
  } else {
    return(1)
  }
}

cm.nm.data <- read.csv("D:/projects/simarba/analysis/aggregation_cm-nm_220712.csv.gz",header=T)
cm.nm.data$rb <- as.logical(cm.nm.data$rb)
cm.nm.data$start <- as.logical(cm.nm.data$start)
cm.nm.data <- cm.nm.data[cm.nm.data$tau <= 20 & cm.nm.data$r!=6e-4 & cm.nm.data$envch!=4e-8,]
sort(names(cm.nm.data))
dim(cm.nm.data)

fraction.summary <- ddply(cm.nm.data, .(envch,r,apl,rb,tau,muM,piM,rM,pi,mu,title,subtitle,in.,start,pi.original), summarise,
  N = length(cm.fraction),
  mean.fixation = mean(cm.fraction),
  fixation = sum(cm.fraction>(1-1/100000))/length(cm.fraction),                          
  sd = sd(cm.fraction),
  se = sd(cm.fraction)/sqrt(length(cm.fraction)),
  p.value = prop.test(sum(cm.fraction>(1-1/100000)), length(cm.fraction), p=unique(in.), conf.level=1-alpha)$p.value,
  selection = bar.color.choice(cm.fraction, unique(in.)))

fraction.summary$selection <- factor(fraction.summary$selection)
fraction.summary$envch <- factor(1e-5 / fraction.summary$envch) #factor(fraction.summary$envch))
fraction.summary$r <- factor(fraction.summary$r)
fraction.summary$apl <- factor(fraction.summary$apl)

#levels(fraction.summary$envch) <- paste(levels(fraction.summary$envch),"gen")# c("slow env 2e-8","medium env 1e-7","fast env 2e-7") 
#levels(fraction.summary$r) <- paste("r=",levels(fraction.summary$r),sep="")

limits <- aes(ymax = fixation + se, ymin=fixation - se)                           

p <- ggplot(fraction.summary, aes(x=factor(tau), y=fixation,  fill=selection, alpha=rb)) 
q <- p + geom_bar(stat='identity', position='dodge', colour='black') + 
    geom_errorbar(limits, width=0.75, position='dodge') +
    facet_grid(facets=r~envch + apl, ) +
    geom_hline(yintercept=0.5) +
    scale_fill_brewer(palette="Set1", name='selection', labels=c("-","X","+")) +
    scale_alpha_manual(values=c(1,0.4), name='rb', labels=c('F','T'))+
    #theme_bw() +
    xlab(x.lab) +
    ylab(y.lab) +
    ylim(c(0,1)) +
    opts(title="CM vs. NM")

ggsave("cm_vs_nm.pdf", q)

            
            
            