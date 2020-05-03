#!/usr/local/opt/python@3/bin/python3

import matplotlib.pyplot as plt
import sys
import csv
import numpy as np

print("python code has run")

def polyStr(theta,n):
    master = ""
    for i in range(0,n):
        master+=str(round(theta[i],2))+"x^" + str(i)
        if(not(i == n-1)):
            master+=" + "
    return master


data_file = sys.argv[1]
pred_file = sys.argv[2]
data_str = []

with open(data_file) as csv_file:
    csv_reader = csv.reader(csv_file, delimiter=',')    
    for row in csv_reader:   
        data_str.append(row)
        
data=np.asarray(data_str).astype(np.float)

theta = data[0,:]
data = data[1:,:]
x = data[:,0]
y = data[:,-1]

data_str = []
with open(pred_file) as csv_file:
    csv_reader = csv.reader(csv_file, delimiter=',')    
    for row in csv_reader:   
        data_str.append(row)

pred=np.asarray(data_str).astype(np.float)
pred = pred[1:,:]
pred = pred[pred[:,0].argsort()]
x_pred = pred[:,0]
y_pred = pred[:,-1]
print(y_pred)

title = "Fitted Equation: " + polyStr(theta,theta.size)


fig = plt.figure()
ax = fig.add_subplot(111)
ax.scatter(x,y,marker="x",c="red",label="data")
ax.plot(x_pred,y_pred,c="blue",label="prediction")
ax.legend()
plt.title(title)
plt.xlabel("x")
plt.ylabel("y")
plt.savefig("FitVsData.png")
#plt.show()



###plot the line for each iteration of the lin reg
