library(ggplot2)
library(plyr)

alpha <- 0.05
x.lab <- expression(pi)
y.lab <- expression("Mean fixation probability " %+-% " SEM")

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

sim.nm.data<-read.csv("D:/projects/simarba/analysis/aggregation_sim-nm_220712.csv.gz",header=T)
sim.nm.data <- sim.nm.data[sim.nm.data$tau == 10 & sim.nm.data$r!=6e-4 & sim.nm.data$envch!=4e-8,]
sim.nm.data$rb <- as.logical(sim.nm.data$rb)
sim.nm.data <- sim.nm.data[which(!(sim.nm.data$r==0 & sim.nm.data$rb==T)),]
sim.nm.data$start <- as.logical(sim.nm.data$start)
sort(names(sim.nm.data))
dim(sim.nm.data)

fraction.summary <- ddply(sim.nm.data, .(envch,r,apl,rb,tau,muM,piM,rM,pi,mu,title,subtitle,in.,start,pi.original), summarise,
                          N = length(sim.fraction),
                          mean.fixation = mean(sim.fraction),
                          fixation = sum(sim.fraction>(1-1/100000))/length(sim.fraction),                          
                          sd = sd(sim.fraction),
                          se = sd(sim.fraction)/sqrt(length(sim.fraction)),
                          p.value = prop.test(sum(sim.fraction>(1-1/100000)), length(sim.fraction), p=unique(in.), conf.level=1-alpha)$p.value,
                          selection = bar.color.choice(sim.fraction, unique(in.)))

fraction.summary$selection <- factor(fraction.summary$selection)
fraction.summary$envch <- factor(1e-5 / fraction.summary$envch) 
fraction.summary$r <- factor(fraction.summary$r)
fraction.summary$apl <- factor(fraction.summary$apl)

#levels(fraction.summary$envch) <- paste(levels(fraction.summary$envch),"gen")
#levels(fraction.summary$r) <- paste("r=",levels(fraction.summary$r),sep="")
#levels(fraction.summary$apl) <- paste("apl=",fraction.summary$apl,sep="")

limits <- aes(ymax = fixation + se, ymin=fixation - se)                           

p <- ggplot(fraction.summary, aes(x=factor(pi), y=fixation,  fill=selection, alpha=rb)) 
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
  opts(title="SIM vs. NM")

ggsave("sim_vs_nm.pdf", q)



