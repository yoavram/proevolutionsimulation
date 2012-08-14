library(ggplot2)
library(plyr)

alpha <- 0.05

my.prop.test <- function(x, n, p ) {
  return(prop.test(x=x, n=n, p=p, conf.level=1-alpha))
}

selection.test <- function(estimate, p.value) {
  if (unique(p.value) > alpha) {
    return(0)
  }
  if (unique(estimate) < 0.5) {
    return(-1)
  } else {
    return(1)
  }
}

my.func <- function(df) {
  N = length(df$invader.fraction)
  start <- unique(df$in.)
  wins <- sum(df$invader.fraction>start)
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
  v <- c(N, fixation, p.value, ci.high, ci.low, selection)
  names(v) <- c("N", "fixation", "p.value", "ci.high", "ci.low", "selection")
  return(v)
}

get.data <- function() {
  data<-read.csv("D:/projects/simarba/analysis/aggregation_140812.csv.gz",header=T)
  data$rb <- as.logical(data$rb)
  data$start <- as.logical(data$start)
  return (data)
}

fixation.summary <- function(data, use.pop=FALSE) {
  if (use.pop) {
    fraction.summary <- ddply(data, .(apl, envch, in., jobname, mu, pi, pi_original, pop, r, rb, rho, start, tau), my.func)
    fraction.summary$pop <- factor(fraction.summary$pop)
  } else {
    fraction.summary <- ddply(data, .(apl, envch, in., jobname, mu, pi, pi_original, r, rb, rho, start, tau), my.func)
  }
  
  fraction.summary$selection <- factor(fraction.summary$selection)
  fraction.summary$envch <- factor(1e-5 / fraction.summary$envch) 
  fraction.summary$r <- factor(fraction.summary$r)
  fraction.summary$apl <- factor(fraction.summary$apl)
  return(fraction.summary) 
}

plot.fixation <- function(fix.sum, x.factor = 'tau', use.pop = FALSE, use.rb=FALSE, x.lab=NULL, y.lab=expression("Mean fixation probability " %+-% " SEM"), plot.title="untitled") {
  limits <- aes(ymax = ci.high, ymin=ci.low)    
  if (x.factor=='tau') {
    q <- ggplot(fix.sum, aes(x=factor(tau), y=fixation,  fill=selection))
    if (is.null(x.lab)) {
      x.lab = expression(tau)
    }
  } else if (x.factor=='rho') {
    q <- ggplot(fix.sum, aes(x=factor(rho), y=fixation,  fill=selection))
    if (is.null(x.lab)) {
      x.lab = expression(rho)
    }
  } else if (x.factor=='pi') {
    q <- ggplot(fix.sum, aes(x=factor(pi), y=fixation,  fill=selection))
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
  if (use.rb) {
    scale_alpha_manual(values=c(1,0.4), name='rb', labels=c('F','T'))
  }
  
  return(q)
}
