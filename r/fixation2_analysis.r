library(ggplot2)
library(plyr)
source("multiplot.r")

alpha <- 0.05

get.data <- function() {
  data<-read.csv("D:/projects/simarba/analysis/aggregation_190812_0927.csv.gz",header=T)
  data$rb <- as.logical(data$rb)
  data$start <- as.logical(data$start)
  return (data)
}


my.func <- function(df) {
  N = length(df$invader.fraction)
  start <- unique(df$in.)
  
  wins <- sum(df$invader.fraction > 0.5)# ==1)
  
  pt <- prop.test(wins, N, p=start, conf.level=1-alpha)
  p.value <- pt$p.value
  fixation <- pt$estimate
  ci <- pt$conf.int
  ci.low <- ci[1]
  ci.high <- ci[2]
  selection <- 0
  if (p.value < alpha) {
    if (fixation < start) {
      selection <- -1
    } else {
      selection <- 1
    }
  }
  finish.rate <- 1-sum(df$invader.fraction!=1 & df$invader.fraction!=0)/N
  avg.tau <- mean(df$mean.mean.mutation.rate/df$mu)
  v <- c(N, fixation, p.value, ci.high, ci.low, selection, finish.rate, avg.tau)
  names(v) <- c("N", "fixation", "p.value", "ci.high", "ci.low", "selection", "finish.rate", "avg.tau")
  return(v)
}

fixation.summary <- function(data, use.pop=FALSE) {
  if (use.pop) {
    fraction.summary <- ddply(data, .(apl, envch, in., jobname, mu, pi1, pi2, pi_original, pop, r, rb, rho1, rho2, start, tau1, tau2), my.func)
    fraction.summary$pop <- factor(fraction.summary$pop)
  } else {
    fraction.summary <- ddply(data, .(apl, envch, in., jobname, mu, pi1, pi2, pi_original, r, rb, rho1, rho2, start, tau1, tau2), my.func)
  }
  
  fraction.summary$selection <- factor(fraction.summary$selection)
  fraction.summary$envch <- factor(1e-5 / fraction.summary$envch) 
  fraction.summary$r <- factor(fraction.summary$r)
  fraction.summary$apl <- factor(fraction.summary$apl)
  return(fraction.summary) 
}

plot.finish.rate <- function(fix.sum, x.factor = 'tau', use.pop = FALSE, use.rb=FALSE, x.lab=NULL, y.lab=expression("Finish Rate"), plot.title="untitled") {
  if (x.factor=='tau') {
    q <- ggplot(fix.sum, aes(x=factor(tau), y=finish.rate,  fill=selection))
    if (is.null(x.lab)) {
      x.lab = expression(tau)
    }
  } else if (x.factor=='rho') {
    q <- ggplot(fix.sum, aes(x=factor(rho), y=finish.rate,  fill=selection))
    if (is.null(x.lab)) {
      x.lab = expression(rho)
    }
  } else if (x.factor=='tau.rho') {
    q <- ggplot(fix.sum, aes(x=factor(tau), y=finish.rate,  fill=selection))
    if (is.null(x.lab)) {
      x.lab = expression(tau == rho)
    }
  }else if (x.factor=='pi') {
    q <- ggplot(fix.sum, aes(x=factor(pi), y=finish.rate,  fill=selection))
    if (is.null(x.lab)) {
      x.lab = expression(pi)
    }
  }
  if (use.rb) {
    q <- q + geom_bar(stat='identity', position='dodge', colour='black', fill='gray66', aes(alpha=rb))
  } else {
    q <- q + geom_bar(stat='identity', position='dodge', colour='black', fill='gray66')
  }
  q <- q + xlab(x.lab) +
    ylab(y.lab) +
    ylim(c(0,1)) +
    geom_hline(yintercept=c(0.9,0.95)) +
    opts(title=plot.title)
  if (use.pop) {
    q <- q + facet_grid(facets=r~envch+pop, )
  } else {
    q <- q + facet_grid(facets=r~envch, )
  }
  if (use.rb) {
    q <- q +scale_alpha_manual(values=c(1,0.4), name='rb', labels=c('F','T'))
  }
  return(q)
}

plot.fixation <- function(fix.sum, x.factor = 'tau', use.pop = FALSE, use.rb=FALSE, x.lab=NULL, y.lab=expression("Mean fixation probability " %+-% " CI"), plot.title="untitled", label.avg.tau=F) {
  limits <- aes(ymax = ci.high, ymin=ci.low)    
  if (x.factor=='tau') {
    q <- ggplot(fix.sum, aes(x=factor(tau2), y=fixation,  fill=selection))
    if (is.null(x.lab)) {
      x.lab = expression(tau)
    }
  } else if (x.factor=='rho') {
    q <- ggplot(fix.sum, aes(x=factor(rho2), y=fixation,  fill=selection))
    if (is.null(x.lab)) {
      x.lab = expression(rho)
    }
  } else if (x.factor=='tau.rho') {
    q <- ggplot(fix.sum, aes(x=factor(tau2), y=fixation,  fill=selection))
    if (is.null(x.lab)) {
      x.lab = expression(tau == rho)
    }
  }else if (x.factor=='pi') {
    q <- ggplot(fix.sum, aes(x=factor(pi1), y=fixation,  fill=selection))
    if (is.null(x.lab)) {
      x.lab = expression(pi)
    }
  }
  if (use.rb) {
    q <- q + geom_bar(stat='identity', position='dodge', colour='black', aes(alpha=rb))
  } else {
    q <- q + geom_bar(stat='identity', position='dodge', colour='black')
  }
  q <- q + geom_errorbar(limits, width=0.75, position='dodge') +
    geom_hline(yintercept=0.5) +
    # based on brewer with Set1
    scale_fill_manual(values = c( "-1" = "#E41A1C", "0" = "#377EB8", "1" = "#4DaF4A"), name='selection', breaks=c(-1,0,1), labels=c("-","X","+")) + 
    xlab(x.lab) +
    ylab(y.lab) +
    ylim(c(0,1)) +
    opts(title=plot.title)
  if (use.pop) {
    q <- q + facet_grid(facets=r~envch+pop, )
  } else {
    q <- q + facet_grid(facets=r~envch, )
  }
  if (label.avg.tau) {
    q <- q + geom_text(aes(label=format(avg.tau, digits=2), y=fixation/2))
    q <- q + geom_text(aes(label=format(tau2, digits=2), y=fixation/4))
  }
  if (use.rb) {
    q <- q + scale_alpha_manual(values=c(1,0.4), name='rb', labels=c('F','T'))
  }
  
  return(q)
}

data <- get.data()
data <- subset(data, filter=='pop' & jobname=='invasion2')

fs <- fixation.summary(subset(data, pi1>0 & tau1 > 1 & rho1 == 1 & pi2 == 0 & tau2>1 & rho2 == 1))
sim.cm <- plot.fixation(fs,x.factor='pi', plot.title='SIM vs. CM', label.avg.tau=T)
sim.cm

fs <- fixation.summary(subset(data, pi1>0 & tau1 > 1 & rho1 > 1 & pi2 == 0 & tau2>1 & rho2 > 1))
simr.cmr <- plot.fixation(fs,x.factor='pi', plot.title='SIMR vs. CMR', label.avg.tau=T)
simr.cmr

