import subprocess
def testStrassen(n, c, file):
	return subprocess.check_output("./strassen 4 " + str(n) + " " + str(c) + " " + file, shell=True)

def testRegular(n, file):
	return subprocess.check_output("./strassen 5 " + str(n) + " " + file, shell=True)

sp = subprocess.call("make", shell=True)
times = [0 for x in xrange(7)]

for y in xrange(30):
	c=0
	sp = subprocess.call("./strassen 8 512 -1 1 out.txt", shell=True)
	for x in xrange(7):
		times[x] = times[x] + int(testStrassen(512, 125+x,"out.txt")[:-1])
		print(str(x))
print("RESULTS\n-------")
for i in xrange(60):
	print(str(125+i) + "\t" + str(times[i]))
sp = subprocess.call("make clean", shell=True)