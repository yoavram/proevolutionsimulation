library(ggplot2)
library(plyr)

alpha <- 0.05
x.lab <- expression(tau)
y.lab <- expression("Mean fixation probability " %+-% " SEM")

selection.test <- function(x, p) {
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

data<-read.csv("aggregation_cm-nm_100812.csv.gz",header=T)
data <- subset(data, filter=='pop')
sort(names(data))
dim(data)
data$rb <- as.logical(data$rb)
data$start <- as.logical(data$start)

fraction.summary <- ddply(data, .(envch, in., jobname, mu, pi, pi_original, pop, r, rb, rho, start, tau), summarise, #pop, apl
                          N = length(invader.fraction),
                          mean.fixation = mean(invader.fraction),
                          fixation = sum(invader.fraction>(1-1/100000))/length(invader.fraction),                          
                          se = sd(invader.fraction)/sqrt(length(invader.fraction)),
                          p.value = prop.test(sum(invader.fraction>(1-1/100000)), length(invader.fraction), p=unique(in.), conf.level=1-alpha)$p.value,
                          selection = selection.test(invader.fraction, unique(in.)))

fraction.summary$selection <- factor(fraction.summary$selection)
fraction.summary$envch <- factor(1e-5 / fraction.summary$envch) #factor(fraction.summary$envch))
fraction.summary$r <- factor(fraction.summary$r)
fraction.summary$apl <- factor(fraction.summary$apl)
fraction.summary$pop <- factor(fraction.summary$pop)

limits <- aes(ymax = fixation + se, ymin=fixation - se)                           

p <- ggplot(fraction.summary, aes(x=factor(tau), y=fixation,  fill=selection))#, alpha=rb)) 
q <- p + geom_bar(stat='identity', position='dodge', colour='black') + 
  geom_errorbar(limits, width=0.75, position='dodge') +
  facet_grid(facets=r~envch+pop, ) + #apl
  geom_hline(yintercept=0.5) +
  scale_fill_brewer(palette="Set1", name='selection', labels=c("-","X","+")) +
  #scale_alpha_manual(values=c(1,0.4), name='rb', labels=c('F','T'))+ # for alpha=rb
  xlab(x.lab) +
  ylab(y.lab) +
  ylim(c(0,1)) +
  opts(title="CM vs. NM")
q

ggsave("cm_vs_nm_by_pop.pdf", q)