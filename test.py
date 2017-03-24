import subprocess
def testStrassen(n, c, min, max):
	sp = subprocess.call("./strassen 6 " + str(n) + " " + str(c) + " " + str(min) + " " + str(max), shell=True)

def testRegular(n, min, max):
	sp = subprocess.call("./strassen 7 " + str(n) + str(min) + " " + str(max), shell=True)

sp = subprocess.call("make", shell=True)
c = 10
for x in xrange(10):
	print(str(c) + " ")
	sp = testStrassen(512, c, 0, 1)
	c = c + 1

sp = subprocess.call("make clean", shell=True)