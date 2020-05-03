# linear_regression
linear regression on data set using gradient descent
![Fit vs Data](FitVsData.png)

Run the code through the driver class, which has an example of how to run this over a set of cubic data

The jave code does most of the work however at the end a python executable is called which generates the plot of the fit and data. To get this to work was a bit tricky as I had to setup the version of python being used inside the python script which is something I have never done before.

Feel free to run this on your own data set, I recommed sticking to a system of only 2 variables i.e. x and y though I believe I wrote the code such that it works with any number of variables corresponding to a single y, i.e. y = x+z should work fine, but the graph wont display anything in 2D.

Also when doing the fitting be aware that the fit can fail and diverge giving you a useless result, to resolve this usually descreasing alpha works, if the fit is doing well but doesn't reach a minimum increase the number of iterations
