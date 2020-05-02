import matplotlib.pyplot as plt
import sys
import csv
import numpy as np

data_file = sys.argv[1]

## plot prefit data

#data_str = np.array([])
#np.append(data,1)
data_str = []


with open('ex1data1.txt') as csv_file:
    csv_reader = csv.reader(csv_file, delimiter=',')    
    for row in csv_reader:   
        data_str.append(row)
        
data=np.asarray(data_str).astype(np.float)
#print(data)
#print(data[0])
#print(data.shape)
#print(data[:,1])

x = data[:,0]
y = data[:,1]

plt.scatter(x,y,marker="x",c="red")
plt.show()
