import matplotlib.pyplot as pl

f = open("log/q2.txt", "r")
s = f.read()

ss = s.split('\n')

x = []
y = []
x2 = []
xy = []

for i in range(len(ss) - 1):
    r = ss[i].split(' ')
    x.append(float(r[0]))
    y.append(float(r[1]))

pl.scatter(x, y, s=1, c="b")
pl.show()
